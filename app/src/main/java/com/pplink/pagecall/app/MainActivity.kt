package com.pplink.pagecall.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pplink.pagecall.app.databinding.ActivityMainBinding
import com.pplink.pagecall.sdk.Pagecall

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val url = "https://app.pagecall.net/615e85b22b096a00095bec7e?access_token=JLwNp1a7yIhzmrZ7BTUtMlTMXW-e3FdW"

//        lifecycleScope.launch {
//            val urlConnection = URL(url).openConnection() as HttpURLConnection
//
//            var html: String = withContext(Dispatchers.IO) {
//                urlConnection.inputStream.bufferedReader().readText()
//            }
//            val newHtml = html.replace("<head>", "" +
//                    "<head>\n" +
//                    "<script>\n" +
//                    "window.close=()=>{Android.onCloseListener();};\n" +
//                    "cordova = Object.create({}, {\n" +
//                    "    plugins: {\n" +
//                    "        value: false\n" +
//                    "    }, platformId: {\n" +
//                    "        value: \"ios\"\n" +
//                    "    }, platformVersion: {\n" +
//                    "        value: \"6.0.17\"\n" +
//                    "    }\n" +
//                    "});\n" +
//                    "</script>")
//
//        }

        val pagecallFragment = Pagecall.newInstance(url, null)
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.pagecall, pagecallFragment, "pagecall")
        transaction.addToBackStack(null)
        transaction.commit()
    }
}