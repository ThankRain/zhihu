package cn.usfl.zhihu.model

import cn.usfl.zhihu.model.room.entity.Action
import cn.usfl.zhihu.model.room.entity.User
import cn.usfl.zhihu.utils.md5s
import cn.usfl.zhihu.viewmodel.MainViewModel

class UserModel(private val vm: MainViewModel) {
    private var db = vm.db

    /**
     * 登录
     * @param name 用户名
     * @param password 密码
     */
    suspend fun login(name: String, password: String): LoginState {
        when(true){
            name.isEmpty() -> return LoginState.USERNAME_EMPTY
            password.isEmpty() -> return LoginState.PASSWORD_EMPTY
        }
        val user = db.userDao().findUserByName(name)
            ?: return LoginState.USER_UNKNOWN//用户不存在
        if (user.password == md5s(password)){
            vm.spUser.edit().putString("login_user",name).apply()
            return LoginState.SUCCESS//登录成功
        }
        return LoginState.PASSWORD_ERROR//密码错误
    }


    /**
     * 获取已登录用户名
     */
    fun getUserName() = vm.spUser.getString("login_user","")?:""

    suspend fun getCurrentUser(): User? {
        val name = getUserName()
        if (!name.isEmpty()){
            return db.userDao().findUserByName(name)
        }
        return null
    }

    /**
     * 注册新用户
     * @param name 用户名
     * @param password 密码
     * @param avatar 头像，目前为输入QQ号自动获取QQ头像 若非QQ号，则作为
     */
    suspend fun register(name: String, password: String, avatar: String): RegisterState {
        when(true){
            name.isEmpty() -> return RegisterState.USERNAME_EMPTY
            password.isEmpty() -> return RegisterState.PASSWORD_EMPTY
            avatar.isEmpty() -> return RegisterState.AVATAR_EMPTY
        }
        if (db.userDao().findUserByName(name) == null) {
            val avatarStr = if (avatar.toLongOrNull() == null) {
                    //非数字，判断是否为url
                    if (avatar.contains("://")) {
                        //初步鉴定为url
                        avatar
                    } else {
                        //非url，返回错误
                        return RegisterState.AVATAR_ERROR
                    }
                } else {
                    //QQ号
                    "https://q1.qlogo.cn/g?b=qq&nk=${avatar}&s=100"
                }//获取|转化|判断头像地址
            //开始注册
            db.userDao().register(
                User(
                    name = name,
                    password = md5s(password),//密码进行md5加盐加密
                    avatar = avatarStr
                )
            )
            //判断是否注册成功
            if (db.userDao().findUserByName(name) != null) {
                return RegisterState.SUCCESS//注册成功
            }
            return RegisterState.UNKNOWN_ERROR//未知错误
        }
        return RegisterState.USER_EXISTED//用户已存在
    }

    /**
     * 根据用户名获取头像
     */
    suspend fun getAvatar(name: String):String{
        val user = db.userDao().findUserByName(name)
            ?: return "https://img.zcool.cn/community/0133c25e71c9baa80120a895b194eb.jpg@1280w_1l_2o_100sh.jpg"
        return user.avatar
    }
    
    suspend fun follow(id: Int): Int {
        val objectUser = db.userDao().findUserById(id)//被关注者
        val currentUser = vm.userModel.getCurrentUser() ?: return ActionState.NOT_LOGIN//关注者
        if (objectUser != null) {
            val action = db.actionDao().getHasAction(
                type = Action.ActionType.UserFollow,
                uid = currentUser.id,
                objId = objectUser.id
            )
            if (action == null) {
                //Not Like 
                val actionTemp = Action(
                    type = Action.ActionType.UserFollow,
                    uid = currentUser.id,
                    objId = objectUser.id
                )
                objectUser.followsCount++
                db.actionDao().setAction(actionTemp)
                db.userDao().update(objectUser)
                return ActionState.ACTION_SUCCESS
            } else {
                objectUser.followsCount--
                db.actionDao().cancelAction(action)
                db.userDao().update(objectUser)
                return ActionState.ACTION_CANCEL
            }
        }
        return ActionState.OBJECT_NOT_EXIST
    }
    
    enum class LoginState {//登录结果枚举类
    SUCCESS,
        USER_UNKNOWN,
        PASSWORD_ERROR,
        USERNAME_EMPTY,
        PASSWORD_EMPTY,
    }
    enum class RegisterState {//注册结果枚举类
    SUCCESS,
        USER_EXISTED,
        AVATAR_ERROR,
        UNKNOWN_ERROR,
        USERNAME_EMPTY,
        PASSWORD_EMPTY,
        AVATAR_EMPTY,
    }
}
