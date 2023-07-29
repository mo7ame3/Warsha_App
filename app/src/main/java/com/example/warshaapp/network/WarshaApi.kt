package com.example.warshaapp.network

import com.example.warshaapp.constant.Constant
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.authentication.AuthenticationCraft
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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


    //Get Craft Of Worker

    @GET(Constant.CRAFT_OF_WORKER + "/{workerId}")
    suspend fun getCraftOfWorker(
        @Path("workerId") workerId: String,
    ): GetCraftOfWorker

}