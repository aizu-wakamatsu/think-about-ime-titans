package com.example.ime_titans

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class Keyboard : InputMethodService(), View.OnTouchListener {

    private val keymap: KeymapHolder = Keymap()
    private var currentKeyId = 0

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        return layoutInflater.inflate(R.layout.keyboard, null).also { view ->
            keymap.keys.map { id ->
                view.findViewById<Button>(id).also { it.setOnTouchListener(this) }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return if (v is Button && event is MotionEvent) {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    currentKeyId = v.id
                    false
                }

                MotionEvent.ACTION_UP -> {
                    sendKeyEvent(currentKeyId)
                    currentKeyId = 0
                    false
                }

                else -> true
            }
        } else {
            true
        }
    }

    private fun sendKeyEvent(id: Int) {
        if (id !in keymap.keys) {
            return
        }

        val keyInfo = keymap.getKeyInfo(id)
        if (keyInfo is KeyInfo.AsciiKeyInfo) {
            sendKeyChar(keyInfo.char)
        }
        else if (keyInfo is KeyInfo.CtrlKeyInfo) {
            sendKeyEvent(keyInfo.code)
        }
    }
}

interface KeymapHolder {
    val keys: Set<Int>
    fun getKeyInfo(indexId: Int): KeyInfo
}

class Keymap : KeymapHolder {
    /*
            キーの配列は以下の通り。03,04,05,10はカーソルキー

            layout
            01 02    04    06 07
                  03    05
            08 09    10    11 12

            key mapping
            L1 L2    Up    R1 R2
                  Lh    Rh
            L3 L4    Dn    R3 R4
     */
    private val list: Map<Int, KeyInfo> = mapOf(
        R.id.key_01 to KeyInfo.L1,
        R.id.key_02 to KeyInfo.L2,
        R.id.key_03 to KeyInfo.Lh,
        R.id.key_04 to KeyInfo.Up,
        R.id.key_05 to KeyInfo.Rh,
        R.id.key_06 to KeyInfo.R1,
        R.id.key_07 to KeyInfo.R2,
        R.id.key_08 to KeyInfo.L3,
        R.id.key_09 to KeyInfo.L4,
        R.id.key_10 to KeyInfo.Dn,
        R.id.key_11 to KeyInfo.R3,
        R.id.key_12 to KeyInfo.R4,

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
        abstract val code: Int
    }

    abstract class CtrlKeyInfo : KeyInfo() {
        abstract val code: Int
    }

    object Up : CtrlKeyInfo() {
        override val code = 0
    }

    object Dn : CtrlKeyInfo() {
        override val code = 1
    }

    object Lh : CtrlKeyInfo() {
        override val code = 2
    }

    object Rh : CtrlKeyInfo() {
        override val code = 3
    }

    object L1 : AsciiKeyInfo() {
        override val char = '4'
        override val code = 4
    }

    object L2 : AsciiKeyInfo() {
        override val char = '5'
        override val code = 5
    }

    object L3 : AsciiKeyInfo() {
        override val char = '6'
        override val code = 7
    }

    object L4 : AsciiKeyInfo() {
        override val char = '7'
        override val code = 8
    }

    object R1 : AsciiKeyInfo() {
        override val char = '8'
        override val code = 9
    }

    object R2 : AsciiKeyInfo() {
        override val char = '9'
        override val code = 10
    }

    object R3 : AsciiKeyInfo() {
        override val char = 'r'
        override val code = 11
    }

    object R4 : AsciiKeyInfo() {
        override val char = '\n'
        override val code = 12
    }
}
