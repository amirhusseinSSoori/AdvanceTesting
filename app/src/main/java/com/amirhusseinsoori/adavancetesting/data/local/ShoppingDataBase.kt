package com.amirhusseinsoori.adavancetesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingDataBase::class],version = 1)
abstract class ShoppingDataBase: RoomDatabase() {
    abstract fun shoppingDao():ShoppingDao
}