package cn.usfl.zhihu.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.usfl.zhihu.model.room.dao.*
import cn.usfl.zhihu.model.room.entity.*

@Database(
    entities = [
        User::class,
        Ask::class,
        Answer::class,
        Action::class,
        Message::class,
        Comment::class,
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun askDao(): AskDao
    abstract fun answerDao(): AnswerDao
    abstract fun actionDao(): ActionDao
    abstract fun messageDao(): MessageDao
    abstract fun commentDao(): CommentDao
}