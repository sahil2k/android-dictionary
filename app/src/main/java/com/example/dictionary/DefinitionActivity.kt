package com.example.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class DefinitionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definition)
        var definition:String?=intent.getStringExtra("my definition")
        var defBox:TextView=findViewById(R.id.def_box)
        defBox.text=definition

    }
    fun goBack(view:View){
        val backIntent= Intent(applicationContext, MainActivity::class.java)
        startActivity(backIntent)
    }
}