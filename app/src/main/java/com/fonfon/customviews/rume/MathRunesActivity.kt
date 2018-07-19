package com.fonfon.customviews.rume

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.fonfon.customviews.R
import kotlinx.android.synthetic.main.activity_rune.*

class MathRunesActivity : AppCompatActivity() {

  var mult = 0
  var add = 0

  private val function = object : RuneView.Function() {
    override fun function(value: Int): Int {
      return value * mult + add
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_rune)

    runeView.func = function

    seek_count.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        text_count.text = progress.toString()
        runeView.count = progress
        seek_mult.max = progress
        seek_add.max = progress
      }
    })

    seek_mult.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        text_mult.text = progress.toString()
        mult = progress
        runeView.invalidate()
      }
    })

    seek_add.setOnSeekBarChangeListener(object : SimpleProgressChangeListener() {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        text_add.text = progress.toString()
        add = progress
        runeView.invalidate()
      }
    })

    check_ark.setOnCheckedChangeListener { _, b -> runeView.style = if (b) RuneView.Style.ARK else RuneView.Style.LINEAR }
  }

  private open class SimpleProgressChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
  }

}
