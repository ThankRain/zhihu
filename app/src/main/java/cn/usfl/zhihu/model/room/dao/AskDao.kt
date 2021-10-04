package cn.usfl.zhihu.model.room.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import cn.usfl.zhihu.model.room.entity.Ask
import kotlinx.coroutines.flow.Flow

@Dao
interface AskDao {
    @Query("SELECT * FROM asks order by created_time desc")
    fun getAllByTime() : PagingSource<Int,Ask>

    @Query("SELECT * FROM asks order by readers_count desc")
    fun getAllByRead() : PagingSource<Int,Ask>

    @Query("SELECT * FROM asks order by answers_count desc")
    fun getAllByAnswer() : PagingSource<Int,Ask>

    @Query("SELECT * FROM asks order by follows_count desc")
    fun getAllByFocus() : PagingSource<Int,Ask>

    @Query("SELECT * FROM asks order by answers_count*2+follows_count+readers_count*0.1 desc")
    fun getAllByHot() : PagingSource<Int,Ask>

    @Query("SELECT * FROM asks where id == :askId")
    suspend fun getAskById(askId:Int): Ask?

    @Query("SELECT * FROM asks where title == :title")
    suspend fun getAskByTitle(title:String): Ask?

    @Insert
    suspend fun insert(ask: Ask)

    @Update
    suspend fun update(ask: Ask)

    @Delete
    suspend fun delete(ask: Ask)
}