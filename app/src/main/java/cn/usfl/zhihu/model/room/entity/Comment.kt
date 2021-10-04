package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "refer_id") val referId: Int,
    @ColumnInfo(name = "refer_type") val referType: Int,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_time") var updatedTime:Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_anonymous") var isAnonymous:Boolean = false,
    @ColumnInfo(name = "comments_count") var commentsCount:Int = 0,
    @ColumnInfo(name = "likes_count") var likesCount:Int = 0,
    @ColumnInfo(name = "status") var status:Int = 0,
){
    object ReferType{
        const val answer = 2
        const val comment = 4
    }
}