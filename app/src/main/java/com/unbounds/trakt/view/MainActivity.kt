package com.unbounds.trakt.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Associate searchable configuration with the SearchView
        with(menu.findItem(R.id.action_search)) {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_pop_out_search)
                    return true
                }

            })
            with(actionView as SearchView) {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        findNavController(R.id.nav_host_fragment).navigate(NavigationDirections.actionGlobalNavigationSearch(query
                                ?: ""))
                        currentFocus?.let { window ->
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            imm?.hideSoftInputFromWindow(window.windowToken, 0)
                        }
                        main_root.requestFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // ignore
                        return false
                    }

                })
            }
        }

        return super.onCreateOptionsMenu(menu)
    }
}
