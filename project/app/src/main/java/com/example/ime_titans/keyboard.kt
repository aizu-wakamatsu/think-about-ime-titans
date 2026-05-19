package com.example.ime_titans

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
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
            currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,keyInfo.code))
            currentInputConnection?.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP,keyInfo.code))
        }
    }
}

