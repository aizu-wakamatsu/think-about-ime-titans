package com.example.ime_titans

import android.view.KeyEvent

interface KeymapHolder {

    val keys: Set<Int>
    fun getKeyInfo(indexId: Int): KeyInfo

}

class Keymap : KeymapHolder {

    private val list: Map<Int, KeyInfo> = mapOf(
        R.id.key_BktCurlyLeft to KeyInfo.BktCurlyLeft,
        R.id.key_BktCurlyRight to KeyInfo.BktCurlyRight,
        R.id.key_BktSquareLeft to KeyInfo.BktSquareLeft,
        R.id.key_BktSquareRight to KeyInfo.BktSquareRight,
        R.id.key_LessThan to KeyInfo.LessThan,
        R.id.key_GreaterThan to KeyInfo.GreaterThan,
        R.id.key_Dollar to KeyInfo.Dollar,
        R.id.key_Percent to KeyInfo.Percent,
        R.id.key_Ampersand to KeyInfo.Ampersand,
        R.id.key_Equals to KeyInfo.Equals,
        R.id.key_Circumflex to KeyInfo.Circumflex,
        R.id.key_Tilde to KeyInfo.Tilde,
        R.id.key_VerticalBar to KeyInfo.VerticalBar,
        R.id.key_Grave to KeyInfo.Grave,
        R.id.key_Semicolon to KeyInfo.Semicolon,
        R.id.key_Backslash to KeyInfo.Backslash,
        R.id.key_Enter to KeyInfo.Enter,
        R.id.key_ArrowLeft to KeyInfo.ArrowLeft,
        R.id.key_ArrowRight to KeyInfo.ArrowRight,
        R.id.key_ArrowUp to KeyInfo.ArrowUp,
        R.id.key_ArrowDown to KeyInfo.ArrowDown,
    )

    override val keys = list.keys

    override fun getKeyInfo(indexId: Int): KeyInfo {
        return list.getOrDefault(indexId, KeyInfo.Null)
    }
}

sealed class KeyInfo {
    object Null : KeyInfo()

    abstract class AsciiKeyInfo : KeyInfo() {
        abstract val char: Char
    }

    abstract class CtrlKeyInfo : KeyInfo() {
        abstract val code: Int
    }

    object BktCurlyLeft : AsciiKeyInfo() {
        override val char = '{'
    }

    object BktCurlyRight : AsciiKeyInfo() {
        override val char = '}'
    }

    object BktSquareLeft : AsciiKeyInfo() {
        override val char = '['
    }

    object BktSquareRight : AsciiKeyInfo() {
        override val char = ']'
    }

    object LessThan : AsciiKeyInfo() {
        override val char = '<'
    }

    object GreaterThan : AsciiKeyInfo() {
        override val char = '>'
    }

    object Dollar : AsciiKeyInfo() {
        override val char = '$'
    }

    object Percent : AsciiKeyInfo() {
        override val char = '%'
    }

    object Ampersand : AsciiKeyInfo() {
        override val char = '&'
    }

    object Equals : AsciiKeyInfo() {
        override val char = '='
    }

    object Circumflex : AsciiKeyInfo() {
        override val char = '^'
    }

    object Tilde : AsciiKeyInfo() {
        override val char = '~'
    }

    object VerticalBar : AsciiKeyInfo() {
        override val char = '|'
    }

    object Grave : AsciiKeyInfo() {
        override val char = '`'
    }

    object Semicolon : AsciiKeyInfo() {
        override val char = ';'
    }

    object Backslash : AsciiKeyInfo() {
        override val char = '\\'
    }

    object Enter : AsciiKeyInfo() {
        override val char = '\n'
    }

    object ArrowUp : CtrlKeyInfo() {
        override val code = KeyEvent.KEYCODE_DPAD_UP
    }

    object ArrowDown : CtrlKeyInfo() {
        override val code = KeyEvent.KEYCODE_DPAD_DOWN
    }

    object ArrowLeft : CtrlKeyInfo() {
        override val code = KeyEvent.KEYCODE_DPAD_LEFT
    }

    object ArrowRight : CtrlKeyInfo() {
        override val code = KeyEvent.KEYCODE_DPAD_RIGHT
    }
}
