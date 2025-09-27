package org.example.projectrr

import androidx.compose.ui.window.ComposeUIViewController
import org.example.projectrr.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}