package com.toknfc.nfctok.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by Chidi Justice
 */
//@Database(entities = arrayOf(), version = 1)
abstract class TagsDb{//: RoomDatabase() {

  /*companion object {
    val DB_NAME = "tags_db"
    var dbInstance: TagsDb? = null

    fun getDatabase(context: Context): TagsDb? {
      if (dbInstance == null) {
        dbInstance = Room.databaseBuilder<TagsDb>(context.applicationContext, TagsDb::class.java,
            DB_NAME).build()
      }
      return dbInstance
    }
  }*/
}