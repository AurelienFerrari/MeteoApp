package com.example.meteoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView


class SettingsActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                // Naviguer vers le fragment ou l'activité de la page d'accueil
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                // Déjà dans l'activité de paramètres, aucune action nécessaire
                return@OnNavigationItemSelectedListener true
            }
            // Ajoutez d'autres éléments de navigation ici si nécessaire
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }
}