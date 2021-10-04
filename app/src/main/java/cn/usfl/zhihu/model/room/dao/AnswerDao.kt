package cn.usfl.zhihu.model.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import cn.usfl.zhihu.model.room.entity.Answer

@Dao
interface AnswerDao {
    @Query("SELECT * FROM answers where ask_id == :askId order by created_time desc")
    fun getAllByTime(askId: Int): PagingSource<Int,Answer>

    @Query("SELECT * FROM answers order by likes_count desc")
    fun getAllByLikes() : PagingSource<Int, Answer>

    @Query("SELECT * FROM answers order by agrees_count desc")
    fun getAllByAgrees() : PagingSource<Int, Answer>

    @Query("SELECT * FROM answers order by comments_count desc")
    fun getAllByComments() : PagingSource<Int, Answer>

    @Query("SELECT * FROM answers order by marks_count desc")
    fun getAllByMarks() : PagingSource<Int, Answer>

    @Query("SELECT * FROM answers " +
            "order by comments_count*2+likes_count+agrees_count+marks_count,created_time desc")
    fun getAllByHot() : PagingSource<Int, Answer>

    @Query("SELECT * FROM answers where id == :id")
    suspend fun getAnswerById(id:Int): Answer?

    @Query("SELECT * FROM answers where content == :content")
    suspend fun getAnswerByContent(content:String): Answer?

    @Insert
    suspend fun insert(answer: Answer)

    @Update
    suspend fun update(answer: Answer)

    @Delete
    suspend fun delete(answer: Answer)
}