package cn.usfl.zhihu.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.usfl.zhihu.R
import cn.usfl.zhihu.model.room.entity.Answer
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.model.room.entity.User
import cn.usfl.zhihu.ui.component.BackAndForwardHeader
import cn.usfl.zhihu.ui.component.MarkdownField
import cn.usfl.zhihu.utils.DefaultAvatar
import cn.usfl.zhihu.utils.GreyAvatar
import cn.usfl.zhihu.utils.asTime
import cn.usfl.zhihu.viewmodel.MainViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@Composable
fun AskScreen(
    navController: NavHostController,
    vm: MainViewModel,
    askId: Int
) {
    var ask by remember {
        mutableStateOf(
            Ask(0, vm.context.getString(R.string.loading), "", 0)
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
    val paging = vm.db.answerDao().getAllByHot()
    Column {
        BackAndForwardHeader(name = "", navController = navController)
        AskWidget(ask = ask)
        AnswerList(paging, vm)
    }
}

@Composable
fun AskWidget(ask: Ask) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(ask.title)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.clickable {
            isExpand = !isExpand
        }) {
            MarkdownField(
                value = ask.content,
                onValueChange = {},
                readOnly = true,
                maxLines = if (isExpand) Int.MAX_VALUE else 2
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Column(
                Modifier
                    .weight(1f)
                    .clickable {
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit_line),
                    contentDescription = stringResource(id = R.string.write_answer),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(id = R.string.write_answer),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .clickable {

                    }) {
                Image(
                    painter = painterResource(id = R.drawable.follow_add_line),
                    contentDescription = stringResource(id = R.string.add_follow),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(id = R.string.add_follow),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }//写回答，关注问题

    }
}

@Composable
fun AnswerList(paging: PagingSource<Int, Answer>, vm: MainViewModel) {
    val flow =
        Pager(PagingConfig(15)) {
            paging
        }.flow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        content = {
            items(flow) { answer ->
                answer?.let { AnswerItem(answer = it, vm) }
            }
            if (flow.itemCount == 0) {
                item {
                    Box(Modifier.fillMaxHeight()) {
                        Text(
                            text = stringResource(id = R.string.empty),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        })
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AnswerItem(answer: Answer, vm: MainViewModel) {
    var user by remember {
        mutableStateOf(
            User(
                name = "匿名用户",
                avatar = DefaultAvatar,
                password = "",
            )
        )
    }
    thread {
        vm.scope.launch {
            user = vm.db.userDao().findUserById(answer.uid) ?: User(
                name = "用户已注销",
                avatar = GreyAvatar,
                password = "",
            )
        }
    }
    Column {
        Row(Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(user.avatar),
                contentDescription = stringResource(id = R.string.avatar),
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = user.name,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
        Text(
            text = answer.content,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        // todo Image & Content
        Text(
            text = "${answer.readersCount} 浏览 · ${answer.agreesCount} 赞同 · " +
                    "${answer.likesCount} 喜欢 · ${answer.commentsCount} 评论 · " +
                    answer.createdTime.asTime(),
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 12.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )

    }

}
