package cn.usfl.zhihu.screen

import android.annotation.SuppressLint
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
import cn.usfl.zhihu.model.AnswerModel.AnswerState.*
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.ui.component.BackAndForwardHeader
import cn.usfl.zhihu.ui.component.PlainTextField
import cn.usfl.zhihu.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CreateAnswerScreen(
    navController: NavHostController = rememberNavController(),
    vm: MainViewModel = viewModel(),
    askId: Int
) {
    var ask by remember {
        mutableStateOf(
            Ask(0, vm.context.getString(R.string.loading),"",0)
        )
    }
    thread {
        vm.scope.launch {
            val askTemp = vm.db.askDao().getAskById(askId)
            if (askTemp != null) {
                ask = askTemp
            }
        }
    }
    var content by remember {
        mutableStateOf("")
    }
    Column {
        BackAndForwardHeader(
            navController = navController,
            name = stringResource(id = R.string.write_answer),
            forward = {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(15))
                    .background(Color.Blue, RoundedCornerShape(15))
                    .clickable {
                        vm.scope.launch {
                            when (vm.answerModel.publish(ask, content)) {
                                SUCCESS -> {
                                    navController.navigate(
                                        "${Route.ask}/${ask.id}",
                                        NavOptions
                                            .Builder()
                                            .setPopUpTo(Route.ask, false)
                                            .build()
                                    )
                                    vm.toast(vm.context.getString(R.string.publish_success))
                                }
                                ANSWER_EXIST -> vm.toast(vm.context.getString(R.string.answer_exist))
                                NOT_LOGIN -> {
                                    navController.navigate(Route.login)
                                    vm.toast(vm.context.getString(R.string.login_first))
                                }
                                CONTENT_EMPTY -> vm.toast(vm.context.getString(R.string.answer_empty))
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
                value = ask.title,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                enabled = false
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
                placeholder = "请输入回答\n可通过 !![img]图片地址!! 插入图片\n通过 !![bili]BV号!! 插入Bilibili视频",
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
