package com.fonfon.customviews.hexagonProgress

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fonfon.customviews.R
import kotlinx.android.synthetic.main.activity_hexagon_progress.*

class HexagonProgressActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hexagon_progress)
  }

  override fun onResume() {
    super.onResume()
    hexagonProgress.start()
  }

  override fun onPause() {
    super.onPause()
    hexagonProgress.stop()
  }
}
