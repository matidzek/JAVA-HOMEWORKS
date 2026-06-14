package zad1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TravelData {

    private static final int LOC = 0, COUNTRY = 1, DEP = 2, RET = 3, PLACE = 4, PRICE = 5, CUR = 6;

    private static final String INPUT_DATE = "yyyy-MM-dd";

    //indeks 0=morze, 1=jezioro, 2=gory. dla kazdego jezyka
    private static final Map<String, String[]> PLACES = new HashMap<>();
    static {
        PLACES.put("pl", new String[]{"morze", "jezioro", "góry"});
        PLACES.put("en", new String[]{"sea", "lake", "mountains"});
        PLACES.put("de", new String[]{"Meer", "See", "Berge"});
    }

    private final List<String[]> offers = new ArrayList<>();

    public TravelData(File dataDir) {
        File[] files = dataDir.listFiles();
        if (files == null) return;
        Arrays.sort(files);
        for (File f : files) {
            if (!f.isFile()) continue;
            try {
                for (String line : Files.readAllLines(f.toPath(), StandardCharsets.UTF_8)) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split("\t");
                    if (parts.length >= 7) offers.add(parts);
                }
            } catch (IOException exc) {
                throw new RuntimeException("Nie moge wczytac pliku " + f + ": " + exc.getMessage(), exc);
            }
        }
    }

    // Surowe oferty (7 pol kazda) - uzywane przez klase Database do zapisu w BD
    public List<String[]> getOffers() {
        return offers;
    }

    //Wymagana metoda: opisy wszystkich ofert w jezyku/lokalizacji loc i z podanym formatem daty
    public List<String> getOffersDescriptionsList(String loc, String dateFormat) {
        Locale target = toLocale(loc);
        List<String> out = new ArrayList<>();
        for (String[] raw : offers) {
            out.add(String.join(" ", localizeFields(raw, target, dateFormat)));
        }
        return out;
    }

    //Tlumaczy pojedyncza oferte na docelowa lokalizacje. Zwraca 6 pol: kraj, data_wyjazdu, data_powrotu, miejsce, cena, waluta
    public String[] localizeFields(String[] raw, String loc, String dateFormat) {
        return localizeFields(raw, toLocale(loc), dateFormat);
    }

    private String[] localizeFields(String[] raw, Locale target, String dateFormat) {
        Locale src = toLocale(raw[LOC]);
        return new String[]{
                mapCountry(raw[COUNTRY], src, target),
                reformatDate(raw[DEP], dateFormat, target),
                reformatDate(raw[RET], dateFormat, target),
                mapPlace(raw[PLACE], src, target),
                reformatPrice(raw[PRICE], src, target),
                raw[CUR]
        };
    }


    @SuppressWarnings("deprecation")
    static String mapCountry(String name, Locale src, Locale dst) {
        for (String code : Locale.getISOCountries()) {
            Locale c = new Locale("", code);
            if (c.getDisplayCountry(src).equalsIgnoreCase(name)) {
                return c.getDisplayCountry(dst);
            }
        }
        return name; // gdy nie rozpoznano - zostawiamy jak bylo
    }

    static String mapPlace(String word, Locale src, Locale dst) {
        String[] from = PLACES.get(src.getLanguage());
        String[] to = PLACES.get(dst.getLanguage());
        if (from != null && to != null) {
            for (int i = 0; i < from.length; i++) {
                if (from[i].equalsIgnoreCase(word.trim())) return to[i];
            }
        }
        return word;
    }

    static String reformatDate(String raw, String dateFormat, Locale dst) {
        try {
            Date d = new SimpleDateFormat(INPUT_DATE).parse(raw.trim());
            return new SimpleDateFormat(dateFormat, dst).format(d);
        } catch (ParseException exc) {
            throw new RuntimeException("Zla data: '" + raw + "'", exc);
        }
    }

    static String reformatPrice(String raw, Locale src, Locale dst) {
        try {
            double v = NumberFormat.getInstance(src).parse(raw.trim()).doubleValue();
            return NumberFormat.getInstance(dst).format(v);
        } catch (ParseException exc) {
            throw new RuntimeException("Zla cena: '" + raw + "'", exc);
        }
    }

    static Locale toLocale(String s) {
        String[] p = s.split("_");
        switch (p.length) {
            case 1:  return new Locale(p[0]);
            case 2:  return new Locale(p[0], p[1]);
            default: return new Locale(p[0], p[1], p[2]);
        }
    }
}