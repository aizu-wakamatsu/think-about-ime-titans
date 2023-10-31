package com.example.ime_titans

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class Keyboard : InputMethodService(), View.OnTouchListener {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ImeTitansTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }

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

    //    private fun setOnKeyboardActionListener(keyboard: keyboard) {
//
//    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(btn: View?, event: MotionEvent?): Boolean {
        return if (btn is Button && event is MotionEvent) {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    currentKeyId = btn.id
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

    // キー入力。本来はもっとごちゃごちゃするので簡略化。
    private fun sendKeyEvent(id: Int) {
        if (id !in keymap.keys) {
            return
        }

        val keyInfo = keymap.getKeyInfo(id)
        if (keyInfo is KeyInfo.AsciiKeyInfo) {
            sendKeyChar(keyInfo.char)
        }
    }

}

interface KeymapHolder {
    val keys: Set<Int>
    fun getKeyInfo(indexId: Int): KeyInfo
}

class Keymap : KeymapHolder {
    private val list: Map<Int, KeyInfo> = mapOf(
        R.id.key_1 to KeyInfo.L1,
        R.id.key_2 to KeyInfo.L2,
        R.id.key_3 to KeyInfo.Lh,
        R.id.key_4 to KeyInfo.Up,
        R.id.key_5 to KeyInfo.Rh,
        R.id.key_6 to KeyInfo.R1,
        R.id.key_7 to KeyInfo.R2,
        R.id.key_8 to KeyInfo.L3,
        R.id.key_9 to KeyInfo.L4,
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

    object Up : AsciiKeyInfo() {
        override val char = '1'
        override val code = 0
    }

    object Dn : AsciiKeyInfo() {
        override val char = '1'
        override val code = 1
    }

    object Lh : AsciiKeyInfo() {
        override val char = '2'
        override val code = 2
    }

    object Rh : AsciiKeyInfo() {
        override val char = '3'
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
