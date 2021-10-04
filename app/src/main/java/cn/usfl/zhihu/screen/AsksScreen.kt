package cn.usfl.zhihu.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.usfl.zhihu.R
import cn.usfl.zhihu.Route
import cn.usfl.zhihu.model.room.entity.Ask
import cn.usfl.zhihu.ui.component.BackAndForwardHeader
import cn.usfl.zhihu.viewmodel.MainViewModel

@Composable
fun AsksScreen(
    navController: NavHostController,
    vm: MainViewModel
) {
    val flow =
        Pager(PagingConfig(15)) {
            vm.db.askDao().getAllByRead()
        }.flow
            .collectAsLazyPagingItems()
    BackAndForwardHeader(name = "回答问题", navController = navController)

    LazyColumn(content = {

        items(flow) { ask ->
            ask?.let {
                AskItem(navController = navController,ask = it)
            }
        }
    })
}

@Composable
fun AskItem(navController: NavHostController,ask: Ask) {
    Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(15))
                .clickable {
                    navController.navigate("${Route.ask}/${ask.id}")
                }
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = ask.title,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "${ask.readersCount} 浏览 · ${ask.answersCount} 回答 · ${ask.followsCount} 关注",
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.Blue)
                        .clickable {
                            navController.navigate("${Route.createAnswer}/${ask.id}")
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.write_answer),
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}