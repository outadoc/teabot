package fr.outadoc.teabot.data.db.indexeddb.model

import com.juul.indexeddb.external.JsArray

@OptIn(ExperimentalWasmJsInterop::class)
external interface DbTea : JsAny {
    var tea_id: String
    var user_id: String
    var user_name: String
    var sent_at_ts: Long
    var is_archived: Boolean
    var messages: JsArray<DbMessage>
}
