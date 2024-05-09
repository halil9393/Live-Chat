package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.livechat.screens.ChatListScreen
import com.example.livechat.screens.LoginScreen
import com.example.livechat.screens.ProfileScreen
import com.example.livechat.screens.SignUpScreen
import com.example.livechat.screens.SingleChatScreen
import com.example.livechat.screens.SingleStatusScreen
import com.example.livechat.screens.StatusScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route:String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}"){
        fun createRoute(id:String) = "singleChat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}"){
        fun createRoute(userId:String) = "singleStatus/$userId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation(){
        val navController = rememberNavController()
        val vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
            composable(DestinationScreen.SignUp.route){
                SignUpScreen(navController,vm)
            }
            composable(DestinationScreen.Login.route){
                LoginScreen(vm,navController)
            }
            composable(DestinationScreen.ChatList.route){
                ChatListScreen(vm,navController)
            }
            composable(DestinationScreen.SingleChat.route){
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(vm,navController,it)
                }

            }
            composable(DestinationScreen.StatusList.route){
                StatusScreen(vm,navController)
            }
            composable(DestinationScreen.Profile.route){
                ProfileScreen(vm,navController)
            }
            composable(DestinationScreen.SingleStatus.route){
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(vm,navController, it )
                }

            }
        }

    }


}
