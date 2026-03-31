package br.com.munnincrow.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataEditalParser {

    private static final DateTimeFormatter FORMATADOR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));

    // Regex para capturar datas dd/MM/yyyy
    private static final Pattern DATA = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

    public static LocalDate[] extrairDatas(String texto) {
        if (texto == null) return new LocalDate[]{null, null};

        texto = texto.replaceAll("\\s+", " ").trim().toLowerCase();

        // 1) Período com duas datas
        Matcher matcher = DATA.matcher(texto);
        LocalDate primeira = null;
        LocalDate segunda = null;

        if (matcher.find()) {
            primeira = parse(matcher.group());
        }
        if (matcher.find()) {
            segunda = parse(matcher.group());
        }

        if (primeira != null && segunda != null) {
            return new LocalDate[]{primeira, segunda};
        }

        // 2) Apenas data final (até, limite, final)
        if (texto.contains("até") || texto.contains("limite") || texto.contains("final")) {
            if (primeira != null) {
                return new LocalDate[]{null, primeira};
            }
        }

        return new LocalDate[]{null, null};
    }

    private static LocalDate parse(String data) {
        try {
            return LocalDate.parse(data, FORMATADOR);
        } catch (Exception e) {
            return null;
        }
    }
}