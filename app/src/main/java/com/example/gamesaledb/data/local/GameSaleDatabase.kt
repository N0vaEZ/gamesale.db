package com.example.gamesaledb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        WishlistEntity::class,
        GameEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class GameSaleDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameSaleDatabase? = null

        fun getDatabase(context: Context): GameSaleDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameSaleDatabase::class.java,
                    "gamesale_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `games` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `slug` TEXT NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
                )
            }
        }
    }
}