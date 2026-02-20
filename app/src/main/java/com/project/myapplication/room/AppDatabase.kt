package com.project.myapplication.room


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao
}
