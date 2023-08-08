package com.example.warshaapp.constant

import android.annotation.SuppressLint
import com.example.warshaapp.R
import com.example.warshaapp.data.MyCraftOrderData

object Constant {
    const val title = "ورشة"

    @SuppressLint("NonConstantResourceId")
    val logo = R.drawable.home

    //token
    var token = ""

    //BaseURI
    const val BASE_URI = "https://api-ramh.onrender.com/"

    //EndPoint
    //Register
    const val REGISTER = "api/v1/users/register"
    const val MY_CRAFT = "api/v1/users"

    //Login
    const val LOGIN = "api/v1/users/login"

    //GetAllCrafts && CreateCraft && GetOneCraft && UpdateCraft
    const val CRAFT = "api/v1/crafts"

    //CreateOrder
    const val DELETE_CRAFT = "api/v1/crafts"

    //CraftList
    const val CRAFT_LIST = "api/v1/crafts/names"

    //CraftOfWorker
    const val CRAFT_OF_WORKER = "api/v1/users"

    //Create Offer
    const val CREATE_OFFER = "api/v1/offers"

    //Get Offers Of An Order
    const val OFFER_OF_AN_ORDER = "api/v1/offers/offersOfAnOrder"

    //Update Offer
    const val UPDATE_OFFER = "api/v1/offers"

    //profile
    const val GET_PROFILE = "api/v1/users/profile"

    //updateProfilePhoto
    const val UPDATE_PROFILE_PHOTO = "api/v1/users/image"

    //GetMyOffer
    const val GET_MY_OFFER = "api/v1/offers/myOffers"

    //updatePassword
    const val UPDATE_PASSWORD = "api/v1/users/updatePassword"


    val completeList = listOf(
        MyCraftOrderData(
            workerName = "أحمد محمد",
            workerRate = 5,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "أحمد محمد",
            workerRate = 5,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "محمد أحمد",
            workerRate = 3,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "محمد محمد",
            workerRate = 2,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "محمود محمد",
            workerRate = 4,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "محمود أحمد",
            workerRate = 3,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
        MyCraftOrderData(
            workerName = "عباس احمد",
            workerRate = 3,
            problemType = "صيانة بسيطة",
            problemTitle = "تصليح كرسي"
        ),
    )

    val reportList = listOf(
        "شخص مزيف",
        "سلوك غير لائق",
        "عدم التزام بالمواعيد",
        "سعر غير مناسب",
        "عمل غير متقن",
        "وصف غير دقيق للمشكلة",
        "عامل غير كفء",
    )

}