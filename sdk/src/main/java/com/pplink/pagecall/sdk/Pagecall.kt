package com.pplink.pagecall.sdk

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.pplink.pagecall.sdk.databinding.FragmentPagecallBinding
import java.lang.Exception

private const val URL = "url"
private const val HTML = "html"

class Pagecall : Fragment() {
    private lateinit var _url: String
    private var _html: String? = null
    private lateinit var _binding: FragmentPagecallBinding
    private lateinit var _webView: WebView
    private var _filePathCallback: ValueCallback<Array<Uri>>? = null
    open var onExit: () -> Unit = {}

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("PagecallPermission", "${it.key} = ${it.value}")
            }
        }

    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // WebView 내부에서 파일 선택자를 열기 위한 로직
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                var results: Array<Uri>? = null

                it.data?.let { data ->
                    data.dataString?.let { dataString ->
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
                _filePathCallback!!.onReceiveValue(results)
            } else {
                // 에러 또는 선택된 파일이 없더라도 반드시 초기화 해주어야 한다.
                // 그렇지 않으면 다시 파일 선택자가 열리지 않는다.
                _filePathCallback!!.onReceiveValue(null)
            }

            _filePathCallback = null
        }

    inner class PagecallClientInterface {
        @JavascriptInterface
        fun onClose() {
            onExit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _url = it.getString(URL)!!
            _html = it.getString(HTML)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestMultiplePermissionsLauncher.launch(PERMISSIONS)
        _binding = FragmentPagecallBinding.inflate(layoutInflater, container, false)

        _webView = _binding.webView

        _webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest?) {
                    request?.grant(request.resources)
                }

                // 파일 업로드를 위한 설정
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    if (_filePathCallback != null) {
                        _filePathCallback!!.onReceiveValue(null)
                        _filePathCallback = null
                    }

                    try {
                        _filePathCallback = filePathCallback
                        val intent = Intent()
                        intent.apply {
                            action = Intent.ACTION_GET_CONTENT
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "*/*"
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileChooserParams!!.acceptTypes)
                        }

                        filterActivityLauncher.launch(intent)
                    } catch (e: Exception) {
                        _filePathCallback!!.onReceiveValue(null)
                        _filePathCallback = null
                    }

                    return true
                }
            }
        }

        CookieManager.getInstance().setAcceptThirdPartyCookies(_webView, true)

        _webView.addJavascriptInterface(PagecallClientInterface(), "Android")

        if (_html == null) {
            _webView.loadUrl(_url)
        } else {
            _webView.loadDataWithBaseURL(_url, _html!!, "text/html", "utf-8", null)
        }

        return _binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String, html: String?) =
            Pagecall().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                    putString(HTML, html)
                }
            }

        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        )
    }
}