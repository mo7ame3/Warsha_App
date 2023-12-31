package com.example.warshaapp.navigation

enum class AllScreens {
    //shared

    SplashScreen,
    LoginScreen,
    ReportScreen,
    ChatDetails,
    ForgotPasswordScreen,
    ChangePasswordScreen,

    //client

    ClientHomeScreen,
    ClientPostScreen,
    ClientOrdersInCraftScreen,
    AdminClientProfileScreen,
    ClientProfileScreen,
    ClientMyProfileScreen,
    ClientOrderOffersScreen,
    ClientRateScreen,

    //worker

    WorkerHomeScreen,
    WorkerProblemDetails,
    AdminWorkerProfileScreen,
    WorkerProfileScreen,
    WorkerMyProfileScreen,
    MyProjectProblemDetails,

    //Admin

    AdminHomeScreen,
    AdminCraftsScreen,
    AdminEditCraftsScreen,
    AdminAllWorkersInSpecificCraft,
    AdminAllWorkers,
    AdminAllClients,
    AdminBlockedUsers,
    AdminReportsQuery,
    AdminReportListQuery,
    AdminCreateNewCraft,

    SettingScreen,
}