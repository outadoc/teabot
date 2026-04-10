package fr.outadoc.teabot.data.db.room

import androidx.room3.Database
import androidx.room3.RoomDatabase
import fr.outadoc.teabot.data.db.room.model.DbMessage
import fr.outadoc.teabot.data.db.room.model.DbTea

@Database(
    entities = [
        DbMessage::class,
        DbTea::class,
    ],
    version = 1,
)
abstract class RoomTeaDatabase : RoomDatabase() {
    abstract fun teaMessageDao(): TeaMessageDao
}
