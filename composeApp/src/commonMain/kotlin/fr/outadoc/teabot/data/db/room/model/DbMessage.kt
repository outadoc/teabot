package fr.outadoc.teabot.data.db.room.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "message")
data class DbMessage(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId: String,
    @ColumnInfo(name = "sent_at_ts")
    val sentAtTs: Long,
    @ColumnInfo(name = "text")
    val text: String,
)
