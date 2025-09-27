package org.example.projectrr.di

import org.example.projectrr.data.getDatabaseBuilder
import org.example.projectrr.data.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { getRoomDatabase(getDatabaseBuilder(get())).getDao() }
}