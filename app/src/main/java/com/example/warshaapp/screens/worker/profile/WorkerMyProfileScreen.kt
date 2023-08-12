package com.example.warshaapp.screens.worker.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.R
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.EmptyColumn
import com.example.warshaapp.components.GetSmallPhoto
import com.example.warshaapp.components.LoginButton
import com.example.warshaapp.components.ProfilePhoto
import com.example.warshaapp.components.StarsNumber
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.model.shared.user.User
import com.example.warshaapp.model.worker.getMyOffer.Data
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.GrayColor
import com.example.warshaapp.ui.theme.MainColor
import com.example.warshaapp.ui.theme.RedColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@ExperimentalMaterial3Api
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "Recycle", "StateFlowValueCalledInComposition"
)
@Composable
fun WorkerMyProfileScreen(
    navController: NavController,
    workerProfileViewModel: WorkerProfileViewModel
) {
    //Shared preference variables
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val userId = sharedPreference.getUserId.collectAsState(initial = "")

    //coroutineScope
    val scope = rememberCoroutineScope()

    //state flow list
    val getProfileUser = MutableStateFlow<List<User>>(emptyList())

    val completeToggle = remember {
        mutableStateOf(true)
    }
    val picturesList = remember {
        mutableStateListOf<Uri?>(null)
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uriPhoto ->
            picturesList.add(uriPhoto)
        }

    var loading by remember {
        mutableStateOf(true)
    }
    var changeProfilePhoto by remember {
        mutableStateOf(false)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    var loading2 by remember {
        mutableStateOf(true)
    }
    val myOfferData = remember {
        mutableStateListOf<Data>()
    }
    val length = remember {
        mutableStateOf(0)
    }

    if (userId.value.toString().isNotEmpty() && Constant.token.isNotEmpty()) {
        val getProfile: WrapperClass<GetProfile, Boolean, Exception> =
            produceState<WrapperClass<GetProfile, Boolean, Exception>>(
                initialValue = WrapperClass(data = null)
            ) {
                value = workerProfileViewModel.getProfile(
                    authorization = Constant.token,
                    userId = userId.value.toString()
                )
            }.value
        if (getProfile.data?.status == "fail" || getProfile.data?.status == "error" || getProfile.e != null) {
            exception = true
            Toast.makeText(
                context,
                "خطأ في الانترنت",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (getProfile.data != null) {
                scope.launch {
                    getProfileUser.emit(getProfile.data!!.data?.user!!)
                    loading = false
                    exception = false

                    val response: WrapperClass<GetMyOffer, Boolean, Exception> =
                        workerProfileViewModel.getMyCompletedOffer(
                            authorization = Constant.token,
                            userId = userId.value.toString()
                        )
                    if (response.data?.status == "success") {
                        if (!response.data!!.data.isNullOrEmpty()) {
                            response.data!!.data?.forEach {
                                length.value = length.value + 1
                                if (it.status == "completed") {
                                    myOfferData.add(it)
                                    if (length.value == response.data!!.data?.size) {
                                        loading2 = false
                                    }
                                } else {
                                    if (length.value == response.data!!.data?.size) {
                                        loading2 = false
                                    }
                                }
                            }
                        } else {
                            loading2 = false
                        }
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            if (changeProfilePhoto) {
                TextButton(onClick = {
                    val imageName = uri?.toString()?.let { it1 -> File(it1) }
                    val fileUri: Uri =
                        uri!! // get the file URI from the file picker result
                    val inputStream = context.contentResolver.openInputStream(fileUri)
                    val file = inputStream?.readBytes()
                        ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val filePart = file?.let {
                        MultipartBody.Part.createFormData(
                            "image", imageName!!.name, it
                        )
                    }
                    scope.launch {
                        loading = true
                        val response: WrapperClass<UpdateProfile, Boolean, Exception> =
                            workerProfileViewModel.updateProfilePhoto(
                                authorization = Constant.token,
                                image = filePart!!,
                                userId = userId.value.toString()
                            )
                        when (response.data?.status) {
                            "success" -> {
                                val newResponse: WrapperClass<GetProfile, Boolean, Exception> =
                                    workerProfileViewModel.getProfile(
                                        userId = userId.value.toString(),
                                        authorization = Constant.token
                                    )
                                if (newResponse.data?.status == null) {
                                    getProfileUser.emit(newResponse.data!!.data?.user!!)
                                    uri = null
                                    loading = false
                                    changeProfilePhoto = false
                                }
                            }

                            "error" -> {
                                uri = null
                                changeProfilePhoto = false
                                loading = false
                                Toast.makeText(
                                    context,
                                    "${response.data?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                uri = null
                                loading = false
                                changeProfilePhoto = false
                                Toast.makeText(
                                    context,
                                    "خطأ في الانترنت",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }) {
                    Text(
                        text = "تحديث", style = TextStyle(
                            color = MainColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    )
                }
            } else {
                Row {

                }
            }
            TopBar(title = "", isProfile = true) {
                navController.popBackStack()
            }
        }
    }) {
        if (!loading && !exception) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val launcher1 =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { URI ->
                            uri = URI
                        }
                    if (uri == null) {
                        GetSmallPhoto(
                            isProfile = true,
                            uri = if (getProfileUser.value[0].avatar != null) getProfileUser.value[0].avatar.toString() else null
                        )
                    } else {
                        ProfilePhoto(uri = uri)
                        changeProfilePhoto = true
                    }
                    IconButton(onClick = {
                        //nav to edit photo
                        launcher1.launch("image/*")
                    }, modifier = Modifier.padding(top = 85.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = null, tint = MainColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = getProfileUser.value[0].name,
                    style = TextStyle(
                        color = MainColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = getProfileUser.value[0].address, style = TextStyle(
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = if (getProfileUser.value[0].bio != null) getProfileUser.value[0].bio.toString() else "",
                    style = TextStyle(
                        color = RedColor,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                //Stars
                StarsNumber(if (getProfileUser.value[0].rate!! > 0) getProfileUser.value[0].rate!!.toInt() else 0)
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LoginButton(isLogin = completeToggle.value, label = "مشاريع مكتملة") {
                        completeToggle.value = true
                    }
                    LoginButton(isLogin = !completeToggle.value, label = "معرض اعمالي") {
                        completeToggle.value = false
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (completeToggle.value) {
                    //Complete
                    if (!loading2) {
                        if (!myOfferData.isEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            ) {
                                items(myOfferData) {
                                    CompleteMyProjectRow(it)
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        } else {
                            EmptyColumn(text = "لا يوجد مشاريع مكتملة")
                        }
                    } else {
                        CircleProgress()
                    }
                } else {
                    //pictures
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                    ) {
                        items(picturesList) {
                            if (it != null) {
                                PickMyPhotoRow(it)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
                if (!completeToggle.value) {
                    LoginButton(
                        isLogin = true,
                        label = "أضف صور لمعرض أعمالي",
                        modifier = Modifier.width(200.dp)
                    ) {
                        launcher.launch("image/*")
                    }
                }
            }
        } else if (loading && !exception) {
            CircleProgress()
        } else if (exception) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    exception = false
                    loading = true
                    scope.launch {
                        val getProfile: WrapperClass<GetProfile, Boolean, Exception> =
                            workerProfileViewModel.getProfile(
                                userId = userId.value.toString(),
                                authorization = Constant.token
                            )
                        if (getProfile.data?.status == "fail" || getProfile.data?.status == "error" || getProfile.e != null) {
                            exception = true
                            Toast.makeText(
                                context,
                                "خطأ في الانترنت",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (getProfile.data != null) {
                                scope.launch {
                                    getProfileUser.emit(getProfile.data!!.data?.user!!)
                                    loading = false
                                    exception = false
                                }
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh, contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }

}


@Composable
fun CompleteMyProjectRow(
    item: Data
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // photo url from data base workers photos
            GetSmallPhoto(uri = if (item.order.user.avatar != null) item.order.user.avatar.toString() else null)
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(
                    text = item.order.user.name, style = TextStyle(
                        color = MainColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = item.order.title, style = TextStyle(
                        color = MainColor,
                        fontSize = 15.sp,
                    )
                )
                Text(
                    text = item.order.orderDifficulty, style = TextStyle(
                        color = GrayColor,
                        fontSize = 12.sp,
                    )
                )

            }
        }
    }
}

@Composable
fun PickMyPhotoRow(
    selectedImage: Uri? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(start = 25.dp, end = 25.dp),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(width = 1.dp, color = MainColor)
    ) {
        //change photo
        PickMyPhotoRow(selectedImage)
    }
}


