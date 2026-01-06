package fr.outadoc.teabot.data.db

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsonMessage : JsAny {
    var message_id: String
    var sent_at_iso: String
    var text: String
}
