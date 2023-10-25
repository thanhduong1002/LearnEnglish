package com.example.learnenglish.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.adapters.OptionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var optionAdapter: OptionAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = Html.fromHtml(
            "<font color=\"#442C2E\">" + getString(R.string.app_name) + "</font>",
            Html.FROM_HTML_MODE_LEGACY
        )

        val listOptions: List<String> = listOf("Vocabulary", "Practice", "Listening")
        val recyclerViewOptions: RecyclerView = findViewById(R.id.recyclerViewOptions)

        optionAdapter = OptionAdapter(listOptions)
        recyclerViewOptions.layoutManager = LinearLayoutManager(this)
        recyclerViewOptions.adapter = optionAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}