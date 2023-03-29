package de.rehatech2223.lgg_frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import de.rehatech2223.lgg_frontend.services.ServiceProvider
import okhttp3.Request

class ConnectionErrorActivity : DynamicThemeActivity() {

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme()
        setContentView(R.layout.connection_detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        url = intent.getStringExtra("url")!!

        initViews()
    }

    private fun initViews() {
        val textField: TextInputEditText = findViewById(R.id.text_input)
        val connectButton: Button = findViewById(R.id.connect_button)
        connectButton.setOnClickListener {
            Log.d("handler", "trying to reconnect!")
            if (textField.text == null) return@setOnClickListener
            else {
                val tempUrl = "http://${textField.text.toString()}/"
                if (!verifyUrl(tempUrl)) return@setOnClickListener

                ServiceProvider.baseUrl = tempUrl
                Log.d("handler", "base url is now: ${ServiceProvider.baseUrl}")
                restartMainActivity()
            }
        }
        findViewById<Button>(R.id.raspberrypi_button).setOnClickListener {
            Log.d("handler", "restart using default ip")
            ServiceProvider.baseUrl = "http://raspberrypi:9000/"
            restartMainActivity()
        }
    }

    private fun restartMainActivity(){
        finish()
        val context: Context = SmarthomeApplication.getContext()
        val mainActivity = Intent(context, MainActivity::class.java)
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mainActivity)
    }

    private fun verifyUrl(tempUrl: String): Boolean {
        return try {
            Request.Builder()
                .url(tempUrl + "device/list")
                .get()
                .build()
            true
        } catch (e: Exception) {
            false
        }
    }

}