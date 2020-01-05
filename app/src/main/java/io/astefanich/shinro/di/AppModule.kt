package io.astefanich.shinro.di

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.R
import io.astefanich.shinro.database.AppDatabase
import io.astefanich.shinro.database.BoardDao
import io.astefanich.shinro.database.BoardGenerator
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.domain.DatabaseName
import io.astefanich.shinro.domain.Grid
import timber.log.Timber
import java.util.concurrent.Executors

@Module
class AppModule {

    @AppScope
    @Provides
    internal fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @AppScope
    @Provides
    internal fun providesDatabaseFromFile(
        application: Application,
        databaseName: DatabaseName
    ): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, databaseName.name)
            .allowMainThreadQueries()
            .createFromAsset(databaseName.name)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.i("ONCREATE")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Timber.i("ONOPEN")
                }
            }
            )
            .build()
    }

    @AppScope
//    @Provides
    internal fun providesAppDatabase(
        application: Application,
        databaseName: DatabaseName,
        boards: Array<Board>
    ): AppDatabase {

        lateinit var appDatabase: AppDatabase
        appDatabase = Room.databaseBuilder(
            application, AppDatabase::class.java,
            databaseName.name
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadScheduledExecutor().execute() {
                        Timber.i("ONCREATE DB")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS LOADED DB")
                    }
                }
            }
            )
            .build()
        return appDatabase
    }

    @AppScope
//    @Provides
    internal fun providesInMemoryAppDatabase(
        application: Application,
        boards: Array<Board>
    ): AppDatabase {

        lateinit var appDatabase: AppDatabase
        appDatabase = Room.inMemoryDatabaseBuilder(
            application, AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadScheduledExecutor().execute() {
                        Timber.i("ONCREATE INMEMORY")
                        appDatabase.boardDao().insertBoards(*boards)
                        Timber.i("BOARDS LOADED INMEMORY")
                    }
                }
            }
            )
            .build()
        return appDatabase
    }


    @AppScope
    @Provides
    internal fun providesBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @AppScope
    @Provides
    internal fun providesDummyBoard(): Board = Board(999, "EASY", Grid(arrayOf()))

    @AppScope
    @Provides
    internal fun providesVideoURI(): Uri =
        Uri.parse("android.resource://io.astefanich.shinro/" + R.raw.what_is_shinro)

    @AppScope
    @Provides
    internal fun providesDatabaseName(): DatabaseName = DatabaseName("shinro.db")

    @AppScope
    @Provides
    internal fun providesBoards(): Array<Board?> = BoardGenerator.getBoards()
}


