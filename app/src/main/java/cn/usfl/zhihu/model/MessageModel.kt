package cn.usfl.zhihu.model

import cn.usfl.zhihu.model.AnswerModel.AnswerState.*
import cn.usfl.zhihu.model.room.entity.Action
import cn.usfl.zhihu.model.room.entity.Answer
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.model.room.entity.Message
import cn.usfl.zhihu.viewmodel.MainViewModel

class MessageModel(private val vm: MainViewModel) {
    private var db = vm.db

    private suspend fun isReferExist(referType: Int, referId: Int) =
        when (referType) {
            Message.ReferType.ask -> vm.askModel.get(referId)?.uid
            Message.ReferType.answer -> vm.answerModel.get(referId)?.uid
            Message.ReferType.user -> db.userDao().findUserById(referId)?.id
            //Message.ReferType.comment -> vm.comment.get(referId) != null todo 完善comment
            else -> null
        }

    suspend fun sendMsg(objId: Int, msg: String): Int {
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        val obj = db.userDao().findUserById(objId) ?: return ActionState.OBJECT_NOT_EXIST
        val message = Message(
            uid = user.id,
            message = msg,
            type = Message.MessageType.message,
            objId = objId,
        )
        db.messageDao().insert(message)
        return ActionState.ACTION_SUCCESS
    }

    suspend fun likeMsg(referType: Int, referId: Int): Int {
        if (referType !in arrayOf(Message.ReferType.ask, Message.ReferType.answer))
            return ActionState.ACTION_CANCEL//不允许的引用对象
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN //用户未登录
        val objId = isReferExist(referType, referId) ?: return ActionState.REFER_NOT_EXIST //引用对象不存在
        db.userDao().findUserById(objId) ?: return ActionState.OBJECT_NOT_EXIST //发信对象不存在
        val message = Message(
            uid = user.id,
            type = Message.MessageType.like,
            objId = objId,
            referType = referType,
            referId = referId
        )
        db.messageDao().insert(message)
        return ActionState.ACTION_SUCCESS
    }

    suspend fun followMsg(referType: Int, referId: Int): Int {
        if (referType !in arrayOf(Message.ReferType.ask, Message.ReferType.user))
            return ActionState.ACTION_CANCEL//不允许的引用对象
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN //获取当前用户
        val objId = isReferExist(referType, referId) ?: return ActionState.REFER_NOT_EXIST //引用对象不存在
        db.userDao().findUserById(objId) ?: return ActionState.OBJECT_NOT_EXIST //发信对象不存在
        val message = Message(
            uid = user.id,
            type = Message.MessageType.follow,
            objId = objId,
            referType = referType,
            referId = referId
        )
        db.messageDao().insert(message)
        return ActionState.ACTION_SUCCESS
    }

    suspend fun atMsg(referType: Int, referId: Int): Int {
        if (referType !in arrayOf(
                Message.ReferType.ask,
                Message.ReferType.answer,
                Message.ReferType.comment
            )
        )
            return ActionState.ACTION_CANCEL//不允许的引用对象
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN //获取当前用户
        val objId = isReferExist(referType, referId) ?: return ActionState.REFER_NOT_EXIST //引用对象不存在
        db.userDao().findUserById(objId) ?: return ActionState.OBJECT_NOT_EXIST //发信对象不存在
        val message = Message(
            uid = user.id,
            type = Message.MessageType.at,
            objId = objId,
            referType = referType,
            referId = referId
        )
        db.messageDao().insert(message)
        return ActionState.ACTION_SUCCESS
    }

    suspend fun commentMsg(referType: Int, referId: Int): Int {
        if (referType !in arrayOf(
                Message.ReferType.answer,
                Message.ReferType.comment
            )
        )
            return ActionState.ACTION_CANCEL//不允许的引用对象
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN //获取当前用户
        val objId = isReferExist(referType, referId) ?: return ActionState.REFER_NOT_EXIST //引用对象不存在
        db.userDao().findUserById(objId) ?: return ActionState.OBJECT_NOT_EXIST //发信对象不存在
        val message = Message(
            uid = user.id,
            type = Message.MessageType.comment,
            objId = objId,
            referType = referType,
            referId = referId
        )
        db.messageDao().insert(message)
        return ActionState.ACTION_SUCCESS
    }



}