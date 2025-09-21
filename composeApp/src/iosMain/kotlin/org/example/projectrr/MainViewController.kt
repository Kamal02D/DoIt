package org.example.projectrr

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.Dispatchers
import org.example.projectrr.data.getDatabaseBuilder
import org.example.projectrr.data.getRoomDatabase
import org.example.projectrr.models.Task

fun MainViewController() = ComposeUIViewController {
    val dbBuilder = remember {
        getDatabaseBuilder()
    }

    val db = remember {
        getRoomDatabase(dbBuilder)
    }
    val dbDoa = db.getDao()
    LaunchedEffect(Dispatchers.Main){
        dbDoa.insert(
            Task(
                text = "gg",
                isDone = false
            )
        )
    }
    App()
}