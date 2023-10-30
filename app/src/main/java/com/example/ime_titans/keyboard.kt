package com.example.ime_titans

import android.inputmethodservice.InputMethodService
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.tooling.preview.Preview
import com.example.ime_titans.ui.theme.ImetitansTheme

class keyboard : InputMethodService() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ImetitansTheme {
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
override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    return if (v is Button && event is MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // 押下したボタンのandroid:idを保持
                currentKeyId = v.id
                false
            }
            MotionEvent.ACTION_UP -> {
                // 押下ボタンに基づいた文字を入力
                sendKeyEvent(currentKeyId)
                currentKeyId = 0
                false
            }
            // タッチ位置が変わったり、複数指でタッチしても
            // 見た目の変化が発生しないようにする
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
        R.id.key_1 to KeyInfo.Num1,
        R.id.key_2 to KeyInfo.Num2,
        R.id.key_3 to KeyInfo.Num3,
        R.id.key_4 to KeyInfo.Num4,
        // 中略
        R.id.key_19 to KeyInfo.Num9,
        R.id.key_20 to KeyInfo.Num0
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

    object Num0 : AsciiKeyInfo() {
        override val char = '0'
        override val code = KeyEvent.KEYCODE_0
    }

    object Num1 : AsciiKeyInfo() {
        override val char = '1'
        override val code = KeyEvent.KEYCODE_1
    }

    // 以下略
}