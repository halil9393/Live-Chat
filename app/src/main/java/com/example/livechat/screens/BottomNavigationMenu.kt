package com.example.livechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.livechat.DestinationScreen
import com.example.livechat.R
import com.example.livechat.navigateTo

enum class BottomNavItem(val icon: Int, val label: String, val navDestination: DestinationScreen) {
    CHATLIST(R.drawable.chat, "Chats", DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, "Statuses", DestinationScreen.StatusList),
    PROFILE(R.drawable.user, "Profile", DestinationScreen.Profile)
}

@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavItem,
    navController: NavController
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(color = MaterialTheme.colorScheme.primary)

    ) {

        for (item in BottomNavItem.values()) {

            Image(
                painter = painterResource(id = item.icon),
                contentDescription = item.label,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, item.navDestination.route)
                    },
                colorFilter = if (item == selectedItem) androidx.compose.ui.graphics.ColorFilter.tint(
                    color = Color.Black
                ) else androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray)
            )
        }
    }

}


@Composable
fun BottomNavigationBar(selectedItem: BottomNavItem, navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.padding(4.dp),
                selected = currentRoute == item.navDestination.route,
                onClick = {
                    navigateTo(navController, item.navDestination.route)
                },
                icon = {

                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                            .weight(1f),
                        colorFilter = if (item == selectedItem)
                            androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                        else androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray)
                    )

                },
                label = {
                    Text(
                        text = item.label,
                        color = if (item == selectedItem)
                            Color.Black
                        else Color.Gray
                    )
                }
            )
        }
    }
}