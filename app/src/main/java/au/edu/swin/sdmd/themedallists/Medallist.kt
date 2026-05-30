package au.edu.swin.sdmd.themedallists

data class Medallist(
    val country: String,
    val iocCode: String,
    val timesCompeted: Int,
    val gold: Int,
    val silver: Int,
    val bronze: Int
) {
    val totalMedals: Int
        get() = gold + silver + bronze

    val highestMedalType: String
        get() = when {
            gold > 0 -> "Gold"
            silver > 0 -> "Silver"
            bronze > 0 -> "Bronze"
            else -> "None"
        }
}