
package com.hype.panel

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient

class FloatingPanelService : Service() {

    private lateinit var wm: WindowManager
    private var web: WebView? = null

    override fun onCreate() {
        super.onCreate()

        val channel = "hype_panel"

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                channel,
                "Hype Panel",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(notificationChannel)
        }

        val notification = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, channel)
        } else {
            Notification.Builder(this)
        }
            .setContentTitle("Hype Panel")
            .setContentText("Painel flutuante ativo")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(1, notification)

        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        val view = WebView(this)
        web = view

        view.settings.javaScriptEnabled = true
        view.webViewClient = WebViewClient()
        view.loadUrl("file:///android_asset/index.html")

        val params = WindowManager.LayoutParams(
            (resources.displayMetrics.widthPixels * 0.88).toInt(),
            450,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 100

        wm.addView(view, params)
    }

    override fun onDestroy() {
        web?.let {
            wm.removeView(it)
            it.destroy()
        }
        web = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
