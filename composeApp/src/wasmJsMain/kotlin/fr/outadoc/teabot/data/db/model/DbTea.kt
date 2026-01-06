package fr.outadoc.teabot.data.db.model

import com.juul.indexeddb.external.JsArray

@OptIn(ExperimentalWasmJsInterop::class)
external interface DbTea : JsAny {
    var sent_at_ts: Long
    var is_archived: Boolean
    var messages: JsArray<DbMessage>
}
