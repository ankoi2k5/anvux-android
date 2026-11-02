package com.example.anvux.core.database

import android.content.Context
import androidx.room.Room
import com.example.anvux.data.local.ProjectDao
import com.example.anvux.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AnVuxDatabase =
        Room.databaseBuilder(context, AnVuxDatabase::class.java, "anvux.db")
            // Dữ liệu gốc nằm trên server nên khi đổi cấu trúc bảng ở giai đoạn dev,
            // ta xóa và tải lại. Bản phát hành sẽ thay bằng migration thật.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideProjectDao(database: AnVuxDatabase): ProjectDao = database.projectDao()

    @Provides
    fun provideTaskDao(database: AnVuxDatabase): TaskDao = database.taskDao()
}