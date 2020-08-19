package com.unbounds.trakt.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import com.unbounds.trakt.NavigationDirections
import com.unbounds.trakt.R
import com.unbounds.trakt.utils.Event
import com.unbounds.trakt.utils.consume
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

        authError.observe(this) { event ->
            event.consume {
                findNavController(R.id.nav_host_fragment).navigate(NavigationDirections.actionGlobalNavigationLogin())
            }
        }
    }
}
