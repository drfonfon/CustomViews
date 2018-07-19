package com.fonfon.customviews

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fonfon.customviews.hexagonProgress.HexagonProgressActivity
import com.fonfon.customviews.rume.MathRunesActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    runeViewButton.setOnClickListener {
      startActivity(Intent(this, MathRunesActivity::class.java))
    }

    hexagonProgress.setOnClickListener {
      startActivity(Intent(this, HexagonProgressActivity::class.java))
    }
  }
}
