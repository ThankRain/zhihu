package cn.usfl.zhihu.utils

import androidx.compose.ui.text.AnnotatedString
import cn.usfl.zhihu.utils.TextStyle.*
import cn.usfl.zhihu.utils.WordParserState.*

fun wordParser(text: String) {
    val maker = AnnotatedStringMaker(text)
    var templeStartIndex = 0 // 临时定位指针，便于之后定位句子
    var state = Normal
    var titleCounter = 0
    var mediaCounter = 0
    for (i in text.indices){
        when(text[i]){
            '#' -> {
                titleCounter ++ //临时计数器 +1
                state = when(state){
                    NextStart -> DoubtTitle
                    else -> state
                }
            }
            ' ' ->{
                when(state){
                    DoubtTitle -> {
                        //正式标题
                        templeStartIndex = i - titleCounter - 1
                        state = InTitle
                    }
                    OutMedia -> {
                        //图片
                        maker.addStyle(Image,0,templeStartIndex,i)
                    }
                    IsVideo -> {
                        //视频
                        maker.addStyle(Video,0,templeStartIndex,i)
                    }
                    else -> {}
                }
            }
            '!' ->{
                if (state == Normal||state == NextStart) {
                    state = DoubtMedia //可能是Image|Video
                    mediaCounter = 1
                    templeStartIndex = i
                }else if (state == DoubtMedia||state == DoubtTitle){
                    state = Normal //不符合预判格式，恢复正常
                }else if (state == OutMedia){
                    state = IsVideo
                    mediaCounter = 0
                }
            }
            '[' ->{
                when(state){
                    DoubtMedia -> {
                        if (mediaCounter == 1){
                            mediaCounter ++
                        }
                        else if (mediaCounter == 3) {
                            state = InMedia
                            mediaCounter ++
                        }
                    }
                    else -> {}
                }
            }
            ']' -> {
                when(state){
                    DoubtMedia -> {
                        if (mediaCounter == 2){
                            mediaCounter ++
                        }
                        else if (mediaCounter == 4) {
                            state = OutMedia
                            mediaCounter ++
                        }
                    }
                    else -> {}
                }
            }
            '\n' ->{
                when(state){
                    InTitle -> {
                        maker.addStyle(Title,titleCounter,templeStartIndex,i)
                    }
                    else -> {}
                }
                state = NextStart
                titleCounter = 0
                templeStartIndex = 0
            }
            else ->{}//其余正常字符不做处理
        }
    }
}

class AnnotatedStringMaker(val text: String){
    fun addStyle(style: TextStyle,extra: Int = 0,start:Int,end: Int) {
        val builder = AnnotatedString.Builder(text)
        //builder.addStyle()
    }
}

sealed class RichText(){
    object Text : RichText()
    object Media: RichText()
}

enum class WordParserState{
    NextStart,//新行初始
    Normal,//正常文本
    InMedia,
    OutMedia,
    DoubtMedia,
    DoubtTitle,//预备标题
    InTitle,
    IsVideo,
}
enum class TextStyle {
    Title,
    Text,
    Image,
    Video,
}
/*比较复杂，暂时不做
    Italics,
    Bold,
    CodeBlock,
    Table,
    List,
    DivideLine,
    UnderLine,
    DeleteLine,
    URL,
    Task,
    */

//词法分析器暂时搁置不做

//!![img]Image_URL!!
//!![video]Video_URL!!