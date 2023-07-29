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
    ClientMyCraftOrders,
    AdminClientProfileScreen,
    ClientProfileScreen,
    ClientMyProfileScreen,
    ClientOrderOfferScreen,
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
    AdminJobsScreen,
    AdminEditJobsScreen,
    AdminAllWorkersInSpecificJob,
    AdminAllWorkers,
    AdminAllClients,
    AdminBlockedUsers,
    AdminReportsQuery,
    AdminReportListQuery,
    AdminCreateNewCraft,

    ProfileScreen,
}