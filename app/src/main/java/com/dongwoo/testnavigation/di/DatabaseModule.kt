package com.dongwoo.testnavigation.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.dongwoo.testnavigation.data.ToDoDatabase
import com.dongwoo.testnavigation.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesDao(database: ToDoDatabase) = database.toDoDao()

}