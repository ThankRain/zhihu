package cn.usfl.zhihu.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.usfl.zhihu.model.room.entity.Action

@Dao
interface ActionDao {
    @Query("SELECT * from actions where action_type = :type and uid = :uid and object_id = :objId")
    fun getHasAction(type: Action.ActionType,uid: Int,objId: Int):Action?

    @Insert
    fun setAction(action: Action)

    @Delete
    fun cancelAction(action: Action)
}