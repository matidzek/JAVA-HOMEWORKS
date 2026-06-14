# Zadania – Java: Klasa Maybe, Kompozycja Funkcji

---

## Zadanie 1 – Klasa `Maybe`

### Opis

Zdefiniować klasę `Maybe<T>` reprezentującą kontener, który może zawierać lub nie pojedynczą wartość dowolnego typu referencyjnego.

**Motywacja:** ułatwienie bezpiecznego programowania w sytuacjach, gdy zmienna może mieć wartość `null` – szczególnie przy dalszym przetwarzaniu z użyciem lambda-wyrażeń, bez ryzyka `NullPointerException`.

> Obiekty `Maybe` nigdy nie powinny wewnętrznie przechowywać wartości `null` – albo zawierają wartość, albo są puste.

### Metody klasy `Maybe`

| Metoda | Opis |
|--------|------|
| `static Maybe<T> of(T x)` | Zwraca obiekt `Maybe` opakowujący wartość `x`; jeśli `x == null` – zwraca pusty `Maybe` |
| `void ifPresent(Consumer<T> cons)` | Jeśli wartość jest obecna – wykonuje `cons` z tą wartością; w przeciwnym razie nic nie robi |
| `Maybe<U> map(Function<T,U> func)` | Jeśli wartość jest obecna – stosuje `func` i zwraca wynik opakowany w nowy `Maybe`; jeśli wynik `func` to `null` lub `Maybe` jest pusty – zwraca pusty `Maybe` |
| `T get()` | Zwraca zawartość; jeśli `Maybe` jest pusty – rzuca `NoSuchElementException` |
| `boolean isPresent()` | Zwraca `true` jeśli wartość jest obecna, `false` jeśli `Maybe` jest pusty |
| `T orElse(T defVal)` | Zwraca zawartość lub `defVal`, jeśli `Maybe` jest pusty |
| `Maybe<T> filter(Predicate<T> pred)` | Zwraca to samo `Maybe`, jeśli warunek `pred` jest spełniony lub `Maybe` jest pusty; w przeciwnym razie zwraca pusty `Maybe` |

### Klasa `Main`

```java
public class Main {

    public static void test() {
        // Metoda of(...)
        String s = "aaa";
        Maybe<String> m1 = Maybe.of(s);
        System.out.println(m1);
        s = null;
        Maybe<String> m2 = Maybe.of(s);
        System.out.println(m2);

        // Metoda ifPresent(...)
        Integer num = null;
        Maybe<Integer> m4 = Maybe.of(num);
        // ZAMIAST
        if (num != null) System.out.println(num);
        // PISZEMY
        m4.ifPresent(n -> System.out.println(n));
        // A NAWET
        m4.ifPresent(System.out::println);

        Maybe<Integer> m5 = Maybe.of(10);
        m5.ifPresent(System.out::println);

        // Metoda map()
        Maybe<Integer> m6 = m5.map(n -> n + 10);
        System.out.println(m6);

        // Metoda get()
        System.out.println(m6.get());
        try {
            System.out.println(m4.get());
        } catch (Exception exc) {
            System.out.println(exc);
        }

        // Metoda orElse()
        // ZAMIAST
        String snum = null;
        if (num != null) snum = "Wartość wynosi: " + num;
        if (snum != null) System.out.println(snum);
        else System.out.println("Wartość niedostępna");

        // MOŻNA NAPISAĆ
        String res = Maybe.of(num)
                         .map(n -> "Wartość wynosi: " + n)
                         .orElse("Wartość niedostępna");
        System.out.println(res);

        // filter(...)
        String txt = "Pies";
        String msg = "";

        // ZAMIAST
        if (txt != null && txt.length() > 0) {
            msg = txt;
        } else {
            msg = "Txt is null or empty";
        }

        // MOŻNA NAPISAĆ
        msg = Maybe.of(txt)
                   .filter(t -> t.length() > 0)
                   .orElse("Txt is null or empty");
        System.out.println(msg);
    }

    public static void main(String[] args) {
        test();
    }
}
```

