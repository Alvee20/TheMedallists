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

        holder.nameText.text = medallist.country
        holder.subtitleText.text = "${medallist.iocCode} \u2013 Competed ${medallist.timesCompeted} times"
        holder.medalCount.text = "${medallist.totalMedals} medals"

        val medalColor = when (medallist.highestMedalType) {
            "Gold" -> ContextCompat.getColor(holder.itemView.context, R.color.gold)
            "Silver" -> ContextCompat.getColor(holder.itemView.context, R.color.silver)
            "Bronze" -> ContextCompat.getColor(holder.itemView.context, R.color.bronze)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.silver)
        }

        val background = holder.medalIcon.background
        if (background is GradientDrawable) {
            background.setColor(medalColor)
        }

        val flagEmoji = FlagHelper.getFlagEmoji(medallist.iocCode)
        if (flagEmoji != null) {
            holder.flagText.text = flagEmoji
            holder.flagText.visibility = View.VISIBLE
        } else {
            holder.flagText.visibility = View.GONE
        }

        val bgColor = when (medallist.highestMedalType) {
            "Gold" -> ContextCompat.getColor(holder.itemView.context, R.color.gold_bg)
            "Silver" -> ContextCompat.getColor(holder.itemView.context, R.color.silver_bg)
            "Bronze" -> ContextCompat.getColor(holder.itemView.context, R.color.bronze_bg)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.silver_bg)
        }
        holder.itemView.setBackgroundColor(bgColor)

        holder.itemView.setOnClickListener {
            onItemClick(medallist)
        }
    }

    override fun getItemCount(): Int = medallists.size
}