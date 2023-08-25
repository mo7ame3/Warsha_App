package com.example.warshaapp.network

import com.example.warshaapp.constant.Constant
import com.example.warshaapp.model.admin.createNewCraft.CreateNewCraft
import com.example.warshaapp.model.admin.updateCraft.UpdateCraft
import com.example.warshaapp.model.client.createOrder.CreateOrder
import com.example.warshaapp.model.client.getMyOrder.GetMyOrder
import com.example.warshaapp.model.client.offerOfAnOrder.GetOfferOfAnOrder
import com.example.warshaapp.model.client.updateOrder.UpdateOrder
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.authentication.AuthenticationCraft
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.delete.Delete
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.model.shared.getCraft.GetCraft
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updateOffer.UpdateOffer
import com.example.warshaapp.model.shared.updatePassword.UpdatePassword
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.model.worker.createOffer.CreateOffer
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.model.worker.home.WorkerHome
import com.example.warshaapp.model.worker.orderDetails.GetOrderDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface WarshaApi {
    //Authentication

    //register
    @POST(Constant.REGISTER)
    suspend fun register(
        @Body registerInput: Map<String, String>
    ): Authentication

    //login
    @POST(Constant.LOGIN)
    suspend fun login(
        @Body loginInput: Map<String, String>
    ): Authentication

    //Worker choose Craft
    @PUT(Constant.MY_CRAFT + "/{workerId}")
    suspend fun workerChooseCraft(
        @Path("workerId") workerId: String,
        @Header("Authorization") authorization: String,
        @Body myCraft: Map<String, String>
    ): AuthenticationCraft

    //Get Craft List use in Register and Client Order Home
    @GET(Constant.CRAFT_LIST)
    suspend fun getCraftList(): AuthenticationCraftList

    //Get Craft

    @GET(Constant.CRAFT + "/{craftId}")
    suspend fun getCraft(
        @Path("craftId") craftId: String,
        @Header("Authorization") authorization: String
    ): GetCraft

    @PATCH(Constant.UPDATE_PASSWORD)
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Body updatePasswordBody: Map<String, String>
    ): UpdatePassword


    //Get Craft Of Worker

    @GET(Constant.CRAFT_OF_WORKER + "/{workerId}")
    suspend fun getCraftOfWorker(
        @Path("workerId") workerId: String,
    ): GetCraftOfWorker

    //Get All Crafts

    @GET(Constant.CRAFT)
    suspend fun getAllCrafts(
        @Header("Authorization") authorization: String,
    ): GetAllCrafts


    @GET(Constant.GET_PROFILE + "/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
    ): GetProfile

    //update profile photo client and worker
    @Multipart
    @PATCH(Constant.UPDATE_PROFILE_PHOTO + "/{userId}")
    suspend fun updateProfilePhoto(
        @Path("userId") userId: String,
        @Part image: MultipartBody.Part,
        @Header("Authorization") authorization: String
    ): UpdateProfile

    //Update Profile Setting
    @PATCH(Constant.GET_PROFILE + "/{userId}")
    suspend fun updateProfileData(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String,
        @Body updateProfileBody: Map<String, String>
    ): UpdateProfile


    //Client

    //Create Order

    @Multipart
    @POST("${Constant.CRAFT}/{craftID}/orders")
    suspend fun createOrder(
        @Path("craftID") craftId: String,
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("orderDifficulty") orderDifficulty: RequestBody,
        @Part("description") description: RequestBody
    ): CreateOrder


    //Get My Order
    @GET("${Constant.CRAFT}/{craftId}/orders/myorders")
    suspend fun getMyOrder(
        @Path("craftId") craftId: String,
        @Header("Authorization") authorization: String
    ): GetMyOrder


    //Delete Order
    @DELETE("${Constant.CRAFT}/{craftId}/orders/{orderId}")
    suspend fun deleteOrder(
        @Path("craftId") craftId: String,
        @Path("orderId") orderId: String,
        @Header("Authorization") authorization: String
    ): Delete

    //Get OrderOfAnCraft
    @GET(Constant.OFFER_OF_AN_ORDER + "/{orderId}")
    suspend fun getOfferOfAnOrder(
        @Header("Authorization") authorization: String,
        @Path("orderId") orderId: String
    ): GetOfferOfAnOrder

    //update Order
    @PATCH("${Constant.CRAFT}/{craftId}/orders/{orderId}")
    suspend fun updateOrder(
        @Path("craftId") craftId: String,
        @Path("orderId") orderId: String,
        @Header("Authorization") authorization: String,
        @Body updateOrderBody: Map<String, String>
    ): UpdateOrder

    //updateOffer for client and worker
    @PATCH(Constant.UPDATE_OFFER + "/{offerID}")
    suspend fun updateOffer(
        @Path("offerID") offerId: String,
        @Header("Authorization") authorization: String,
        @Body updateBody: Map<String, String>
    ): UpdateOffer


    //worker
    //Get worker Home

    @GET("api/v1/crafts/{craftId}/orders")
    suspend fun getWorkerHome(
        @Path("craftId") craftId: String,
        @Header("Authorization") authorization: String
    ): WorkerHome


    //get my offer
    @GET(Constant.GET_MY_OFFER + "/{userId}")
    suspend fun getMyOffer(
        @Path("userId") userId: String,
        @Header("Authorization") authorization: String
    ): GetMyOffer


    @GET(Constant.CRAFT + "/{craftId}/orders/{orderId}")
    suspend fun getOrderDetails(
        @Path("craftId") craftId: String,
        @Path("orderId") orderId: String,
        @Header("Authorization") authorization: String
    ): GetOrderDetails

    //Create Offer
    @POST(Constant.CREATE_OFFER + "/{orderId}")
    suspend fun createOffer(
        @Header("Authorization") authorization: String,
        @Path("orderId") orderId: String,
        @Body offerBody: Map<String, String>
    ): CreateOffer


    //admin
    //create craft
    @Multipart
    @POST(Constant.CRAFT)
    suspend fun adminCreateCraft(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part
    ): CreateNewCraft

    //updateCraft
    @Multipart
    @PATCH(Constant.CRAFT + "/{craftID}")
    suspend fun updateCraft(
        @Path("craftID") craftId: String,
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part? = null,
        @Part("name") name: RequestBody? = null,
    ): UpdateCraft

    //deleteCraft
    @DELETE(Constant.CRAFT + "/{craftID}")
    suspend fun deleteCraft(
        @Path("craftID") craftId: String,
        @Header("Authorization") authorization: String,
    ): Delete

}