package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityMainBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.OptionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var optionAdapter: OptionAdapter
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHtmlTitle(getString(R.string.app_name), getColor(R.color.text))

        val listOptions: List<String> = listOf("Vocabulary", "Practice", "Listening")

        optionAdapter = OptionAdapter(listOptions)
        binding.recyclerViewOptions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOptions.adapter = optionAdapter
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