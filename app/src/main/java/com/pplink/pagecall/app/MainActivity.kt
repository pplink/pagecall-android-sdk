package com.pplink.pagecall.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pplink.pagecall.app.databinding.ActivityMainBinding
import com.pplink.pagecall.sdk.Pagecall
import com.pplink.pagecall.sdk.PagecallClient

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val url =
            "https://app.pagecall.net/6166384c3864013690838438/"
        val customJavascript = "window.close=()=>{Android.onExit();};"

        setPagecall(url, null, customJavascript)
    }

    private fun setPagecall(url: String, html: String?, customJavascript: String?) {
        val pagecall = Pagecall.newInstance(url, html)
        pagecall.pagecallClient = object: PagecallClient() {
            override fun onExit() {
                Log.d("Interface", "onExit")
            }
        }
        pagecall.customJavascript = customJavascript

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.pagecall, pagecall, "pagecall")
        transaction.addToBackStack(null)
        transaction.commit()
    }
}