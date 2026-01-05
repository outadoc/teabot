package fr.outadoc.teabot.data.db

internal fun <T : JsAny> jso(): T = js("({})")
