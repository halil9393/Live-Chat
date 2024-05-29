@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.livechat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.livechat.screens.CommonImage
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun CommonProgressBar() {
    Row(modifier = Modifier
        .background(Color.LightGray.copy(0.5f))
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommonSubProgressBar(){
    Box(modifier = Modifier.fillMaxSize()){
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun CheckSignedIn(vm: LCViewModel, navController: NavController) {
    val alreadySignIn = remember { mutableStateOf(false) }
    val singIn = vm.signIn.value
    if (singIn && !alreadySignIn.value) {
        alreadySignIn.value = true
        navController.navigate(DestinationScreen.ChatList.route) {
            popUpTo(0)
        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CommonRow(
    imageUrl: String?,
    title: String?,
    subTitle: String?,
    timestamp: String?,
    onItemClick: () -> Unit
) {

    val instant = Instant.ofEpochMilli(timestamp?.toLong() ?: 0)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val currentDateTime = LocalDateTime.now()
    val diff = ChronoUnit.DAYS.between(localDateTime.toLocalDate(), currentDateTime.toLocalDate())
    val formattedTime = when {
        diff == 0L -> { // Bugün
            localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        diff == 1L -> { // Dün
            "Dün"
        }

        else -> { // Diğer günler
            localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { onItemClick.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = title ?: "---",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formattedTime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = subTitle ?: "---",
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
fun CommonTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean,
    showSearchBar: Boolean,
    searchText: String,
    closeSearchBar: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    navigateUp: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    if (showSearchBar) {

        val focusManager = LocalFocusManager.current

        val textFieldFocusRequester = remember { FocusRequester() }

        SideEffect {
            textFieldFocusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(64.dp)
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                backgroundColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = closeSearchBar) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                    BasicTextField(
                        value = searchText,
                        onValueChange = {
                            onSearchTextChanged(it)
                        },
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth()
                            .focusRequester(textFieldFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ))
                }
            }
        }


    } else if (canNavigateBack) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 22.sp
                )
            },
            actions = { actions() },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 22.sp
                )
            },
            actions = { actions() },
            modifier = modifier
        )
    }
}
