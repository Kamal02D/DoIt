package org.example.projectrr.routing

import kotlinx.serialization.Serializable

enum class Screens{
    SPLASH_SCREEN,
    MAIN
}
@Serializable
sealed class Routes {
    constructor(title: Screens)
    @Serializable
    data object Splash : Routes(title = Screens.SPLASH_SCREEN)
    @Serializable
    data object Main : Routes(title = Screens.MAIN)
}