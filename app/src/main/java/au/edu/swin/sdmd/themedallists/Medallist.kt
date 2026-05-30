package au.edu.swin.sdmd.themedallists

// Data class representing a country's Olympic medal record
data class Medallist(
    val country: String,
    val iocCode: String,
    val timesCompeted: Int,
    val gold: Int,
    val silver: Int,
    val bronze: Int
) {
    // Computed property to get total medal count
    val totalMedals: Int
        get() = gold + silver + bronze

    // Determines the highest medal type this country has earned
    val highestMedalType: String
        get() = when {
            gold > 0 -> "Gold"
            silver > 0 -> "Silver"
            bronze > 0 -> "Bronze"
            else -> "None"
        }
}