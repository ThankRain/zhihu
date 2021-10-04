package cn.usfl.zhihu.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "uid") val uid: Int = 0,
    @ColumnInfo(name = "message") val message: String = "",
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "object_id") val objId: Int,
    @ColumnInfo(name = "refer_id") val referId: Int = 0,
    @ColumnInfo(name = "refer_type") val referType: Int = ReferType.none,
    @ColumnInfo(name = "created_time") var createdTime:Long = System.currentTimeMillis(),
    ){
    object MessageType{
        const val like = 0
        const val comment = 1
        const val at = 2
        const val follow = 3
        const val message = 4
    }
    object ReferType{
        const val none = 0
        const val ask = 1
        const val answer = 2
        const val user = 3
        const val comment = 4
    }
}