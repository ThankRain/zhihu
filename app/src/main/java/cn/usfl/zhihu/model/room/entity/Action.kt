package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actions")//行为 点赞 关注 收藏 赞同
data class Action(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "action_type") val type: ActionType,
    @ColumnInfo(name = "object_id") val objId: Int,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
) {
    enum class ActionType {
        AskLike,
        AskFollow,
        AnswerLike,
        AnswerAgree,
        AnswerMark,
        UserFollow,
        CommentLike,
    }
}