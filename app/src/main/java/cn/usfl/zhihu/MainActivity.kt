package cn.usfl.zhihu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import cn.usfl.zhihu.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var vm: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            vm = viewModel()
            vm.setContexts(this)
            AppNavHost(navController = navController,vm)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}