package cn.usfl.zhihu.model

import cn.usfl.zhihu.model.AnswerModel.AnswerState.*
import cn.usfl.zhihu.model.room.entity.Action
import cn.usfl.zhihu.model.room.entity.Answer
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.viewmodel.MainViewModel

class AnswerModel(private val vm: MainViewModel) {
    private var db = vm.db

    suspend fun publish(ask: Ask, content: String): AnswerState {
        if (content.isEmpty()) return CONTENT_EMPTY
        val user = vm.userModel.getCurrentUser() ?: return NOT_LOGIN
        if (db.answerDao().getAnswerByContent(content) == null) {
            db.answerDao().insert(
                Answer(
                    askId = ask.id,
                    content = content,
                    uid = user.id
                )
            )
            ask.answersCount++
            db.askDao().update(ask)
            return SUCCESS
        }
        return ANSWER_EXIST
    }


    suspend fun get(id: Int): Answer? {
        val answer = db.answerDao().getAnswerById(id)
        if (answer != null) {
            answer.readersCount++
            db.answerDao().update(answer)
        }
        return answer
    }

    suspend fun like(id: Int): Int {
        val answer = db.answerDao().getAnswerById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (answer != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.AnswerLike,
                uid = user.id,
                objId = answer.id
            )
            if (action == null) {
                //Not Like
                val actionTemp = Action(
                    type = Action.ActionType.AnswerLike,
                    uid = user.id,
                    objId = answer.id
                )
                answer.likesCount++
                db.actionDao().setAction(actionTemp)
                db.answerDao().update(answer)
                return ActionState.ACTION_SUCCESS
            } else {
                answer.likesCount--
                db.actionDao().cancelAction(action)
                db.answerDao().update(answer)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    suspend fun agree(id: Int): Int {
        val answer = db.answerDao().getAnswerById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (answer != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.AnswerAgree,
                uid = user.id,
                objId = answer.id
            )
            if (action == null) {
                //Not Like
                val actionTemp = Action(
                    type = Action.ActionType.AnswerAgree,
                    uid = user.id,
                    objId = answer.id
                )
                answer.agreesCount++
                db.actionDao().setAction(actionTemp)
                db.answerDao().update(answer)
                return ActionState.ACTION_SUCCESS
            } else {
                answer.agreesCount--
                db.actionDao().cancelAction(action)
                db.answerDao().update(answer)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    suspend fun mark(id: Int): Int {
        val answer = db.answerDao().getAnswerById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (answer != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.AnswerMark,
                uid = user.id,
                objId = answer.id
            )
            if (action == null) {
                //Not Like
                val actionTemp = Action(
                    type = Action.ActionType.AnswerMark,
                    uid = user.id,
                    objId = answer.id
                )
                answer.marksCount++
                db.actionDao().setAction(actionTemp)
                db.answerDao().update(answer)
                return ActionState.ACTION_SUCCESS
            } else {
                answer.marksCount--
                db.actionDao().cancelAction(action)
                db.answerDao().update(answer)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    enum class AnswerState {
        //发布结果枚举类
        SUCCESS,
        ANSWER_EXIST,
        NOT_LOGIN,
        CONTENT_EMPTY,
    }

}