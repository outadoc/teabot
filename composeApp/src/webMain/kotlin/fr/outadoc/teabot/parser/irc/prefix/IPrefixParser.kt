/**
 * Copyright 2018 WillowChat project.
 * This file is distributed under the ISC license.
 * https://github.com/WillowChat/Kale
 */

package fr.outadoc.teabot.parser.irc.prefix

internal interface IPrefixParser {
    fun parse(rawPrefix: String): Prefix?
}
