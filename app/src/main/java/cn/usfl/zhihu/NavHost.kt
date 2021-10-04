package cn.usfl.zhihu

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cn.usfl.zhihu.Route
import cn.usfl.zhihu.screen.*
import cn.usfl.zhihu.viewmodel.MainViewModel


@Composable
fun AppNavHost(navController: NavHostController, vm: MainViewModel) {
    NavHost(navController = navController, startDestination = Route.createAsk) {
        composable(Route.login) {
            LoginScreen(navController, vm)
        }
        composable(Route.createAsk) {
            CreateAskScreen(navController, vm)
        }
        composable(Route.main){

        }
        composable(Route.asks){
            AsksScreen(navController = navController, vm = vm)
        }
        composable("${Route.ask}/{askId}",
            arguments = listOf(navArgument("askId") { type = NavType.IntType })){
            AskScreen(navController = navController, vm = vm, askId = it.arguments?.getInt("askId")?:0)
        }
        composable("${Route.answer}/{answerId}",
            arguments = listOf(navArgument("answerId") { type = NavType.IntType })){

        }
        composable("${Route.createAnswer}/{askId}",
            arguments = listOf(navArgument("askId") { type = NavType.IntType })){
            CreateAnswerScreen(askId = it.arguments?.getInt("askId")?:0,
                navController = navController,
                vm = vm
            )
        }
    }
}

object Route{
    const val login = "login"//注册&登录
    const val createAsk = "create_ask"
    const val createAnswer = "create_answer"
    const val ask = "ask"//查看Ask及对应所有Answers
    const val answer = "answer"//查看单个Answer
    const val asks = "asks"//查看所有问题
    const val main = "main"//主页面
}