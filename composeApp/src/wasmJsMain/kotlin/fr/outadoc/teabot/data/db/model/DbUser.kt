package fr.outadoc.teabot.data.db.model

@OptIn(ExperimentalWasmJsInterop::class)
external interface DbUser : JsAny {
    var user_id: String
    var user_name: String
    var teas: JsArray<DbTea>
}