### Oczekiwane wyjście

```
Maybe has value aaa
Maybe is empty
10
Maybe has value 20
20
java.util.NoSuchElementException:  maybe is empty
Wartość niedostępna
Wartość niedostępna
Pies
```

---

## Zadanie 2 – `InputConverter` (kompozycja funkcji)

### Opis

Zbudować klasę `InputConverter<T>`, która pozwala przekształcać dane wejściowe (ustalane w konstruktorze) za pomocą łańcucha funkcji podanych jako argumenty metody `convertBy`.

### Zasada działania

Metoda `convertBy` przyjmuje dowolną liczbę funkcji i stosuje je kolejno na danych wejściowych – wynik jednej staje się wejściem następnej:

```java
// Suma liczb całkowitych z pliku:
Integer s = new InputConverter<String>(fname).convertBy(flines, join, collectInts, sum);

// Lista liczb całkowitych z napisu:
List<Integer> n = new InputConverter<String>(txt).convertBy(collectInts);
```

### Wymagania

- W klasie `InputConverter` może wystąpić **tylko jedna metoda** o nazwie `convertBy` – **przeciążanie jest niedopuszczalne**.
- Plik `Main.java` **nie wolno modyfikować** (poza miejscem oznaczonym `/*<--`).

### Klasa `Main` (niemodyfikowalna poza `/*<--`)

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /*<--
         *  definicja operacji w postaci lambda-wyrażeń:
         *  - flines       – zwraca listę wierszy z pliku tekstowego
         *  - join         – łączy napisy z listy (zwraca jeden napis)
         *  - collectInts  – zwraca listę liczb całkowitych zawartych w napisie
         *  - sum          – zwraca sumę elementów listy liczb całkowitych
         */

        String fname = System.getProperty("user.home") + "/LamComFile.txt";
        InputConverter<String> fileConv = new InputConverter<>(fname);
        List<String> lines    = fileConv.convertBy(flines);
        String text           = fileConv.convertBy(flines, join);
        List<Integer> ints    = fileConv.convertBy(flines, join, collectInts);
        Integer sumints       = fileConv.convertBy(flines, join, collectInts, sum);

        System.out.println(lines);
        System.out.println(text);
        System.out.println(ints);
        System.out.println(sumints);

        List<String> arglist = Arrays.asList(args);
        InputConverter<List<String>> slistConv = new InputConverter<>(arglist);
        sumints = slistConv.convertBy(join, collectInts, sum);
        System.out.println(sumints);
    }
}
```

### Przykładowe dane

**Plik `{user.home}/LamComFile.txt`:**

```
Cars:
- Fiat: 15, Ford: 20
- Opel: 8, Mitsubishi: 10
```

**Argumenty wywołania `main`:**

```
Warszawa 100 Kielce 200 Szczecin 300
```

### Oczekiwane wyjście

```
[Cars:, - Fiat: 15, Ford: 20, - Opel: 8, Mitsubishi: 10]
Cars:- Fiat: 15, Ford: 20- Opel: 8, Mitsubishi: 10
[15, 20, 8, 10]
53
600
```

### Wskazówki implementacyjne

- `flines` – typ `Function<String, List<String>>`: czyta plik o podanej ścieżce i zwraca listę wierszy.
- `join` – typ `Function<List<String>, String>`: scala elementy listy napisów w jeden napis (bez separatora).
- `collectInts` – typ `Function<String, List<Integer>>`: wyodrębnia wszystkie liczby całkowite z napisu.
- `sum` – typ `Function<List<Integer>, Integer>`: zwraca sumę elementów listy.
- `convertBy` musi obsługiwać **dowolną liczbę funkcji** o potencjalnie różnych typach wejścia/wyjścia – rozważyć użycie `@SafeVarargs` i surowego typu `Function` lub generics z ograniczeniami.
