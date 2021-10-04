package cn.usfl.zhihu.model.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import cn.usfl.zhihu.model.room.entity.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments where refer_id == :referId and refer_type = :referType " +
            "order by created_time desc")
    fun getAllByTime(referType: Int,referId: Int): PagingSource<Int,Comment>

    @Query("SELECT * FROM comments order by likes_count,comments_count desc")
    fun getAllByLikes() : PagingSource<Int, Comment>

    @Query("SELECT * FROM comments order by comments_count,likes_count desc")
    fun getAllByComments() : PagingSource<Int, Comment>

    @Query("SELECT * FROM comments order by comments_count*2+likes_count desc")
    fun getAllByHot() : PagingSource<Int, Comment>

    @Query("SELECT * FROM comments where id == :id")
    suspend fun getCommentById(id:Int): Comment?

    @Query("SELECT * FROM comments where content == :content")
    suspend fun getCommentByContent(content:String): Comment?

    @Insert
    suspend fun insert(Comment: Comment)

    @Update
    suspend fun update(Comment: Comment)

    @Delete
    suspend fun delete(Comment: Comment)
}