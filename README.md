# Pagecall Android SDK
[![](https://jitpack.io/v/pplink/pagecall-android-sdk.svg)](https://jitpack.io/#pplink/pagecall-android-sdk)
## 개요
- 고객사의 원활한 Pagecall 도입을 위해 제공되는 Android SDK 입니다.
- Android SDK를 활용한 데모를 제공하고 있습니다. [링크](https://github.com/pplink/pagecall-demo)
- Pagecall SDK는 Fragment를 사용하고 있습니다.

## 기능
- 페이지콜 입장
- Custom Javascript 주입
- URL 방식, HTML 방식 모두 지원
- 미팅 나가기, 에러 발생 등의 이벤트를 처리할 수 있는 인터페이스 (지원 예정)

## 지원 범위
- Android API ≥ 21
- Pagecall SDK는 API >= 28 에서 안정된 동작을 보장합니다.
- Pagecall SDK는 API < 24 에서 동작을 하지 않습니다.

## 사용법
1. 프로젝트의 settings.gradle 혹은 build.gradle에 다음을 추가합니다.
```
// settings.gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url "https://jitpack.io" } // 이 라인을 추가합니다.
    }
}
 
// build.gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" } // 이 라인을 추가합니다.
    }
}
````
2. 페이지콜을 실행시키는 모듈의 build.gradle에 다음을 추가합니다.
```
 // 버전은 문서 상단 JitPack Badge의 버전을 사용하시면 됩니다.
...
dependencies {
    ...
    implementation 'com.github.pplink.pagecall-android-sdk:sdk:$Version'
}
...
```
3. AndroidManifest.xml에 다음 권한을 추가합니다.
```
...
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.webkit.PermissionRequest" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera2.full" />
```
4. 다음 예제와 같이 페이지콜을 실행합니다.
```
override fun onCreate(savedInstanceState: Bundle?) 
{
     ...
     val url = "Pagecall URL"
     val customJavascript = "console.log("Hello, Pagecall");"

     setPagecall(url, null, customJavascript)
 }

 private fun setPagecall(url: String, html: String?, customJavascript: String?) 
 {
     val pagecall = Pagecall.newInstance(url, html)
     pagecall.customJavascript = customJavascript

     // 예시) 페이지콜이 띄워질 위치의 View와 Pagecall Fragment를 치환
     val transaction = supportFragmentManager.beginTransaction()
     transaction.replace(R.id.pagecall, pagecall, "pagecall")
     transaction.addToBackStack(null)
     transaction.commit()
 }
```

## 라이선스
[Apache-2.0](./LICENSE)
