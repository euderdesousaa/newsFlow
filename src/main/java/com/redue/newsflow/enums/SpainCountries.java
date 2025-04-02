package com.redue.newsflow.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum SpainCountries {
    SPAIN("ES"),
    ARGENTINA("AR"),
    COLOMBIA("CO"),
    CHILE("CL"),
    MEXICO("MX"),
    PERU("PE"),
    VENEZUELA("VE"),
    ECUADOR("EC"),
    GUATEMALA("GT"),
    CUBA("CU"),
    DOMINICAN_REPUBLIC("DO"),
    BOLIVIA("BO"),
    HONDURAS("HN"),
    PARAGUAY("PY"),
    EL_SALVADOR("SV"),
    NICARAGUA("NI"),
    COSTA_RICA("CR"),
    PANAMA("PA"),
    URUGUAY("UY");

    private final String isoCode;

    SpainCountries(String isoCode) {
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

    public static Set<String> getAllIsoCodes() {
        return Arrays.stream(values())
                .map(SpainCountries::getIsoCode)
                .collect(Collectors.toSet());
    }
}
