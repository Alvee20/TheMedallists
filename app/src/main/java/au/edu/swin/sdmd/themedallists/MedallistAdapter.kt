package au.edu.swin.sdmd.themedallists

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MedallistAdapter(
    private val medallists: List<Medallist>,
    private val onItemClick: (Medallist) -> Unit
) : RecyclerView.Adapter<MedallistAdapter.MedallistViewHolder>() {

    // ViewHolder holds references to views in each row
    inner class MedallistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.textName)
        val subtitleText: TextView = itemView.findViewById(R.id.textSubtitle)
        val medalIcon: ImageView = itemView.findViewById(R.id.imageMedal)
        val medalCount: TextView = itemView.findViewById(R.id.textMedalCount)
        val flagText: TextView = itemView.findViewById(R.id.textFlag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedallistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medallist, parent, false)
        return MedallistViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedallistViewHolder, position: Int) {
        val medallist = medallists[position]

        // Set the main text (country name) and subtitle (IOC code + times competed)
        holder.nameText.text = medallist.country
        holder.subtitleText.text = "${medallist.iocCode} \u2013 Competed ${medallist.timesCompeted} times"
        holder.medalCount.text = "${medallist.totalMedals} medals"

        // Determine the medal colour based on the country's highest medal type
        val medalColor = when (medallist.highestMedalType) {
            "Gold" -> ContextCompat.getColor(holder.itemView.context, R.color.gold)
            "Silver" -> ContextCompat.getColor(holder.itemView.context, R.color.silver)
            "Bronze" -> ContextCompat.getColor(holder.itemView.context, R.color.bronze)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.silver)
        }

        // Apply the colour to the circle icon background
        val background = holder.medalIcon.background
        if (background is GradientDrawable) {
            background.setColor(medalColor)
        }

        // Show flag emoji on top of the medal circle if available
        val flagEmoji = FlagHelper.getFlagEmoji(medallist.iocCode)
        if (flagEmoji != null) {
            holder.flagText.text = flagEmoji
            holder.flagText.visibility = View.VISIBLE
        } else {
            // Historical teams like Soviet Union won't have a flag
            holder.flagText.visibility = View.GONE
        }

        // Apply a subtle row background tint based on medal type
        val bgColor = when (medallist.highestMedalType) {
            "Gold" -> ContextCompat.getColor(holder.itemView.context, R.color.gold_bg)
            "Silver" -> ContextCompat.getColor(holder.itemView.context, R.color.silver_bg)
            "Bronze" -> ContextCompat.getColor(holder.itemView.context, R.color.bronze_bg)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.silver_bg)
        }
        holder.itemView.setBackgroundColor(bgColor)

        // Set click listener for this row
        holder.itemView.setOnClickListener {
            onItemClick(medallist)
        }
    }

    override fun getItemCount(): Int = medallists.size
}