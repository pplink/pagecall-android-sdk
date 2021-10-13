package com.pplink.pagecall.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import com.pplink.pagecall.app.databinding.ActivityMainBinding
import com.pplink.pagecall.sdk.Pagecall
import androidx.lifecycle.lifecycleScope
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
                        "window.close=()=>{Android.onClose();};\n" +
                        "cordova = Object.create({}, {\n" +
                        "    plugins: {\n" +
                        "        value: false\n" +
                        "    }, platformId: {\n" +
                        "        value: \"ios\"\n" +
                        "    }, platformVersion: {\n" +
                        "        value: \"6.0.17\"\n" +
                        "    }\n" +
                        "});\n" +
                        "</script>"
            )

            setPagecall(url, newHtml)
        }
    }

    private fun setPagecall(url: String, html: String?) {
        val pagecallFragment = Pagecall.newInstance(url, html)
        pagecallFragment.onExit = { Log.d("Interface", "onExit") }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.pagecall, pagecallFragment, "pagecall")
        transaction.addToBackStack(null)
        transaction.commit()
    }
}