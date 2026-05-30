package au.edu.swin.sdmd.themedallists

/**
 * Helper object that converts IOC country codes to flag emojis.
 * It maps IOC codes to ISO 3166-1 alpha-2 codes, then converts
 * those to Unicode regional indicator symbols which render as flag emojis.
 */
object FlagHelper {

    // Maps IOC country codes to ISO 3166-1 alpha-2 codes
    // Historical nations are mapped to their closest modern successor state
    private val iocToIsoMap = mapOf(
        "AFG" to "AF", "ALB" to "AL", "ALG" to "DZ", "ASA" to "AS",
        "AND" to "AD", "ANG" to "AO", "ANT" to "AG", "ARG" to "AR",
        "ARM" to "AM", "ARU" to "AW", "AUS" to "AU", "AUT" to "AT",
        "AZE" to "AZ", "BAH" to "BS", "BRN" to "BH", "BAN" to "BD",
        "BAR" to "BB", "BLR" to "BY", "BEL" to "BE", "BIZ" to "BZ",
        "BEN" to "BJ", "BER" to "BM", "BHU" to "BT", "BOL" to "BO",
        "BIH" to "BA", "BOT" to "BW", "BRA" to "BR", "IVB" to "VG",
        "BRU" to "BN", "BUL" to "BG", "BUR" to "BF", "BDI" to "BI",
        "CAM" to "KH", "CMR" to "CM", "CAN" to "CA", "CPV" to "CV",
        "CAY" to "KY", "CAF" to "CF", "CHA" to "TD", "CHI" to "CL",
        "CHN" to "CN", "TPE" to "TW", "COL" to "CO", "COM" to "KM",
        "COK" to "CK", "CRC" to "CR", "CRO" to "HR", "CUB" to "CU",
        "CYP" to "CY", "CZE" to "CZ", "COD" to "CD", "DEN" to "DK",
        "DJI" to "DJ", "DMA" to "DM", "DOM" to "DO", "TLS" to "TL",
        "ECU" to "EC", "EGY" to "EG", "ESA" to "SV", "GEQ" to "GQ",
        "ERI" to "ER", "EST" to "EE", "ETH" to "ET", "FSM" to "FM",
        "FIJ" to "FJ", "FIN" to "FI", "FRA" to "FR", "GAB" to "GA",
        "GEO" to "GE", "GER" to "DE", "GHA" to "GH", "GBR" to "GB",
        "GRE" to "GR", "GRN" to "GD", "GUM" to "GU", "GUA" to "GT",
        "GUI" to "GN", "GBS" to "GW", "GUY" to "GY", "HAI" to "HT",
        "HON" to "HN", "HKG" to "HK", "HUN" to "HU", "ISL" to "IS",
        "IND" to "IN", "INA" to "ID", "IRI" to "IR", "IRQ" to "IQ",
        "IRL" to "IE", "ISR" to "IL", "ITA" to "IT", "CIV" to "CI",
        "JAM" to "JM", "JPN" to "JP", "JOR" to "JO", "KAZ" to "KZ",
        "KEN" to "KE", "KIR" to "KI", "KOS" to "XK", "KUW" to "KW",
        "KGZ" to "KG", "LAO" to "LA", "LAT" to "LV", "LIB" to "LB",
        "LES" to "LS", "LBR" to "LR", "LBA" to "LY", "LIE" to "LI",
        "LTU" to "LT", "LUX" to "LU", "MAD" to "MG", "MAW" to "MW",
        "MAS" to "MY", "MDV" to "MV", "MLI" to "ML", "MLT" to "MT",
        "MHL" to "MH", "MTN" to "MR", "MRI" to "MU", "MEX" to "MX",
        "MDA" to "MD", "MON" to "MC", "MGL" to "MN", "MNE" to "ME",
        "MAR" to "MA", "MOZ" to "MZ", "MYA" to "MM", "NAM" to "NA",
        "NRU" to "NR", "NEP" to "NP", "NED" to "NL", "NZL" to "NZ",
        "NCA" to "NI", "NIG" to "NE", "NGR" to "NG", "PRK" to "KP",
        "MKD" to "MK", "NOR" to "NO", "OMA" to "OM", "PAK" to "PK",
        "PLW" to "PW", "PLE" to "PS", "PAN" to "PA", "PNG" to "PG",
        "PAR" to "PY", "PER" to "PE", "PHI" to "PH", "POL" to "PL",
        "POR" to "PT", "PUR" to "PR", "QAT" to "QA", "CGO" to "CG",
        "ROU" to "RO", "RUS" to "RU", "RWA" to "RW", "SKN" to "KN",
        "LCA" to "LC", "VIN" to "VC", "SAM" to "WS", "SMR" to "SM",
        "STP" to "ST", "KSA" to "SA", "SEN" to "SN", "SRB" to "RS",
        "SEY" to "SC", "SLE" to "SL", "SIN" to "SG", "SVK" to "SK",
        "SLO" to "SI", "SOL" to "SB", "SOM" to "SO", "RSA" to "ZA",
        "KOR" to "KR", "SSD" to "SS", "ESP" to "ES", "SRI" to "LK",
        "SUD" to "SD", "SUR" to "SR", "SWZ" to "SZ", "SWE" to "SE",
        "SUI" to "CH", "SYR" to "SY", "TJK" to "TJ", "TAN" to "TZ",
        "THA" to "TH", "GAM" to "GM", "TOG" to "TG", "TGA" to "TO",
        "TTO" to "TT", "TUN" to "TN", "TUR" to "TR", "TKM" to "TM",
        "TUV" to "TV", "UGA" to "UG", "UKR" to "UA", "UAE" to "AE",
        "USA" to "US", "URU" to "UY", "UZB" to "UZ", "VAN" to "VU",
        "VEN" to "VE", "VIE" to "VN", "ISV" to "VI", "YEM" to "YE",
        "ZAM" to "ZM", "ZIM" to "ZW",

        // Historical and dissolved nations mapped to successor states
        "ANZ" to "AU", // Australasia → Australia
        "BOH" to "CZ", // Bohemia → Czech Republic
        "BWI" to "JM", // British West Indies → Jamaica
        "GDR" to "DE", // East Germany → Germany
        "FRG" to "DE", // West Germany → Germany
        "TCH" to "CZ", // Czechoslovakia → Czech Republic
        "URS" to "RU", // Soviet Union → Russia
        "YUG" to "RS", // Yugoslavia → Serbia
        "EUN" to "RU", // Unified Team → Russia
        "EUA" to "DE", // United Team of Germany → Germany
        "RU1" to "RU", // Russian Empire → Russia
        "SCG" to "RS", // Serbia and Montenegro → Serbia
        "AHO" to "NL", // Netherlands Antilles → Netherlands
        "MAL" to "MY", // Malaya → Malaysia
        "NBO" to "MY", // North Borneo → Malaysia
        "YAR" to "YE", // North Yemen → Yemen
        "YMD" to "YE", // South Yemen → Yemen
        "ROC" to "TW", // Republic of China → Taiwan
        "COR" to "KR", // Korea → South Korea
        "SAA" to "DE", // Saar → Germany
        "OAR" to "RU"  // Olympic Athletes from Russia → Russia
    )

    /**
     * Converts an IOC code to a flag emoji string.
     * Returns null if the IOC code has no matching ISO country code
     * (e.g. for special teams like Mixed team or Refugee Olympic Team).
     */
    fun getFlagEmoji(iocCode: String): String? {
        val isoCode = iocToIsoMap[iocCode] ?: return null
        // Each letter is converted to a regional indicator Unicode symbol
        // 'A' maps to U+1F1E6, 'B' to U+1F1E7, etc.
        val first = Character.toChars(0x1F1E6 + (isoCode[0].uppercaseChar() - 'A'))
        val second = Character.toChars(0x1F1E6 + (isoCode[1].uppercaseChar() - 'A'))
        return String(first) + String(second)
    }
}