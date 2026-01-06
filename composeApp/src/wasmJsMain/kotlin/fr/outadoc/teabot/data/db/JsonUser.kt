package fr.outadoc.teabot.data.db

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsonUser : JsAny {
    var user_id: String
    var user_name: String
}
