package cn.usfl.zhihu.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import cn.usfl.zhihu.model.*
import cn.usfl.zhihu.model.room.AppDatabase

class MainViewModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    val scope = viewModelScope
    lateinit var db: AppDatabase
    lateinit var spUser: SharedPreferences
    lateinit var userModel: UserModel
    lateinit var askModel: AskModel
    lateinit var answerModel: AnswerModel
    lateinit var messageModel: MessageModel
    lateinit var commentModel: CommentModel
    fun setContexts(context: Context) {
        this.context = context
        this.db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        ).build()
        spUser = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        userModel = UserModel(this)
        askModel = AskModel(this)
        answerModel = AnswerModel(this)
        messageModel = MessageModel(this)
        commentModel = CommentModel(this)
    }

    fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}