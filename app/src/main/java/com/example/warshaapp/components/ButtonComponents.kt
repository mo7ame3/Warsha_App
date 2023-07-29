package com.example.warshaapp.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.warshaapp.ui.theme.GrayColor
import com.example.warshaapp.ui.theme.MainColor


@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    isLogin: Boolean,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit

) {
    Button(
        onClick = {
            if (enabled) {
                onClick.invoke()
            }
        },
        shape = CircleShape,
        modifier = modifier.width(130.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) {
                if (isLogin) MainColor
                else Color.White
            } else {
                GrayColor.copy(alpha = 0.7f)
            }
        )
    ) {
        Text(
            text = label, style = TextStyle(
                if (isLogin) Color.White
                else MainColor
            )
        )
    }
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit

) {
    Button(
        onClick = {
            if (enabled) {
                onClick.invoke()
            }
        },
        shape = CircleShape,
        modifier = modifier.width(130.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MainColor else GrayColor
        )
    ) {
        Text(
            text = label, style = TextStyle(
                Color.White
            )
        )
    }
}


@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}