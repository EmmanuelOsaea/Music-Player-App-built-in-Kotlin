package com.example.musicplayer.ui

import android.media.audiofx.Equalizer
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R

class EqualizerActivity : AppCompatActivity() {

    private lateinit var equalizer: Equalizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)

        equalizer = Equalizer(0, 0)
        equalizer.enabled = true

        val eqContainer = findViewById<LinearLayout>(R.id.equalizerContainer)
        val bands = equalizer.numberOfBands
        val range = equalizer.bandLevelRange

        for (i in 0 until bands) {
            val bandIndex = i.toShort()
            val freq = equalizer.getCenterFreq(bandIndex) / 1000
            val label = TextView(this).apply {
                text = "${freq}Hz"
                setTextColor(resources.getColor(android.R.color.white))
            }
            val seekBar = SeekBar(this).apply {
                max = range[1] - range[0]
                progress = equalizer.getBandLevel(bandIndex) - range[0]
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                        equalizer.setBandLevel(bandIndex, (progress + range[0]).toShort())
                    }
                    override fun onStartTrackingTouch(sb: SeekBar?) {}
                    override fun onStopTrackingTouch(sb: SeekBar?) {}
                })
            }
            eqContainer.addView(label)
            eqContainer.addView(seekBar)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        equalizer.release()
    }
}
