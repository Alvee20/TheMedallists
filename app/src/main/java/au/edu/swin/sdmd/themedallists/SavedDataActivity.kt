package au.edu.swin.sdmd.themedallists

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SavedDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_data)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.saved_data_title)

        val nameText = findViewById<TextView>(R.id.textSavedName)
        val iocCodeText = findViewById<TextView>(R.id.textSavedIocCode)
        val timesCompetedText = findViewById<TextView>(R.id.textSavedTimesCompeted)
        val medalsText = findViewById<TextView>(R.id.textSavedMedals)
        val medalIcon = findViewById<ImageView>(R.id.imageSavedMedal)
        val flagText = findViewById<TextView>(R.id.textSavedFlag)

        val prefs = getSharedPreferences("medallist_prefs", MODE_PRIVATE)
        val country = prefs.getString("last_country", null)

        if (country != null) {
            val iocCode = prefs.getString("last_ioc_code", "") ?: ""
            val timesCompeted = prefs.getInt("last_times_competed", 0)
            val gold = prefs.getInt("last_gold", 0)
            val silver = prefs.getInt("last_silver", 0)
            val bronze = prefs.getInt("last_bronze", 0)

            nameText.text = country
            iocCodeText.text = "IOC Code: $iocCode"
            timesCompetedText.text = "Competed $timesCompeted times"
            medalsText.text = "Gold: $gold  |  Silver: $silver  |  Bronze: $bronze"

            val flagEmoji = FlagHelper.getFlagEmoji(iocCode)
            if (flagEmoji != null) {
                flagText.text = flagEmoji
                flagText.visibility = View.VISIBLE
            } else {
                flagText.visibility = View.GONE
            }

            val highestType = when {
                gold > 0 -> "Gold"
                silver > 0 -> "Silver"
                bronze > 0 -> "Bronze"
                else -> "None"
            }
            val medalColor = when (highestType) {
                "Gold" -> ContextCompat.getColor(this, R.color.gold)
                "Silver" -> ContextCompat.getColor(this, R.color.silver)
                "Bronze" -> ContextCompat.getColor(this, R.color.bronze)
                else -> ContextCompat.getColor(this, R.color.silver)
            }
            val background = medalIcon.background
            if (background is GradientDrawable) {
                background.setColor(medalColor)
            }
        } else {
            nameText.text = getString(R.string.no_saved_data)
            iocCodeText.text = ""
            timesCompetedText.text = ""
            medalsText.text = ""
            flagText.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}