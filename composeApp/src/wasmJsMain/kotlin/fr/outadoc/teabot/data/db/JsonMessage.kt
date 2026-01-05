package fr.outadoc.teabot.data.db

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsonMessage : JsAny {
    var message_id: String
    var user_id: String
    var user_name: String
    var text: String
}
