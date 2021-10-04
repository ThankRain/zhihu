package cn.usfl.zhihu.model

import cn.usfl.zhihu.model.AskModel.AskState.*
import cn.usfl.zhihu.model.room.entity.Action
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.viewmodel.MainViewModel

class AskModel(private val vm: MainViewModel) {
    private var db = vm.db

    suspend fun publish(title: String, content: String): AskState {
        if (title.isEmpty()) return TITLE_EMPTY
        if (content.isEmpty()) return CONTENT_EMPTY
        val user = vm.userModel.getCurrentUser() ?: return NOT_LOGIN
        if (db.askDao().getAskByTitle(title) == null){
            db.askDao().insert(Ask(
                title = title,
                content = content,
                uid = user.id
            ))
            return SUCCESS
        }
        return ASK_EXIST
    }

    suspend fun get(id:Int): Ask? {
        val ask = db.askDao().getAskById(id)
        if (ask != null){
            ask.readersCount ++
            db.askDao().update(ask)
        }
        return ask
    }

    suspend fun like(id: Int): Int {
        val ask = db.askDao().getAskById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (ask != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.AskLike,
                uid = user.id,
                objId = ask.id
            )
            if (action == null) {
                //Not Like 
                val actionTemp = Action(
                    type = Action.ActionType.AskLike,
                    uid = user.id,
                    objId = ask.id
                )
                ask.likesCount++
                db.actionDao().setAction(actionTemp)
                db.askDao().update(ask)
                return ActionState.ACTION_SUCCESS
            } else {
                ask.likesCount--
                db.actionDao().cancelAction(action)
                db.askDao().update(ask)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    suspend fun follow(id: Int): Int {
        val ask = db.askDao().getAskById(id)
        val user = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN
        if (ask != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.AskFollow,
                uid = user.id,
                objId = ask.id
            )
            if (action == null) {
                //Not Like
                val actionTemp = Action(
                    type = Action.ActionType.AskFollow,
                    uid = user.id,
                    objId = ask.id
                )
                ask.followsCount++
                db.actionDao().setAction(actionTemp)
                db.askDao().update(ask)
                return ActionState.ACTION_SUCCESS
            } else {
                ask.followsCount--
                db.actionDao().cancelAction(action)
                db.askDao().update(ask)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }

    enum class AskState {//发布结果枚举类
    SUCCESS,
        ASK_EXIST,
        NOT_LOGIN,
        TITLE_EMPTY,
        CONTENT_EMPTY,
    }

}