package com.unbounds.trakt.view.login

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.unbounds.trakt.BuildConfig
import com.unbounds.trakt.R
import com.unbounds.trakt.service.api.AuthInterceptor
import com.unbounds.trakt.service.api.TraktApi
import com.unbounds.trakt.service.api.model.trakt.request.Code
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var api: TraktApi

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val connection: CustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, client: CustomTabsClient) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                client.warmup(0L) // This prevents backgrounding after redirection
                customTabsIntent.launchUrl(this@LoginActivity, Uri.parse(String.format("%s/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
                        BuildConfig.BASE_URL,
                        BuildConfig.CLIENT_ID,
                        getString(R.string.oauth_referrer))))
            }

            override fun onServiceDisconnected(name: ComponentName) {}
        }
        CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", connection)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        parseResponse(intent.data)
    }

    private fun parseResponse(uri: Uri?) {
        if (uri != null && uri.toString().startsWith(getString(R.string.oauth_referrer))) {
            val authCode = uri.getQueryParameter("code")
            if (authCode != null) {
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    val code = Code(authCode, getString(R.string.oauth_referrer))
                    val token = withContext(Dispatchers.IO) {
                        api.getToken(code).await().body()
                    }

                    authInterceptor.token = token

                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    companion object {
        fun createIntent(activity: Activity?): Intent {
            return Intent(activity, LoginActivity::class.java)
        }
    }
}
