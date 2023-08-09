package com.example.warshaapp.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.warshaapp.R
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.DrawerData
import com.example.warshaapp.data.MyCraftOrderData
import com.example.warshaapp.ui.theme.GoldColor
import com.example.warshaapp.ui.theme.GrayColor
import com.example.warshaapp.ui.theme.MainColor
import com.example.warshaapp.ui.theme.SecondaryColor

@Composable
fun DrawerHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(bottomEnd = 300.dp, bottomStart = 160.dp),
        color = MainColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = Constant.logo),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = Constant.title, style = TextStyle(
                    color = Color.White, fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun DrawerBody(
    isClient: Boolean,
    name: String,
    onClick: (DrawerData) -> Unit,
) {
    val drawerListClient = if (isClient) listOf(
        DrawerData(title = name, pic = R.drawable.person),
        DrawerData(title = "إعدادات حسابي", pic = R.drawable.settings),
        DrawerData(title = "طلباتي", pic = R.drawable.my_orders),
        DrawerData(title = "مكتملة", pic = R.drawable.completed_orders),
        DrawerData(title = "تسجيل الخروج", pic = R.drawable.logout),
    )
    else listOf(
        DrawerData(title = name, pic = R.drawable.person),
        DrawerData(title = "إعدادات حسابي", pic = R.drawable.settings),
        DrawerData(title = "مشاريعي", pic = R.drawable.my_orders),
        DrawerData(title = "تسجيل الخروج", pic = R.drawable.logout),
    )

    LazyColumn {
        items(drawerListClient) {
            DrawerRow(drawerData = it) { select ->
                onClick.invoke(select)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun DrawerRow(
    drawerData: DrawerData,
    onClick: (DrawerData) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(start = 20.dp, end = 20.dp)
        .clickable {
            onClick.invoke(drawerData)
        }) {
        Icon(
            painter = painterResource(id = drawerData.pic),
            contentDescription = null,
            tint = MainColor,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = drawerData.title, style = TextStyle(
                fontSize = 20.sp, color = MainColor
            )
        )
    }
}

@Composable
fun TopMainBar(
    title: MutableState<String>, onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 15.dp, end = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MainColor
                )
            }
            Text(
                text = title.value, style = TextStyle(
                    fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = MainColor
                )
            )
            Icon(
                painter = painterResource(id = Constant.logo),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = MainColor
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun TopBar(
    title: String,
    isProfile: Boolean = false,
    onAction: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = if (isProfile) Modifier.padding(end = 10.dp) else Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {

        Row {}
        Text(text = title, style = TextStyle(color = MainColor, fontSize = 18.sp))
        IconButton(onClick = onAction) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = MainColor)
        }
    }
}


@Composable
fun BottomBar(
    selected: MutableState<String>,
    title: MutableState<String> = mutableStateOf(""),
    home: () -> Unit = {},
    order: () -> Unit = {},
    chat: () -> Unit = {},
    isClient: Boolean = true

) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
        contentColor = MainColor
    ) {
        NavigationBar(containerColor = MainColor) {

            NavigationBarItem(selected = selected.value == "chat", onClick = {
                selected.value = "chat"
                title.value = "المحادثات"
                chat.invoke()
            }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = MainColor,
                unselectedIconColor = SecondaryColor
            )
            )

            NavigationBarItem(selected = selected.value == "home", onClick = {
                selected.value = "home"
                title.value = Constant.title
                home.invoke()
            }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home_icon_white),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = MainColor,
                unselectedIconColor = SecondaryColor
            )
            )

            NavigationBarItem(selected = selected.value == "order", onClick = {
                selected.value = "order"
                title.value = if (isClient) "طلباتي" else "مشاريعي"
                order.invoke()
            }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.my_orders),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = MainColor,
                unselectedIconColor = SecondaryColor
            )
            )
        }
    }
}

