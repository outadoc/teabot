package fr.outadoc.teabot.data.db.room.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tea")
data class DbTea(
    @PrimaryKey
    @ColumnInfo(name = "tea_id")
    val teaId: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "sent_at_ts")
    val sentAtTs: Long,
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean,
    @ColumnInfo(name = "message")
    val messages: List<DbMessage>,
)
