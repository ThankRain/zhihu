package cn.usfl.zhihu.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cn.usfl.zhihu.R



/**
 * 顶部Bar（带Forward按钮）
 */
@Composable
fun BackAndForwardHeader(
    name: String,
    navController: NavHostController,
    forward: @Composable () -> Unit = {},
) {
    Row(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
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
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(Modifier.align(Alignment.CenterVertically)) {
            forward()
        }
    }
}