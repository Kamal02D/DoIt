package org.example.projectrr.di


import org.example.projectrr.viewModels.MainScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedMode = module {
    viewModelOf(::MainScreenViewModel)
}

expect val platformModule : Module