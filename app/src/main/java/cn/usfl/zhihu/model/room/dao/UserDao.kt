package cn.usfl.zhihu.model.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import cn.usfl.zhihu.model.room.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users " +
            "order by follows_count+likes_count+answers_count*3+asks_count*2 desc")
    fun getAll(): PagingSource<Int, User>

    @Query("SELECT * FROM users WHERE id == :uid")
    suspend fun findUserById(uid: Int): User?

    @Query("SELECT * FROM users WHERE name == :name")
    fun findUsersByName(name: String): PagingSource<Int, User>

    @Query("SELECT * FROM users WHERE name == :name")
    suspend fun findUserByName(name: String): User?

    @Insert
    suspend fun register(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}