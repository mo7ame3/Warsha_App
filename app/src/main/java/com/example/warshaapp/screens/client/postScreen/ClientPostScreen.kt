package com.example.warshaapp.screens.client.postScreen

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.components.BackPressHandler
import com.example.warshaapp.components.ButtonDefault
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.DropList
import com.example.warshaapp.components.PickPhoto
import com.example.warshaapp.components.TextInput
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.createOrder.CreateOrder
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "Recycle")
@Composable
fun ClientPostScreen(
    navController: NavController,
    craftId: String,
    craftName: String,
    clientPostViewModel: ClientPostViewModel
) {
    BackPressHandler {
        navController.navigate(AllScreens.ClientHomeScreen.name + "/home") {
            navController.popBackStack()
            navController.popBackStack()
        }
    }

    Scaffold(topBar = {
        TopBar(title = "البحث عن $craftName") {
            navController.navigate(AllScreens.ClientHomeScreen.name + "/home") {
                navController.popBackStack()
                navController.popBackStack()
            }
        }
    }) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val problemTitle = remember {
            mutableStateOf("")
        }
        val problemDescription = remember {
            mutableStateOf("")
        }
        val problemType = remember {
            mutableStateOf("")
        }
        val expanded = remember {
            mutableStateOf(false)
        }
        var selectedImage by remember {
            mutableStateOf<Uri?>(null)
        }
        var loading by remember {
            mutableStateOf(false)
        }
        val problemTypeList = listOf("صيانة بسيطة", "صيانة متوسطة", "صيانة معقدة")
        val keyboardController = LocalSoftwareKeyboardController.current
        // valid
        val valid = (problemTitle.value.trim().isNotBlank()
                && problemDescription.value.trim().isNotBlank()
                && problemType.value.trim().isNotBlank()
                && selectedImage.toString().isNotEmpty())


        if (!loading) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                TextInput(
                    input = problemTitle,
                    isNotBackground = true,
                    label = "عنوان المشروع",
                    onAction = KeyboardActions {
                        keyboardController?.hide()
                    })
                Spacer(modifier = Modifier.height(5.dp))
                DropList(
                    value = problemType,
                    expanded = expanded,
                    isNotBackground = true,
                    list = problemTypeList,
                    label = "نوع المشكلة",
                    modifier = Modifier.height(160.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "تفاصيل المشكلة",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    color = MainColor
                )
                Spacer(modifier = Modifier.height(5.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(start = 25.dp, end = 25.dp),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(width = 1.dp, color = MainColor)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TextInput(
                            input = problemDescription,
                            isBorder = false,
                            label = if (problemDescription.value.isEmpty()) "اكتب تفاصيل المشكلة هنا..." else "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp),
                            isSingleLine = false,
                            isNotBackground = true,
                        )
                    }
                }

                //Add photo
                Spacer(modifier = Modifier.height(5.dp))
                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                        selectedImage = uri
                    }
                Text(
                    text = "اضف صور",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    color = MainColor
                )
                Spacer(modifier = Modifier.height(5.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(start = 25.dp, end = 25.dp)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(width = 1.dp, color = MainColor)
                ) {
                    //change photo
                    PickPhoto(selectedImage)
                }
                //Ellipse

                ButtonDefault(label = "ارسل", enabled = valid) {
                    if (selectedImage != null) {
                        val imageName = selectedImage?.toString()?.let { it1 -> File(it1) }
                        val fileUri: Uri =
                            selectedImage!! // get the file URI from the file picker result
                        val inputStream = context.contentResolver.openInputStream(fileUri)
                        val file = inputStream?.readBytes()
                            ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val filePart = file?.let {
                            MultipartBody.Part.createFormData(
                                "image", imageName!!.name, it
                            )
                        }
                        val problemTitlePart = problemTitle.value
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val problemTypePart = problemType.value
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val problemDescriptionPart = problemDescription.value
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        scope.launch {
                            loading = true
                            if (Constant.token.isNotEmpty()) {
                                val response: WrapperClass<CreateOrder, Boolean, Exception> =
                                    clientPostViewModel.createOrder(
                                        token = Constant.token,
                                        craftId = craftId,
                                        image = filePart!!,
                                        description = problemDescriptionPart,
                                        title = problemTitlePart,
                                        orderDifficulty = problemTypePart
                                    )
                                when (response.data?.status) {
                                    "success" -> {
                                        navController.navigate(AllScreens.ClientHomeScreen.name + "/home") {
                                            navController.popBackStack()
                                            navController.popBackStack()
                                        }
                                    }

                                    "error" -> {
                                        loading = false
                                        Toast.makeText(
                                            context,
                                            "${response.data?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {
                                        loading = false
                                        Toast.makeText(
                                            context,
                                            "خطأ في الانترنت",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "لم تقم بإضافة صورة", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            CircleProgress()
        }


    }
}