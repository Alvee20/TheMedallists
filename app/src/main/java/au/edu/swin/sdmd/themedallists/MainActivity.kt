package au.edu.swin.sdmd.themedallists

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedallistAdapter
    private var medallistList = mutableListOf<Medallist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerMedallists)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Read data from the CSV file in res/raw
        medallistList = readMedallistsFromCsv()

        // Create the adapter with a click listener
        adapter = MedallistAdapter(medallistList) { medallist ->
            onMedallistClicked(medallist)
        }
        recyclerView.adapter = adapter
    }

    /**
     * Reads the medallists.csv file from the res/raw folder.
     * The file is read-only because it lives inside the APK's resources.
     * CSV columns: Country, IOC code, Times competed, Gold, Silver, Bronze
     */
    private fun readMedallistsFromCsv(): MutableList<Medallist> {
        val list = mutableListOf<Medallist>()
        val inputStream = resources.openRawResource(R.raw.medallists)
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Skip the header row
        reader.readLine()

        reader.forEachLine { line ->
            val tokens = line.split(",")
            // Expecting: Country, IOC code, Times competed, Gold, Silver, Bronze
            if (tokens.size >= 6) {
                val medallist = Medallist(
                    country = tokens[0].trim(),
                    iocCode = tokens[1].trim(),
                    timesCompeted = tokens[2].trim().toIntOrNull() ?: 0,
                    gold = tokens[3].trim().toIntOrNull() ?: 0,
                    silver = tokens[4].trim().toIntOrNull() ?: 0,
                    bronze = tokens[5].trim().toIntOrNull() ?: 0
                )
                list.add(medallist)
            }
        }
        reader.close()
        return list
    }

    /**
     * Called when a country row is clicked.
     * Shows a Toast and Snackbar, then saves the data to SharedPreferences.
     */
    private fun onMedallistClicked(medallist: Medallist) {
        // Show a Toast with the country name
        Toast.makeText(this, "${medallist.country} selected", Toast.LENGTH_SHORT).show()

        // Show a Snackbar with the country's medal details
        val message = "${medallist.country}: ${medallist.gold}G ${medallist.silver}S ${medallist.bronze}B"
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()

        // Save to SharedPreferences so we can view it later
        saveLastClicked(medallist)
    }

    /**
     * Persists the last clicked country using SharedPreferences.
     * This data survives app restarts.
     */
    private fun saveLastClicked(medallist: Medallist) {
        val prefs = getSharedPreferences("medallist_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("last_country", medallist.country)
            putString("last_ioc_code", medallist.iocCode)
            putInt("last_times_competed", medallist.timesCompeted)
            putInt("last_gold", medallist.gold)
            putInt("last_silver", medallist.silver)
            putInt("last_bronze", medallist.bronze)
            apply()
        }
    }

    // Inflate the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_saved_data -> {
                // Open the SavedDataActivity
                val intent = Intent(this, SavedDataActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}