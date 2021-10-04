package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asks")
data class Ask(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "content") var content:String,
    @ColumnInfo(name = "uid") var uid:Int,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_time") var updatedTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_anonymous") var isAnonymous:Boolean = false,
    @ColumnInfo(name = "answers_count") var answersCount:Int = 0,
    @ColumnInfo(name = "follows_count") var followsCount:Int = 0,
    @ColumnInfo(name = "likes_count") var likesCount:Int = 0,
    @ColumnInfo(name = "readers_count") var readersCount:Int = 0,
    @ColumnInfo(name = "status") var status:Int = 0,
)