package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "avatar") var avatar: String,
    @ColumnInfo(name = "likes_count") var likes: Int = 0,
    @ColumnInfo(name = "asks_count") var asks: Int = 0,
    @ColumnInfo(name = "answers_count") var answers: Int = 0,
    @ColumnInfo(name = "follows_count") var followsCount:Int = 0,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "status") var status: Int = 0,//-1 Banned,0 Normal,1 Verified
){
    override fun equals(other: Any?): Boolean {
        return when(other){
            is User -> id == other.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + likes
        result = 31 * result + asks
        result = 31 * result + answers
        result = 31 * result + status
        return result
    }

}
