package fr.outadoc.teabot.data.db.model

@OptIn(ExperimentalWasmJsInterop::class)
external interface DbMessage : JsAny {
    var message_id: String
    var sent_at_ts: Long
    var text: String
}
