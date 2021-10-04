package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class Answer(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "ask_id") var askId:Int,
    @ColumnInfo(name = "content") var content:String,
    @ColumnInfo(name = "uid") var uid:Int,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_time") var updatedTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_anonymous") var isAnonymous:Boolean = false,
    @ColumnInfo(name = "comments_count") var commentsCount:Int = 0,
    @ColumnInfo(name = "readers_count") var readersCount:Int = 0,
    @ColumnInfo(name = "agrees_count") var agreesCount:Int = 0,
    @ColumnInfo(name = "likes_count") var likesCount:Int = 0,
    @ColumnInfo(name = "marks_count") var marksCount:Int = 0,
    @ColumnInfo(name = "status") var status:Int = 0,
)