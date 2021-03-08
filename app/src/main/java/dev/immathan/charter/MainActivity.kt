package dev.immathan.charter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dev.chrisbanes.insetter.applyInsetter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //Insetter.setEdgeToEdgeSystemUiFlags(window.decorView, true)
        window.decorView.applyInsetter {
            // Apply the navigation bar insets...

            // Apply the status bar insets...
            type(statusBars = true) {
                // Add to margin on all sides
                padding()
            }
        }


        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.refresh).setOnClickListener {
            (findViewById<View>(R.id.chartView) as ChartView).refresh()
        }
    }
}