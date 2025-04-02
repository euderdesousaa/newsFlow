package com.redue.newsflow.enums;

import java.util.Arrays;

public enum FranceCountries {
    CANADA("CA"),
    FRANCE("FR"),
    BELGIUM("BE"),
    SWITZERLAND("CH"),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO("CD"),
    BENIN("BJ"),
    BURKINA_FASO("BF"),
    BURUNDI("BI"),
    CAMEROON("CM"),
    CHAD("TD"),
    COMOROS("KM"),
    CONGO("CG"),
    IVORY_COAST("CI"),
    DJIBOUTI("DJ"),
    GABON("GA"),
    GUINEA("GN"),
    EQUATORIAL_GUINEA("GQ"),
    HAITI("HT"),
    LUXEMBOURG("LU"),
    MADAGASCAR("MG"),
    MALI("ML"),
    MOROCCO("MA"),
    NIGER("NE"),
    CENTRAL_AFRICAN_REPUBLIC("CF"),
    RWANDA("RW"),
    SENEGAL("SN"),
    SEYCHELLES("SC"),
    TOGO("TG"),
    VANUATU("VU");
    
    private final String isoCode;

    FranceCountries(String isoCode) {
        this.isoCode = isoCode;
    }

    public static boolean isSpanishSpeaking(String isoCode) {
        if (isoCode == null || isoCode.isBlank()) {
            return false;
        }
        String normalized = isoCode.toUpperCase();
        return Arrays.stream(values())
                .anyMatch(country -> country.isoCode.equals(normalized));
    }
}
