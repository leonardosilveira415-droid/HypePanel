package com.hype.panel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)
        layout.setBackgroundColor(Color.rgb(7, 17, 29))

        val title = TextView(this)
        title.text = "HYPE PANEL"
        title.textSize = 25f
        title.setTextColor(Color.MAGENTA)
        title.gravity = Gravity.CENTER

        val button = Button(this)
        button.text = "Abrir painel flutuante"

        button.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                startForegroundService(
                    Intent(this, FloatingPanelService::class.java)
                )
            } else {
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                )
            }
        }

        layout.addView(title)
        layout.addView(button)
        setContentView(layout)
    }
}
