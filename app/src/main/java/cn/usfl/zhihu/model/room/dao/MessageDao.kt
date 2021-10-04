package cn.usfl.zhihu.model.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.usfl.zhihu.model.ActionState
import cn.usfl.zhihu.model.room.entity.Message

@Dao
interface MessageDao {
    @Query("select * from message where uid = :uid and type = ${Message.MessageType.like} " +
            "order by created_time desc")
    fun getAllLikes(uid:Int): PagingSource<Int,Message>

    @Query("select * from message where uid = :uid and type = ${Message.MessageType.at} "+
            "order by created_time desc")
    fun getAllAts(uid:Int): PagingSource<Int,Message>

    @Query("select * from message where uid = :uid and type = ${Message.MessageType.message} "+
            "order by created_time desc")
    fun getAllMessages(uid:Int): PagingSource<Int,Message>

    @Query("select * from message where uid = :uid and type = ${Message.MessageType.comment} "+
            "order by created_time desc")
    fun getAllComments(uid:Int): PagingSource<Int,Message>

    @Query("select * from message where uid = :uid and type = ${Message.MessageType.follow} "+
            "order by created_time desc")
    fun getAllFollows(uid:Int): PagingSource<Int,Message>

    @Insert
    suspend fun insert(message: Message)

    @Delete
    suspend fun delete(message: Message)
}