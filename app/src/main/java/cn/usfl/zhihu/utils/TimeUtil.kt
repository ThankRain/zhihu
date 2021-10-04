package cn.usfl.zhihu.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *  author : ChenWenJie
 * email  :1181620038@qq.com
 *  date   : 2020/7/28
 *  desc   : 时间相关工具类
 */
fun Long.asTime(): String {
    val startDate = this
    val endTime = System.currentTimeMillis()     //获取毫秒数
    val timeDifference = endTime - startDate;
    val second = timeDifference / 1000;    //计算秒
    if (second < 60) {
        return "$second 秒前" //根据需要可以写成刚刚。
    } else {
        val minute = second / 60
        if (minute < 60) {
            return "$minute 分钟前"
        } else {
            val hour = minute / 60
            if (hour < 24) {
                return "$hour 小时前"
            } else {
                val day = hour / 24
                if (day < 30) {
                    return "$day 天前"
                } else {
                    if (day / 30 < 12) {
                        val formatter = SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)
                        return formatter.format(startDate)
                    } else {
                        val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA)
                        return formatter.format(startDate)
                    }
                }

            }
        }
    }

}
