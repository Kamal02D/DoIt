package org.example.projectrr.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.projectrr.Constants

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("${Constants.DataBase.STORAGE_FILE_NAME}.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}