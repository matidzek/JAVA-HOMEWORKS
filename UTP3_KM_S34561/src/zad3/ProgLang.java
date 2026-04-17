package zad3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {

    private final Map<String, Collection<String>> langsMap = new LinkedHashMap<>();
    private final Map<String, Collection<String>> progsMap = new LinkedHashMap<>();

    public ProgLang(String nazwaPliku) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(nazwaPliku));

        for (String line : lines) {
            String[] parts = line.split("\t");
            String lang = parts[0];
            Collection<String> progsForLang = langsMap.computeIfAbsent(lang, k -> new LinkedHashSet<>());

            for (int i = 1; i < parts.length; i++) {
                String prog = parts[i];
                progsForLang.add(prog);
                Collection<String> langsForProg = progsMap.computeIfAbsent(prog, k -> new LinkedHashSet<>());
                langsForProg.add(lang);
            }
        }
    }

    public Map<String, Collection<String>> getLangsMap() {
        return langsMap;
    }

    public Map<String, Collection<String>> getProgsMap() {
        return progsMap;
    }

    public Map<String, Collection<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(langsMap, (e1, e2) -> {
            int sizeCompare = Integer.compare(e2.getValue().size(), e1.getValue().size());
            if (sizeCompare != 0) return sizeCompare;
            return e1.getKey().compareTo(e2.getKey());
        });
    }

    public Map<String, Collection<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(progsMap, (e1, e2) -> {
            int sizeCompare = Integer.compare(e2.getValue().size(), e1.getValue().size());
            if (sizeCompare != 0)  return sizeCompare;
            return e1.getKey().compareTo(e2.getKey());
        });
    }

    public Map<String, Collection<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        return filtered(progsMap, entry -> entry.getValue().size() > n);
    }
    public static <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return map.entrySet().stream()
                .sorted(comparator).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public static <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        return map.entrySet().stream().filter(predicate).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}