package cn.usfl.zhihu.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import cn.usfl.zhihu.R
import cn.usfl.zhihu.Route
import cn.usfl.zhihu.model.AskModel.AskState.*
import cn.usfl.zhihu.ui.component.BackAndForwardHeader
import cn.usfl.zhihu.ui.component.PlainTextField
import cn.usfl.zhihu.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateAskScreen(
    navController: NavHostController = rememberNavController(),
    vm: MainViewModel = viewModel()
) {
    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }
    Column {
        BackAndForwardHeader(
            navController = navController,
            name = stringResource(id = R.string.new_ask),
            forward = {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(15))
                    .background(Color.Blue, RoundedCornerShape(15))
                    .clickable {
                        vm.scope.launch {
                            when (vm.askModel.publish(title, content)) {
                                SUCCESS -> {
                                    navController.navigate(
                                        Route.asks,
                                        NavOptions
                                            .Builder()
                                            .setPopUpTo(Route.ask, false)
                                            .build()
                                    )
                                    vm.toast("发布成功")
                                }
                                ASK_EXIST -> vm.toast("提问已存在")
                                NOT_LOGIN -> {
                                    navController.navigate(Route.login)
                                    vm.toast("请先登录")
                                }
                                TITLE_EMPTY -> vm.toast("标题不能为空")
                                CONTENT_EMPTY -> vm.toast("描述不能为空")
                            }
                        }
                    }
                    .width(64.dp)
                    .height(32.dp)
                ) {
                    Text(
                        stringResource(id = R.string.publish),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        )
        Column(
            Modifier
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            PlainTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = "输入问题并以问号结尾",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(0.5.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PlainTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = "请对问题加以描述\n可通过 !![img]图片地址!! 插入图片\n通过 !![bili]BV号!! 插入Bilibili视频",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                textStyle = TextStyle(
                    fontSize = 16.sp
                )
            )
        }
    }
}
