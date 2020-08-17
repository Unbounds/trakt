package com.unbounds.trakt.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.unbounds.trakt.R
import com.unbounds.trakt.utils.Event
import com.unbounds.trakt.utils.consume
import com.unbounds.trakt.view.login.LoginActivity
import com.unbounds.trakt.view.progress.ProgressFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @Named("auth")
    lateinit var authError: LiveData<Event<Unit>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        authError.observe(this, Observer { event ->
            event.consume {
                startActivityForResult(LoginActivity.createIntent(this@MainActivity), LOGIN_REQUEST)
            }
        })

        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, ProgressFragment()).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_content, ProgressFragment()).commit()
            }
        }
    }

    companion object {
        private const val LOGIN_REQUEST = 1
    }
}