@Composable
fun InternetCraftPhoto(uri: String) {
    Box {

        // to reload image
        var refreshImage by remember { mutableStateOf(0) }
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current).data(uri)
                .setParameter("refresh", refreshImage, memoryCacheKey = null).build()
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
        )
        // condition to reload image
        when (painter.state) {
            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { refreshImage++ }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {}
        }
    }
}

//craft name
@Composable
fun InternetCraftName(jobName: String) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .height(90.dp)
            .width(350.dp),
        shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp),
        tonalElevation = 5.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = jobName)
        }
    }
}

@Composable
fun CircleProgress() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MainColor)
    }
}

@Composable
fun ButtonDefault(
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
fun PickPhoto(
    selectImage: Uri? = null, isProfile: Boolean = false
) {
    if (selectImage != null) {
        Image(
            painter = rememberAsyncImagePainter(selectImage),
            contentDescription = null,
            modifier = if (isProfile) Modifier.clip(CircleShape) else Modifier.clip(
                RoundedCornerShape(25.dp)
            ),
            contentScale = ContentScale.Crop
        )
    } else {
        Icon(
            imageVector = if (!isProfile) Icons.Default.KeyboardArrowUp else Icons.Default.Person,
            contentDescription = null,
            tint = if (!isProfile) Color.Gray.copy(alpha = 0.2f) else Color.White
        )
    }
}

@Composable
fun ProblemDescription(
    problemDescription: String, modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(width = 1.dp, color = MainColor)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp, top = 10.dp)
        ) {
            item {
                Text(
                    text = problemDescription, style = TextStyle(
                        color = MainColor, fontSize = 20.sp
                    )
                )
            }
        }
    }
}


@Composable
fun GetSmallPhoto(uri: String? = null, isProfile: Boolean = false) {
    Surface(
        shape = CircleShape,
        color = MainColor,
        modifier = if (isProfile) Modifier.size(120.dp) else Modifier.size(50.dp)
    ) {
        if (uri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = null,
                modifier = Modifier.size(300.dp, 200.dp),
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White
            )
        }
    }

}

@Composable
fun ProfilePhoto(uri: Uri? = null) {
    Surface(
        shape = CircleShape, color = MainColor, modifier = Modifier.size(120.dp)
    ) {
        PickPhoto(uri, isProfile = true)
    }
}

@Composable
fun StarsNumber(stars: Int) {
    Row {
        if (stars >= 1) {
            Icon(
                imageVector = Icons.Default.Star, contentDescription = null, tint = GoldColor
            )
        }
        if (stars >= 2) {
            Icon(
                imageVector = Icons.Default.Star, contentDescription = null, tint = GoldColor
            )
        }
        if (stars >= 3) {
            Icon(
                imageVector = Icons.Default.Star, contentDescription = null, tint = GoldColor
            )
        }
        if (stars >= 4) {
            Icon(
                imageVector = Icons.Default.Star, contentDescription = null, tint = GoldColor
            )
        }
        if (stars == 5) {
            Icon(
                imageVector = Icons.Default.Star, contentDescription = null, tint = GoldColor
            )
        }
    }
}


@Composable
fun SmallPhoto(uri: Uri? = null) {
    Surface(
        shape = CircleShape, color = MainColor, modifier = Modifier.size(50.dp)
    ) {
        PickPhoto(isProfile = true, selectImage = uri)
    }
}

@Composable
fun ClientCompleteProjectRow(
    item: MyCraftOrderData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // photo url from data base workers photos
            SmallPhoto()
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(
                    text = item.workerName.toString(), style = TextStyle(
                        color = MainColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = item.problemTitle + "- " + item.problemType, style = TextStyle(
                        color = MainColor,
                        fontSize = 12.sp,
                    )
                )
                StarsNumber(stars = item.workerRate!!)

            }
        }
    }
}


@Composable
fun EmptyColumn(
    text: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = text, style = TextStyle(
                    color = MainColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
        }
    }
}
