package com.redue.newsflow.enums;

import lombok.*;

@Getter
public enum RegionLanguage {
    AR("es"),            // Argentina
    AM("hy"),            // Armênia
    AU("en"),            // Austrália
    AT("de"),            // Áustria
    BY("be,ru"),         // Belarus
    BE("nl,fr,de"),      // Bélgica
    BO("es"),            // Bolívia
    BR("pt"),            // Brasil
    BG("bg"),            // Bulgária
    CA("en,fr"),         // Canadá
    CL("es"),            // Chile
    CN("zh"),            // China
    CO("es"),            // Colômbia
    HR("hr"),            // Croácia
    CZ("cs"),            // República Tcheca
    EC("es"),            // Equador
    EG("ar,en"),         // Egito
    FR("fr"),            // França
    DE("de"),            // Alemanha
    GR("el"),            // Grécia
    HN("es"),            // Honduras
    HK("zh,en"),         // Hong Kong
    IN("hi,en"),         // Índia
    ID("id"),            // Indonésia
    IR("fa"),            // Irã
    IE("en"),            // Irlanda
    IL("he"),            // Israel
    IT("it"),            // Itália
    JP("ja"),            // Japão
    KR("ko"),            // Coreia do Sul
    MX("es"),            // México
    NL("nl"),            // Países Baixos
    NZ("en"),            // Nova Zelândia
    NI("es"),            // Nicarágua
    PK("ur,en"),         // Paquistão
    PA("es"),            // Panamá
    PE("es"),            // Peru
    PL("pl"),            // Polônia
    PT("pt"),            // Portugal
    QA("ar,en"),         // Catar
    RO("ro"),            // Romênia
    RU("ru"),            // Rússia
    SA("ar,en"),         // Arábia Saudita
    ZA("en"),            // África do Sul
    ES("es"),            // Espanha
    CH("de,fr,it"),      // Suíça
    SY("ar,en"),         // Síria
    TW("zh"),            // Taiwan
    TH("th"),            // Tailândia
    TR("tr"),            // Turquia
    UA("uk,ru"),         // Ucrânia
    GB("en"),            // Reino Unido
    US("en"),            // Estados Unidos
    UY("es"),            // Uruguai
    VE("es");            // Venezuela

    private final String language;

    RegionLanguage(String language) {
        this.language = language;
    }

    public static String getLanguageForRegion(String isoCode) {
        for (RegionLanguage region : values()) {
            if (region.name().equalsIgnoreCase(isoCode)) {
                return region.getLanguage();
            }
        }
        return "en";
    }
}