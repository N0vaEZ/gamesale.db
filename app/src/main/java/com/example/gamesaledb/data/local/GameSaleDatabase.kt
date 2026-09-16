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
        GameEntity::class,
        PriceEntity::class,
        GameNoteEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class GameSaleDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao

    abstract fun gameDao(): GameDao

    abstract fun priceDao(): PriceDao

    abstract fun gameNoteDao(): GameNoteDao

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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7
                    )
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `prices` (
                `gameId` TEXT NOT NULL,
                `shopId` INTEGER NOT NULL,
                `shopName` TEXT NOT NULL,
                `amount` REAL NOT NULL,
                `currency` TEXT NOT NULL,
                PRIMARY KEY(`gameId`, `shopId`)
            )
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE wishlist ADD COLUMN title TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    "ALTER TABLE wishlist ADD COLUMN slug TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE wishlist ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'SYNCED'"
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE wishlist ADD COLUMN syncOperation TEXT NOT NULL DEFAULT 'ADD'"
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `game_notes` (
                `gameId` TEXT NOT NULL,
                `note` TEXT NOT NULL,
                PRIMARY KEY(`gameId`)
            )
            """.trimIndent()
                )
            }
        }
    }
}