@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.warshaapp.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.warshaapp.R
import com.example.warshaapp.model.shared.authentication.Craft
import com.example.warshaapp.ui.theme.MainColor
import com.example.warshaapp.ui.theme.SecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 25.dp, end = 25.dp),
    input: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Email,
    isError: MutableState<Boolean>? = null,
    label: String,
    isBorder: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,
    isSingleLine: Boolean = true,
    leadingImageVector: ImageVector? = null,
    isNotBackground: Boolean = false,
) {

    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        label = { Text(text = label) },
        value = input.value,
        onValueChange = {
            if (label == "البريد الالكتروني") {
                val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
                input.value = it
                isError?.value = !emailRegex.toRegex().matches(it)
            } else if (label == "الاسم") {
                val nameRegex = "^[a-zA-Zأ-ي]+(([',. -][a-zA-Zأ-ي])?[a-zA-Zأ-ي]*)*$"
                input.value = it
                isError?.value = !nameRegex.toRegex().matches(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = onAction,
        colors = if (label == "البريد الالكتروني") {
            TextFieldDefaults.outlinedTextFieldColors(
                containerColor = if (!isNotBackground) {
                    SecondaryColor.copy(
                        alpha = 0.2f
                    )
                } else Color.White,
            )
        } else {
            TextFieldDefaults.outlinedTextFieldColors(
                containerColor = if (!isNotBackground) {
                    SecondaryColor.copy(
                        alpha = 0.2f
                    )
                } else Color.White,
                disabledBorderColor = Color.Transparent,
                focusedBorderColor = if (isBorder) {
                    if (isNotBackground) Color.Transparent else MainColor
                } else Color.White,
                unfocusedBorderColor = if (isBorder) {
                    if (isNotBackground) Color.Transparent else MainColor
                } else
                    Color.White
            )
        },
        singleLine = isSingleLine,
        leadingIcon = {
            if (leadingImageVector != null) {
                Icon(imageVector = leadingImageVector, contentDescription = null, tint = MainColor)
            }
        },
        isError = isError!!.value,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropInput(
    modifier: Modifier,
    text: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text,
    expanded: MutableState<Boolean>,
    readOnly: Boolean = false,
    label: String,
    isNotBackground: Boolean = false,
    isBorder: Boolean = true,
    isSingleLine: Boolean = true
) {
    OutlinedTextField(
        readOnly = readOnly,
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        label = { Text(text = label, style = TextStyle(color = MainColor)) },
        value = text.value,
        onValueChange = {},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = KeyboardActions {
//            expanded.value = !expanded.value
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = if (!isNotBackground) {
                SecondaryColor.copy(
                    alpha = 0.2f
                )
            } else Color.White,
            disabledBorderColor = Color.Transparent,
            focusedBorderColor = if (isBorder) {
                if (isNotBackground) Color.Transparent else MainColor
            } else Color.White,
            unfocusedBorderColor = if (isBorder) {
                if (isNotBackground) Color.Transparent else MainColor
            } else
                Color.White
        ),
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
        },
        singleLine = isSingleLine,

        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Password,
    isSingleLine: Boolean = true,
    isError: MutableState<Boolean>,
    eye: MutableState<Boolean>,
    onButtonAction: () -> Unit,
    onAction: KeyboardActions = KeyboardActions.Default,
    label: String = "كلمة السر",
    leadingImageVector: ImageVector? = null,
    isNotBackground: Boolean = false,
) {
    val visualTransformation = if (eye.value) VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
        singleLine = isSingleLine,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        shape = RoundedCornerShape(25.dp),
        label = { Text(text = label) },
        value = password.value,
        onValueChange = {
            if (it.isNotBlank()) {
                if (it.length > 11) {
                    password.value = it
                    isError.value = false
                } else {
                    password.value = it
                    isError.value = true
                }
            } else {
                password.value = ""
                isError.value = true
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = if (!isNotBackground) {
                SecondaryColor.copy(
                    alpha = 0.2f
                )
            } else Color.White,
            disabledBorderColor = MainColor,
            focusedBorderColor = MainColor,
            unfocusedBorderColor = MainColor
        ),
        keyboardActions = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = { onButtonAction.invoke() }) {
                if (eye.value) Icon(
                    painter = painterResource(id = R.drawable.visibilityoff),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                else Icon(
                    painter = painterResource(id = R.drawable.visibility),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        leadingIcon = {
            if (leadingImageVector != null) {
                Icon(imageVector = leadingImageVector, contentDescription = null, tint = MainColor)
            }
        }, isError = isError.value
    )
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropList(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    value: MutableState<String>,
    craftId: MutableState<String>? = null,
    label: String = "العنوان",
    list: List<String>? = null,
    craftList: List<Craft>? = null,
    isNotBackground: Boolean = false
) {

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }) {
        DropInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .menuAnchor(),
            text = value,
            expanded = expanded,
            readOnly = true,
            label = label,
            isNotBackground = isNotBackground
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 25.dp, end = 50.dp)
        ) {
            ExposedDropdownMenu(
                expanded = expanded.value,
                modifier = modifier,
                onDismissRequest = {
                    expanded.value = false
                },
            ) {
                if (list != null && craftList == null) {
                    list.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = item)
                            },
                            onClick = {
                                value.value = item
                                expanded.value = false
                            },
                        )
                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                } else if (craftList != null && list == null) {
                    craftList.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = item.name)
                            },
                            onClick = {
                                value.value = item.name
                                craftId?.value = item.id
                                expanded.value = false
                            },
                        )
                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}







