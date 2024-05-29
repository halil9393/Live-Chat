@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.example.livechat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.livechat.CommonProgressBar
import com.example.livechat.CommonRow
import com.example.livechat.CommonSubProgressBar
import com.example.livechat.CommonTopAppBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.navigateTo

@Composable
fun ChatListScreen(vm: LCViewModel, navController: NavController) {

    val inProgress = vm.inProcessChats
    if (inProgress.value) {
        CommonProgressBar()
    } else {

        val chats by vm.searchedChatList.collectAsState()
        val searchText by vm.searchText.collectAsState()
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = {
            showDialog.value = true
        }
        val onDismiss: () -> Unit = {
            showDialog.value = false
        }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }

        var showMenu by remember { mutableStateOf(false) }
        var showSearchBar by remember { mutableStateOf(false) }


        Scaffold(
            topBar = {
                CommonTopAppBar(
                    title = "LiveChat",
                    canNavigateBack = false,
                    showSearchBar = showSearchBar,
                    searchText = searchText,
                    closeSearchBar = {
                        showSearchBar = false
                        vm.onSearchTextChanged("")
                    },
                    onSearchTextChanged = {
                        vm.onSearchTextChanged(it)
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera"
                            )
                        }
                        IconButton(onClick = { showSearchBar = !showSearchBar }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More",
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Refresh")
                                },
                                onClick = { /* TODO */ },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("Settings")
                                },
                                onClick = { /* TODO */ },
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = BottomNavItem.CHATLIST,
                    navController = navController
                )
            },
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    if (vm.inSubProcessChats.value) {
                        CommonSubProgressBar()
                    } else if (chats.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(text = "No chats available")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(chats) { chat ->
                                val chatUser = if (chat.user1.userId == userData?.userId) {
                                    chat.user2
                                } else {
                                    chat.user1
                                }

                                val lastMessage = chat.lastMessage

                                CommonRow(
                                    imageUrl = chatUser.imageUrl,
                                    title = chatUser.name,
                                    subTitle = lastMessage?.message,
                                    timestamp = lastMessage?.timestamp.toString()
                                ) {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            DestinationScreen.SingleChat.createRoute(id = it)
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        )
    }


}

@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
        }, confirmButton = {
            Button(onClick = { onAddChat(addChatNumber.value) }) {
                Text(text = "Add Chat")
            }
        }, title = {
            Text(text = "Add Chat")
        }, text = {
            OutlinedTextField(
                value = addChatNumber.value,
                onValueChange = {
                    addChatNumber.value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        })


    }

    FloatingActionButton(
        onClick = { onFabClick.invoke() },
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }
}

