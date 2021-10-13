package com.pplink.pagecall.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.pplink.pagecall.app.databinding.ActivityMainBinding
import com.pplink.pagecall.sdk.Pagecall
import androidx.lifecycle.lifecycleScope
import com.pplink.pagecall.sdk.PagecallClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val url =
            "https://app.pagecall.net/6166384c3864013690838438/"

//        setPagecall(url, null)

        lifecycleScope.launch {
            val urlConnection = URL(url).openConnection() as HttpURLConnection

            var html: String = withContext(Dispatchers.IO) {
                urlConnection.inputStream.bufferedReader().readText()
            }
            val newHtml = html.replace(
                "<head>", "" +
                        "<head>\n" +
                        "<script>\n" +
                        "window.close=()=>{Android.onExit();};\n" +
                        "</script>"
            )

            setPagecall(url, newHtml)
        }
    }

    private fun setPagecall(url: String, html: String?) {
        val pagecall = Pagecall.newInstance(url, html)
        pagecall.pagecallClient = object: PagecallClient() {
            override fun onExit() {
                Log.d("Interface", "onExit")
            }
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.pagecall, pagecall, "pagecall")
        transaction.addToBackStack(null)
        transaction.commit()
    }
}