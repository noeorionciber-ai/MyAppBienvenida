package com.example.myappbienvenida

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure the app starts in Spanish if it's the first time onCreate is called in this session
        if (savedInstanceState == null) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("es")
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        setContentView(R.layout.activity_main)

        val languages = resources.getStringArray(R.array.languages)
        
        // Custom adapter to disable filtering
        val adapter = object : ArrayAdapter<String>(this, R.layout.dropdown_item, languages) {
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val results = FilterResults()
                        results.values = languages
                        results.count = languages.size
                        return results
                    }
                    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                        notifyDataSetChanged()
                    }
                }
            }
        }

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.languageAutoComplete)
        autoCompleteTextView.setAdapter(adapter)

        // Set initial text based on current locale
        val currentLang = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "es"
        val initialText = when (currentLang) {
            "en" -> languages[1]
            "fr" -> languages[2]
            "de" -> languages[3]
            else -> languages[0]
        }
        autoCompleteTextView.setText(initialText, false)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            val localeCode = when (selectedItem) {
                languages[1] -> "en"
                languages[2] -> "fr"
                languages[3] -> "de"
                else -> "es"
            }
            
            // Explicitly dismiss the dropdown and clear focus
            autoCompleteTextView.dismissDropDown()
            autoCompleteTextView.clearFocus()

            if (localeCode != currentLang) {
                setLocale(localeCode)
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}