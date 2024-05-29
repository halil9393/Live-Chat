@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.livechat.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.livechat.CommonProgressBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.navigateTo


@Composable
fun ProfileScreen(vm: LCViewModel, navController: NavController) {


    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {

        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }

        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = BottomNavItem.PROFILE,
                    navController = navController
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    ProfileContent(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp),
                        vm = vm,
                        name = name,
                        number = number,
                        onNameChange = { name = it },
                        onNumberChange = { number = it },
                        onSave = {
                            vm.createOrUpdateProfile(
                                name = name, number = number
                            )
                        },
                        onBack = {
                            navigateTo(
                                navController = navController,
                                route = DestinationScreen.ChatList.route
                            )
                        },
                        onLogout = {
                            vm.logout()
                            navigateTo(
                                navController = navController,
                                route = DestinationScreen.Login.route
                            )
                        }
                    )
                    Button(
                        onClick = {
                            navigateTo(navController, DestinationScreen.Settings.route)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Settings")

                    }

                }
            })

    }
}

@Composable
fun CommonDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )

}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberImagePainter(data = data)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}


@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogout: () -> Unit
) {

    val imageUrl = vm.userData.value?.imageUrl

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", Modifier.clickable {
                onSave.invoke()
            })


        }

        CommonDivider()
        ProfileImage(imageUrl = imageUrl, vm = vm)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Name ", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedSupportingTextColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Number ", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.textFieldColors(
                    focusedSupportingTextColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable {
                onLogout.invoke()
            })
        }
    }

}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                vm.uploadProfileImage(uri)
            }
        }

    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(200.dp)
            ) {

                CommonImage(data = imageUrl)
            }

            Text(text = "Change Profile Picture")
        }

        if (vm.inProcess.value) {
            CommonProgressBar()
        }
    }
}