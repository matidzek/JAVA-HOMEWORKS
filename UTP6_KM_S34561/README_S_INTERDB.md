# Zadanie: S_INTERDB

> **Render:** HTML &nbsp;|&nbsp; **Charset:** CP1250

---

## Opis zadania

Biuro podróży otrzymuje od różnych kontrahentów (polskich, angielskich, niemieckich…) pliki z ofertami wyjazdów/wycieczek.

Każda oferta zapisana jest w jednym wierszu pliku i zawiera pola rozdzielone znakami tabulacji:

```
lokalizacja_kontrahenta  kraj  date_wyjazdu  date_powrotu  miejsce  cena  symbol_waluty
```

### Opis pól

| Pole                    | Opis |
|-------------------------|------|
| `lokalizacja_kontrahenta` | Napis oznaczający język_kraj (np. `pl_PL`, `en_US`) – format zgodny z `Locale.toString()` |
| `kraj`                  | Nazwa kraju w języku kontrahenta |
| `date_wyjazdu`          | Data w formacie `RRRR-MM-DD` (np. `2015-12-31`) |
| `date_powrotu`          | Data w formacie `RRRR-MM-DD` |
| `miejsce`               | Jedno z: `morze`, `jezioro`, `góry` – w języku kontrahenta |
| `cena`                  | Liczba w formacie obowiązującym w kraju kontrahenta |
| `symbol_waluty`         | Kod waluty ISO, np. `PLN`, `USD` |

---

## Wymagania aplikacji

1. **Wczytywanie danych** – dodaje zawartość plików ofert do bazy danych (dowolny silnik bazodanowy).
2. **Internacjonalizacja** – prezentuje klientowi w tabeli `JTable` pełny zestaw ofert w języku i ustawieniach regionalnych wybranych przez użytkownika.
3. **Lokalizacje testowe** – wybrać dwie–trzy lokalizacje do testowania; pliki kontrahentów dostarczyć w podkatalogu `data` projektu.

---

## Wymagane klasy

### `TravelData`

Musi zawierać metodę:

```java
List<String> travelData.getOffersDescriptionsList(String loc, String dateFormat)
```

Metoda zwraca listę napisów, gdzie każdy napis to opis jednej oferty wczytanej z plików katalogu `data`, przedstawiony:
- w języku i zgodnie z regułami lokalizacji `loc`,
- z datami sformatowanymi według podanego `dateFormat` (formaty zgodne z `SimpleDateFormat`).

### `Database`

- `createDb()` – tworzy bazę danych i wpisuje do niej wszystkie oferty wczytane z plików.
- `showGui()` – otwiera GUI z tabelą pokazującą oferty; GUI umożliwia wybór języka i ustawień regionalnych.

> **Uwaga:** Wszelkie operacje bazodanowe mogą być przeprowadzane **tylko** w klasie `Database`.

---

## Klasa `Main`

Poniższy kod jest modyfikowalny **wyłącznie w miejscach zaznaczonych komentarzem** `/*<-- */`:

```java
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        File dataDir = new File("data");
        TravelData travelData = new TravelData(dataDir);
        String dateFormat = "yyyy-MM-dd";
        for (String locale : Arrays.asList("pl_PL", "en_GB")) {
            List<String> odlist = travelData.getOffersDescriptionsList(locale, dateFormat);
            for (String od : odlist) System.out.println(od);
        }
        // --- część bazodanowa
        String url = /*<-- tu należy wpisać URL bazy danych */
        Database db = new Database(url, travelData);
        db.create();
        db.showGui();
    }
}
```

---

## Przykładowe dane wejściowe

Pliki w katalogu `data/` zawierają m.in.:

```
pl	Japonia	2015-09-01	2015-10-01	jezioro	10000,20	PLN
pl_PL	Włochy	2015-07-10	2015-07-30	morze	4000,10	PLN
en_GB	United States	2015-07-10	2015-08-30	mountains	5,400.20	USD
```

---

## Oczekiwane wyjście na konsolę

**Dla lokalizacji `pl_PL`:**

```
Japonia 2015-09-01 2015-10-01 jezioro 10 000,2 PLN
Włochy 2015-07-10 2015-07-30 morze 4 000,1 PLN
Stany Zjednoczone Ameryki 2015-07-10 2015-08-30 góry 5 400,2 USD
```

**Dla lokalizacji `en_GB`:**

```
Japan 2015-09-01 2015-10-01 lake 10,000.2 PLN
Italy 2015-07-10 2015-07-30 sea 4,000.1 PLN
United States 2015-07-10 2015-08-30 mountains 5,400.2 USD
```

Po wydrukowaniu ofert aplikacja tworzy bazę danych i wyświetla GUI.

---

## Uwagi implementacyjne

- Nazwy krajów i miejsca (`morze`/`sea`, `jezioro`/`lake`, `góry`/`mountains`) muszą być tłumaczone na język docelowej lokalizacji.
- Ceny należy formatować zgodnie z ustawieniami liczbowymi lokalizacji docelowej (separator tysięcy i dziesiętny).
- Daty są prezentowane w formacie podanym jako argument `dateFormat`.
- Nazwy krajów można uzyskać programistycznie z klasy `Locale` (np. `new Locale("", "US").getDisplayCountry(targetLocale)`).
- Kodowanie plików wynikowych: **CP1250**.
