package fr.outadoc.teabot.data.db.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import fr.outadoc.teabot.data.db.room.model.DbTea
import kotlinx.coroutines.flow.Flow

@Dao
interface TeaMessageDao {
    @Upsert
    fun upsertTea(tea: DbTea)

    @Query("SELECT * FROM tea")
    fun getAllTea(): Flow<List<DbTea>>

    @Query("SELECT * FROM tea WHERE user_id = :userId")
    fun getTeaForUser(userId: String): List<DbTea>
}
