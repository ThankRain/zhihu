package cn.usfl.zhihu.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cn.usfl.zhihu.R
import cn.usfl.zhihu.model.UserModel.LoginState
import cn.usfl.zhihu.model.UserModel.RegisterState.*
import cn.usfl.zhihu.ui.component.LabelTextField
import cn.usfl.zhihu.utils.DefaultAvatar
import cn.usfl.zhihu.viewmodel.MainViewModel
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController,vm: MainViewModel = viewModel()) {
    val isLogin = remember {
        mutableStateOf(true)
    }
    Column {
        HeadBar(isLogin,navController)
        InputArea(isLogin,navController,vm)
    }
}

@Composable
private fun HeadBar(
    isLogin: MutableState<Boolean>,
    navController: NavHostController
) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.back_line),
            contentDescription = stringResource(id = R.string.close),
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable {
                    navController.popBackStack()
                }
                .width(32.dp)
                .height(32.dp)
                .padding(4.dp)
        )
        Row(Modifier.padding(vertical = 8.dp)) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = if (isLogin.value) R.string.login else R.string.register),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                )
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun InputArea(
    isLogin: MutableState<Boolean>,
    navController: NavHostController,
    vm: MainViewModel = viewModel()
) {
    var name by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var avatar by remember {
        mutableStateOf("")
    }
    var avatarReal by remember {
        mutableStateOf(DefaultAvatar)
    }
    Column(Modifier.padding(horizontal = 16.dp)) {
        Image(
            painter = rememberImagePainter(avatarReal),
            contentDescription = stringResource(id = R.string.avatar),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .width(64.dp)
                .height(64.dp)
                .border(2.dp, Color.White, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
        )
        LabelTextField(
            modifier = Modifier.fillMaxWidth(),
            lineLabel = painterResource(id = R.drawable.user_line),
            fillLabel = painterResource(id = R.drawable.user_fill),
            value = name,
            onValueChange = {
                name = it
                vm.scope.launch {
                    avatarReal = vm.userModel.getAvatar(it)
                }
            },
            singleLine = true,
            placeholder = stringResource(id = R.string.username)
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = !isLogin.value) {
            LabelTextField(
                modifier = Modifier.fillMaxWidth(),
                lineLabel = painterResource(id = R.drawable.avatar_line),
                fillLabel = painterResource(id = R.drawable.avatar_fill),
                value = avatar,
                onValueChange = {
                    avatar = it
                    avatarReal = if (avatar.toLongOrNull() == null) {
                        //非数字，判断是否为url
                        if (avatar.contains("://")) {
                            //初步鉴定为url
                            avatar
                        } else {
                            //非url，返回错误
                            "https://img.zcool.cn/community/0133c25e71c9baa80120a895b194eb.jpg@1280w_1l_2o_100sh.jpg"
                        }
                    } else {
                        //QQ号
                        "https://q1.qlogo.cn/g?b=qq&nk=$avatar&s=100"
                    }//获取|转化|判断头像地址
                },
                singleLine = true,
                placeholder = stringResource(id = R.string.enter_avatar)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LabelTextField(
            modifier = Modifier.fillMaxWidth(),
            lineLabel = painterResource(id = R.drawable.key_line),
            fillLabel = painterResource(id = R.drawable.key_fill),
            value = password,
            onValueChange = {
                password = it
            },
            singleLine = true,
            placeholder = stringResource(id = R.string.password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = if (!isLogin.value) R.string.login else R.string.register),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .align(Alignment.CenterVertically)
                    .clickable {
                        isLogin.value = !isLogin.value
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                style = TextStyle(
                    color = Color.Blue
                )
            )
            Spacer(modifier = Modifier.weight(1f))

            //Login|Register
            Box(
                Modifier
                    .height(64.dp)
                    .width(64.dp)
                    .background(Color.Blue, RoundedCornerShape(50))
                    .align(Alignment.CenterVertically)
                    .clickable {
                        vm.scope.launch {
                            if (isLogin.value) {
                                when (vm.userModel.login(name, password)) {
                                    LoginState.SUCCESS -> {
                                        vm.toast(vm.context.getString(R.string.login_success))
                                        navController.popBackStack()
                                    }
                                    LoginState.USER_UNKNOWN -> vm.toast(vm.context.getString(R.string.user_unknown))
                                    LoginState.PASSWORD_ERROR -> vm.toast(vm.context.getString(R.string.password_error))
                                    LoginState.USERNAME_EMPTY -> vm.toast(vm.context.getString(R.string.username_empty))
                                    LoginState.PASSWORD_EMPTY -> vm.toast(vm.context.getString(R.string.password_empty))
                                }
                            } else {
                                when (vm.userModel.register(name, password, avatar)) {
                                    SUCCESS -> vm.toast(vm.context.getString(R.string.register_succeeded))
                                    USER_EXISTED -> vm.toast(vm.context.getString(R.string.user_exist))
                                    AVATAR_ERROR -> vm.toast(vm.context.getString(R.string.avatar_error))
                                    UNKNOWN_ERROR -> vm.toast(vm.context.getString(R.string.unknown_error))
                                    USERNAME_EMPTY -> vm.toast(vm.context.getString(R.string.username_empty))
                                    PASSWORD_EMPTY -> vm.toast(vm.context.getString(R.string.password_empty))
                                    AVATAR_EMPTY -> vm.toast(vm.context.getString(R.string.avatar_empty))
                                }
                            }
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_line),
                    contentDescription = stringResource(id = if (isLogin.value) R.string.login else R.string.register),
                    modifier = Modifier
                        .rotate(180f)
                        .height(24.dp)
                        .width(24.dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}