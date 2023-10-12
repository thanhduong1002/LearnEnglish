package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.ui.fragments.MultipleChoiceFragment
import com.example.learnenglish.ui.fragments.WordFillingFragment

class DetailPracticeActivity : AppCompatActivity() {
    private var quantity: Int = 1
    private var receivedData: String? = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_practice)

        val receivedIntent = intent

        if (receivedIntent != null) {
            receivedData = receivedIntent.getStringExtra(Quantity)

            if (receivedData != null) {
                supportActionBar?.title =
                    Html.fromHtml("<font color=\"#442C2E\">$quantity/$receivedData</font>", Html.FROM_HTML_MODE_LEGACY)
            }
        }

        receivedData?.let { checkAndUpdate(it.toInt()) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun checkAndUpdate(questions: Int) {
        val buttonCheck: Button = findViewById(R.id.buttonCheck)
        val forgetTextView: TextView = findViewById(R.id.textViewForget)
        var isChecked = false

        forgetTextView.paintFlags = forgetTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        buttonCheck.text = "Check"

        updateFragment(quantity)

        buttonCheck.setOnClickListener {
            if (!isChecked) {
                buttonCheck.text = "Next"
                forgetTextView.text = ""
                isChecked = true
            } else {
                quantity++
                isChecked = false

                supportActionBar?.title =
                    Html.fromHtml("<font color=\"#442C2E\">$quantity/$receivedData</font>", Html.FROM_HTML_MODE_LEGACY)

                if (quantity == questions + 1) {
                    buttonCheck.text = "Done"
                } else {
                    buttonCheck.text = "Check"
                    forgetTextView.text = "I don't remember this word!"
                }
            }

            if (quantity == questions + 1 && isChecked) {
                intent = Intent(this, PracticeActivity::class.java)
                intent.putExtra(PracticeActivity.Title, "Practice")

                startActivity(intent)
            }

            updateFragment(quantity)
        }

        forgetTextView.setOnClickListener {
            quantity++

            supportActionBar?.title =
                Html.fromHtml("<font color=\"#442C2E\">$quantity/$receivedData</font>", Html.FROM_HTML_MODE_LEGACY)

            if (quantity == 11) {
                intent = Intent(this, PracticeActivity::class.java)
                intent.putExtra(PracticeActivity.Title, "Practice")

                startActivity(intent)
            }

            updateFragment(quantity)
        }
    }

    private fun updateFragment(number: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = if (number % 2 == 0) {
            MultipleChoiceFragment()
        } else {
            WordFillingFragment()
        }

        fragmentTransaction.replace(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val Quantity = "Quantity"
    }
}