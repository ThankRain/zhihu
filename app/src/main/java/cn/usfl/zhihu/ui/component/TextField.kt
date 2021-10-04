package cn.usfl.zhihu.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.usfl.zhihu.R
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@Composable
fun LabelTextField(
    modifier: Modifier = Modifier,
    lineLabel: Painter? = null,
    fillLabel: Painter? = null,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorColor: Color = Color.Black
) {
    var focused by remember { mutableStateOf(true) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged {
                focused = it.isFocused
            },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = {
            Row(
                Modifier
                    .border(
                        1.dp,
                        if (focused) Color.Blue else Color.LightGray,
                        RoundedCornerShape(25)
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                if (lineLabel != null) {
                    Image(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = if (focused) fillLabel ?: lineLabel else lineLabel,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(if (focused) Color.Blue else Color.LightGray),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    color = (Color.Transparent),
                ) {
                    it()
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(
                                color = Color.LightGray
                            ),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }

            }
        }
    )
}

/**
 * Plain TextField 普通TextEditor(带Hint)
 */
@Composable
fun PlainTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorColor: Color = Color.Black
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = {
            Surface(
                modifier = Modifier,
                color = (Color.Transparent),
            ) {
                it()
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(
                            color = Color.LightGray

                        ),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }
    )
}

/**
 * Markdown 富文本编辑框
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun MarkdownField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorColor: Color = Color.Black
) {
    val rawTexts = remember {
        value.split("!!")// 按 !! 分割
    }
    LazyColumn(content = {
        items(rawTexts) { rawText ->
            if (rawText.startsWith("[img]") && rawText.contains("://")) {
                //图片
                val url = rawText.substring(5)
                Image(
                    painter = rememberImagePainter(url),
                    contentDescription = stringResource(id = R.string.picture)
                )
            } else if (rawText.startsWith("[bili]") && rawText.contains("://")) {
                //图片
                val url = rawText.substring(6)
                /**
                 * <iframe
                 * src="//player.bilibili.com/
                 * player.html?aid=463204239
                 * &bvid=BV1JL41147nY&cid=414675283&page=1"
                 * scrolling="no" border="0"
                 * frameborder="no" framespacing="0" allowfullscreen="true">
                 *     </iframe>
                 */
                Box(modifier = Modifier.fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
                ) {
                    Text(
                        text = "This is a Video",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }else {
                Text(text = rawText)
            }
        }
    })

}

/**
 * 定义视频格式 ![][!video_path]
 * 定义图片格式 ![][image_path]
 */

@Preview
@Composable
fun MarkdownTest() {
    MarkdownField(value = "# Android Studio \n## Markdown Test", onValueChange = {})
}