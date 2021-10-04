package cn.usfl.zhihu.model

import cn.usfl.zhihu.model.CommentModel.CommentState.*
import cn.usfl.zhihu.model.room.entity.Action
import cn.usfl.zhihu.model.room.entity.Answer
import cn.usfl.zhihu.model.room.entity.Comment
import cn.usfl.zhihu.model.room.entity.Message
import cn.usfl.zhihu.viewmodel.MainViewModel

class CommentModel(private val vm: MainViewModel) {
    private var db = vm.db

    private suspend fun isReferExist(referType: Int, referId: Int): Any? {
        return when (referType) {
            Message.ReferType.answer -> vm.answerModel.get(referId)
            Message.ReferType.comment -> vm.commentModel.get(referId)
            else -> null
        }
    }

    suspend fun comment(referType: Int, referId: Int, content: String): CommentState {
        if (referType !in arrayOf(Message.ReferType.comment, Message.ReferType.answer))
            return ACTION_CANCEL//不允许的引用对象
        if (content.isEmpty()) return CONTENT_EMPTY
        val obj = isReferExist(referType, referId) ?: return REFER_NOT_EXIST //引用对象不存在
        val user = vm.userModel.getCurrentUser() ?: return NOT_LOGIN
        if (db.commentDao().getCommentByContent(content) == null) {
            db.commentDao().insert(
                Comment(
                    referType = referType,
                    referId = referId,
                    content = content,
                    uid = user.id
                )
            )
            when (obj) {
                is Comment -> {
                    obj.commentsCount++
                    db.commentDao().update(obj)
                }
                is Answer -> {
                    obj.commentsCount++
                    db.answerDao().update(obj)
                }
                else -> {
                }
            }
            return SUCCESS
        }
        return COMMENT_EXIST
    }

    suspend fun delete(comment: Comment): CommentDeleteState {
        val referId = comment.referId
        val referType = comment.referType
        val user = vm.userModel.getCurrentUser() ?: return CommentDeleteState.NOT_LOGIN//判断用户是否登录
        get(comment.id) ?: return CommentDeleteState.COMMENT_NOT_EXIST//判断评论是否存在
        if (user.id != comment.uid) return CommentDeleteState.NO_ACCESS//是否本人
        db.commentDao().delete(comment)//删除
        val obj = isReferExist(referType, referId) ?: return CommentDeleteState.REFER_NOT_EXIST //引用对象不存在
        when (obj) {//引用对象存在，评论数-1
            is Comment -> {
                obj.commentsCount--
                db.commentDao().update(obj)
            }
            is Answer -> {
                obj.commentsCount--
                db.answerDao().update(obj)
            }
            else -> {
            }
        }
        return CommentDeleteState.SUCCESS
    }

    suspend fun get(id: Int) = db.commentDao().getCommentById(id)

    suspend fun like(id: Int): Int {
        val comment = db.commentDao().getCommentById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (comment != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.CommentLike,
                uid = user.id,
                objId = comment.id
            )
            if (action == null) {
                //Not Like
                val actionTemp = Action(
                    type = Action.ActionType.CommentLike,
                    uid = user.id,
                    objId = comment.id
                )
                comment.likesCount++
                db.actionDao().setAction(actionTemp)
                db.commentDao().update(comment)
                return ActionState.ACTION_SUCCESS
            } else {
                comment.likesCount--
                db.actionDao().cancelAction(action)
                db.commentDao().update(comment)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    enum class CommentState {


        //发布结果枚举类
        SUCCESS,
        ACTION_CANCEL,
        REFER_NOT_EXIST,
        COMMENT_EXIST,
        NOT_LOGIN,
        CONTENT_EMPTY,
    }
    enum class CommentDeleteState {


        //发布结果枚举类
        SUCCESS,
        ACTION_CANCEL,
        REFER_NOT_EXIST,
        NO_ACCESS,
        COMMENT_NOT_EXIST,
        NOT_LOGIN,
    }

}