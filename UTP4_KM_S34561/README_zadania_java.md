# Zadania – Java Streams, Parsowanie kodu, Operacje na plikach

---

## Zadanie 1 – Anagramy (Java Streams)

### Opis

Na liście słów z:

```
http://wiki.puzzlers.org/pub/wordlists/unixdict.txt
```

znaleźć wszystkie anagramy. Wypisać słowa z **maksymalną liczbą anagramów** oraz wszystkie ich anagramy w postaci:

```
slowo anagram1 anagram2 ...
```

### Wymagania

- Program ma być **bardzo krótki** dzięki zastosowaniu przetwarzania strumieniowego (`java.util.stream`).
- **Brak użycia strumieni = 0 punktów.**

---

## Zadanie 2 – Finder (6 punktów)

### Opis

Napisać program, który wczytuje plik `Test.java` z katalogu `{user.home}` (z poprawnym składniowo kodem źródłowym Javy) i wyszukuje w nim:
- instrukcje `if`,
- napisy `"wariant"`.

### Oczekiwane wyjście na konsolę

```
Liczba instrukcji if: n
Liczba napisow wariant: m
```

### Klasa `Main` (niemodyfikowalna)

```java
public class Main {

    public static void main(String... args) throws Exception {
        String home = System.getProperty("user.home");
        Finder finder = new Finder(home + "/Test.java");
        int nif = finder.getIfCount();
        int nwar = finder.getStringCount("wariant");
        System.out.println("Liczba instrukcji if: " + nif);
        System.out.println("Liczba napisow wariant: " + nwar);
    }
}
```

### Uwagi

1. Wszystkie klasy w programie muszą być **publiczne** (w oddzielnych plikach).
2. Klasy `Main` **nie wolno** w żaden sposób modyfikować.
3. Plik `Test.java` musi znajdować się w katalogu zwracanym przez:
   ```java
   System.getProperty("user.home");
   ```
4. Plik tworzymy sobie sami.
5. **Nie dołączać** pliku `Test.java` do projektu.
6. Forma wydruku na konsoli jest **obowiązkowa**.

### Wskazówka implementacyjna

Instrukcja `if` ma ściśle określone wymagania składniowe. **Nie jest** instrukcją `if` coś, co wygląda jak `if`, ale jest umieszczone w:
- komentarzu (liniowym `//` lub blokowym `/* */`),
- literale napisowym (`"..."`).

---

## Zadanie 3 – Futil (przetwarzanie plików)

### Opis

Katalog `{user.home}/UTP6dir` zawiera pliki tekstowe (rozszerzenie `.txt`) umieszczone w różnych podkatalogach. Kodowanie plików: **CP1250**.

Przeglądając **rekursywnie** drzewo katalogowe od `{user.home}/UTP6dir`, wczytać wszystkie pliki `.txt` i połączoną ich zawartość zapisać do pliku `UTP6res.txt` znajdującego się w katalogu projektu. Kodowanie pliku wynikowego: **UTF-8**.

### Klasa `Main` (niemodyfikowalna)

```java
public class Main {

    public static void main(String[] args) {
        String dirName = System.getProperty("user.home") + "/UTP6dir";
        String resultFileName = "UTP6res.txt";
        Futil.processDir(dirName, resultFileName);
    }
}
```

### Wymagania

- Pliku `Main.java` **nie wolno** modyfikować.
- Dostarczyć definicji klasy `Futil`.
- Nazwa katalogu (`UTP6dir`), nazwa pliku wynikowego (`UTP6res.txt`) oraz ich położenie są **obowiązkowe**.
- Do przeglądania katalogu należy zastosować **`FileVisitor`**.
- **Nie stosować** przetwarzania strumieniowego (`java.util.stream`).
- Zakładamy, że na starcie programu plik wynikowy **nie istnieje**.
