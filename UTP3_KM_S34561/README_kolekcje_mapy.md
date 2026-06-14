# Zadania – Java: Kolekcje, Sortowanie, Mapy

---

## Zadanie 1 – XList

### Opis

Stworzyć klasę `XList`, dostarczającą dodatkowych możliwości tworzenia list i operowania na nich.

### Tworzenie obiektów `XList`

Klasa powinna udostępniać odpowiednie **konstruktory** oraz **statyczne metody `of`** umożliwiające tworzenie obiektów `XList` z:
- innych kolekcji,
- tablic,
- argumentów podawanych przez przecinki.

**Pomocnicze metody do tworzenia z napisów:**

| Metoda | Opis |
|--------|------|
| `ofChars(napis)` | Zwraca x-listę znaków napisu |
| `ofTokens(napis [, sep])` | Zwraca x-listę tokenów napisu rozdzielonych separatorem `sep`; jeśli brak – białymi znakami |

### Metody operacyjne

| Metoda | Opis |
|--------|------|
| `union(dowolna_kolekcja)` | Zwraca nową x-listę z dołączoną zawartością kolekcji |
| `diff(dowolna_kolekcja)` | Zwraca x-listę elementów tej x-listy, które **nie** występują w kolekcji |
| `unique()` | Zwraca nową x-listę bez duplikatów (lista, nie `Set`) |
| `combine()` | Zwraca x-listę list-kombinacji elementów z poszczególnych kolekcji będących elementami tej x-listy |
| `collect(Function)` | Zwraca nową x-listę wyników funkcji stosowanej do każdego elementu |
| `join([sep])` | Zwraca napis będący połączeniem elementów, z opcjonalnym separatorem |
| `forEachWithIndex(BiConsumer)` | Iteruje po liście z dostępem do elementu i jego indeksu |

### Uwagi

- Za realizację **każdej właściwości** uzyskuje się odrębne punkty – można wykonać tylko część zadania.
- Plik `Main.java` **nie może mieć błędów kompilacji** – jeśli jakaś metoda nie jest zaimplementowana, należy usunąć z `Main.java` fragmenty, które ją wywołują.

### Klasa `Main`

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Integer[] ints = { 100, 200, 300 };
        Set<Integer> set = new HashSet<>(Arrays.asList(3, 4, 5));

        XList<Integer> list1 = new XList<>(1, 3, 9, 11);
        XList<Integer> list2 = XList.of(5, 6, 9);
        XList<Integer> list3 = new XList(ints);
        XList<Integer> list4 = XList.of(ints);
        XList<Integer> list5 = new XList(set);
        XList<Integer> list6 = XList.of(set);

        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);
        System.out.println(list4);
        System.out.println(list5);
        System.out.println(list6);

        XList<String> slist1 = XList.charsOf("ala ma kota");
        XList<String> slist2 = XList.tokensOf("ala ma kota");
        XList<String> slist3 = XList.tokensOf("A-B-C", "-");

        System.out.println(slist1);
        System.out.println(slist2);
        System.out.println(slist3);

        List<Integer> m1 = list1.union(list2);
        System.out.println(m1);
        m1.add(11);
        System.out.println(m1);
        XList<Integer> m2 = (XList<Integer>) m1;
        XList<Integer> m3 = m2.union(ints).union(XList.of(4, 4));
        System.out.println(m2);
        System.out.println(m3);
        m3 = m3.union(set);
        System.out.println(m3);

        System.out.println(m3.diff(set));
        System.out.println(XList.of(set).diff(m3));

        XList<Integer> uniq = m3.unique();
        System.out.println(uniq);

        List<String> sa = Arrays.asList("a", "b");
        List<String> sb = Arrays.asList("X", "Y", "Z");
        XList<String> sc = XList.charsOf("12");
        XList toCombine = XList.of(sa, sb, sc);
        System.out.println(toCombine);
        XList<XList<String>> cres = toCombine.combine();
        System.out.println(cres);

        XList<String> j1 = cres.collect(list -> list.join());
        System.out.println(j1.join(" "));
        XList<String> j2 = cres.collect(list -> list.join("-"));
        System.out.println(j2.join(" "));

        XList<Integer> lmod = XList.of(1, 2, 8, 10, 11, 30, 3, 4);
        lmod.forEachWithIndex((e, i) -> lmod.set(i, e * 2));
        System.out.println(lmod);
        lmod.forEachWithIndex((e, i) -> { if (i % 2 == 0) lmod.remove(e); });
        System.out.println(lmod);
        lmod.forEachWithIndex((e, i) -> { if (i % 2 == 0) lmod.remove(i); });
        System.out.println(lmod);
    }
}
```

### Oczekiwane wyjście

```
[1, 3, 9, 11]
[5, 6, 9]
[100, 200, 300]
[100, 200, 300]
[3, 4, 5]
[3, 4, 5]
[a, l, a,  , m, a,  , k, o, t, a]
[ala, ma, kota]
[A, B, C]
[1, 3, 9, 11, 5, 6, 9]
[1, 3, 9, 11, 5, 6, 9, 11]
[1, 3, 9, 11, 5, 6, 9, 11]
[1, 3, 9, 11, 5, 6, 9, 11, 100, 200, 300, 4, 4]
[1, 3, 9, 11, 5, 6, 9, 11, 100, 200, 300, 4, 4, 3, 4, 5]
[1, 9, 11, 6, 9, 11, 100, 200, 300]
[]
[1, 3, 9, 11, 5, 6, 100, 200, 300, 4]
[[a, b], [X, Y, Z], [1, 2]]
[[a, X, 1], [b, X, 1], [a, Y, 1], [b, Y, 1], [a, Z, 1], [b, Z, 1], [a, X, 2], [b, X, 2], [a, Y, 2], [b, Y, 2], [a, Z, 2], [b, Z, 2]]
aX1 bX1 aY1 bY1 aZ1 bZ1 aX2 bX2 aY2 bY2 aZ2 bZ2
a-X-1 b-X-1 a-Y-1 b-Y-1 a-Z-1 b-Z-1 a-X-2 b-X-2 a-Y-2 b-Y-2 a-Z-2 b-Z-2
[2, 4, 16, 20, 22, 60, 6, 8]
[4, 16, 22, 60, 8]
[16, 22, 60, 8]
```

---

## Zadanie 2 – CustomersPurchaseSortFind

### Opis

W pliku `customers.txt` w katalogu `{user.home}` znajdują się dane o zakupach klientów w postaci:

```
id_klienta;nazwisko i imię;nazwa_towaru;cena;zakupiona_ilość
```

Identyfikator klienta ma postać `cNNNNN`, gdzie `N` to cyfra `[0–9]`, np.:

```
c00001;Kowalski Jan;bułka;2;100
```

### Wymagane wyjście na konsolę

Program wypisuje kolejno:

1. **`Nazwiska`** – dane posortowane wg nazwisk rosnąco; przy równych nazwiskach – wg identyfikatora rosnąco.
2. **`Koszty`** – dane posortowane wg kosztu (`cena × ilość`) malejąco; przy równych kosztach – wg identyfikatora rosnąco; każdy wiersz kończy się dopiskiem `(koszt: X)`.
3. **`Klient c00001`** – wszystkie zakupy klienta `c00001` (w kolejności z pliku).
4. **`Klient c00002`** – wszystkie zakupy klienta `c00002` (w kolejności z pliku).

> **Uwaga:** Plik musi zawierać klientów o identyfikatorach `c00001` i `c00002`.

### Przykład

**Dane wejściowe (`customers.txt`):**

```
c00004;Nowak Anna;banany;4.0;50.0
c00003;Kowalski Jan;mleko;4.0;5.0
c00001;Kowalski Jan;mleko;4.0;10.0
c00001;Kowalski Jan;mleko;5.0;2.0
c00002;Malina Jan;mleko;4.0;2.0
c00002;Malina Jan;chleb;3.0;5.0
c00001;Kowalski Jan;bulka;2.0;100.0
```

**Oczekiwane wyjście:**

```
Nazwiska
c00001;Kowalski Jan;mleko;4.0;10.0
c00001;Kowalski Jan;mleko;5.0;2.0
c00001;Kowalski Jan;bulka;2.0;100.0
c00003;Kowalski Jan;mleko;4.0;5.0
c00002;Malina Jan;mleko;4.0;2.0
c00002;Malina Jan;chleb;3.0;5.0
c00004;Nowak Anna;banany;4.0;50.0
Koszty
c00001;Kowalski Jan;bulka;2.0;100.0 (koszt: 200.0)
c00004;Nowak Anna;banany;4.0;50.0 (koszt: 200.0)
c00001;Kowalski Jan;mleko;4.0;10.0 (koszt: 40.0)
c00003;Kowalski Jan;mleko;4.0;5.0 (koszt: 20.0)
c00002;Malina Jan;chleb;3.0;5.0 (koszt: 15.0)
c00001;Kowalski Jan;mleko;5.0;2.0 (koszt: 10.0)
c00002;Malina Jan;mleko;4.0;2.0 (koszt: 8.0)
Klient c00001
c00001;Kowalski Jan;mleko;4.0;10.0
c00001;Kowalski Jan;mleko;5.0;2.0
c00001;Kowalski Jan;bulka;2.0;100.0
Klient c00002
c00002;Malina Jan;mleko;4.0;2.0
c00002;Malina Jan;chleb;3.0;5.0
```

### Wymagane klasy

- `Purchase` – klasa opisująca pojedynczy zakup; **nie wolno** operować na surowych `String`ach.
- `CustomersPurchaseSortFind` – klasa z metodami `readFile`, `showSortedBy`, `showPurchaseFor`.
- `Main` – **niemodyfikowalna**:

```java
public class Main {

    public static void main(String[] args) {
        CustomersPurchaseSortFind cpsf = new CustomersPurchaseSortFind();
        String fname = System.getProperty("user.home") + "/customers.txt";
        cpsf.readFile(fname);
        cpsf.showSortedBy("Nazwiska");
        cpsf.showSortedBy("Koszty");

        String[] custSearch = { "c00001", "c00002" };
        for (String id : custSearch) {
            cpsf.showPurchaseFor(id);
        }
    }
}
```

### Uwagi

- Programy nie dające pokazanej formy wydruku otrzymują **0 punktów**.
- Nazwa pliku (`customers.txt` w `{user.home}`) jest **obowiązkowa**.
- Plik testowy należy utworzyć samodzielnie i umieścić w `{user.home}` (np. `C:\Users\Janek` w Windows 7).
- **Nie dołączać** pliku do projektu.

---

## Zadanie 3 – ProgLang

### Opis

Firma software'owa prowadzi projekty w różnych językach programowania. Plik `Programmers.tsv` z katalogu `{user.home}` zawiera dane o programistach w formacie TSV (separatory: znaki tabulacji):

```
język1<TAB>nazwisko1<TAB>nazwisko2<TAB>...
język2<TAB>nazwisko1<TAB>nazwisko2<TAB>...
```

### Wymagana klasa `ProgLang`

| Metoda | Opis |
|--------|------|
| `ProgLang(String nazwaPliku)` | Konstruktor – wczytuje plik |
| `getLangsMap()` | Mapa: `język → kolekcja programistów` |
| `getProgsMap()` | Mapa: `programista → kolekcja języków` |
| `getLangsMapSortedByNumOfProgs()` | Mapa `język → programiści`, posortowana **malejąco** wg liczby programistów; przy równej liczbie – alfabetycznie wg nazwy języka |
| `getProgsMapSortedByNumOfLangs()` | Mapa `programista → języki`, posortowana **malejąco** wg liczby języków; przy równej liczbie – alfabetycznie wg nazwiska |
| `getProgsMapForNumOfLangsGreaterThan(int n)` | Mapa `programista → języki` tylko dla programistów znających **więcej niż `n`** języków |
| `sorted(mapa, lambda)` | Zwraca posortowaną wersję dowolnej mapy wg porządku zadanego lambdą |
| `filtered(mapa, lambda)` | Zwraca mapę zawierającą tylko wpisy spełniające warunek zadany lambdą (wynik `boolean`) |

> Metody `sorted` i `filtered` powinny być **ogólne** (działające na dowolnych mapach) i używane wewnętrznie przez inne metody klasy.

### Klasa `Main` (niemodyfikowalna)

```java
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ProgLang pl = new ProgLang(System.getProperty("user.home") + "/Programmers.tsv");
        System.out.println("@1 Mapa językow:");
        pl.getLangsMap().forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@2 Mapa programistów:");
        pl.getProgsMap().forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@3 Języki posortowane wg liczby programistów:");
        pl.getLangsMapSortedByNumOfProgs()
          .forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@4 Programiści posortowani wg liczby języków:");
        pl.getProgsMapSortedByNumOfLangs()
          .forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@5 Oryginalna mapa języków niezmieniona:");
        pl.getLangsMap().forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@6 Oryginalna mapa programistów niezmienione:");
        pl.getProgsMap().forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@7 Mapa programistów znających więcej niż 1 język:");
        pl.getProgsMapForNumOfLangsGreaterThan(1)
          .forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("@8 Oryginalna mapa programistów nie jest zmieniona:");
        pl.getProgsMap().forEach((k, v) -> System.out.println(k + " = " + v));
    }
}
```

### Przykładowe dane (`Programmers.tsv`)

```
Groovy	Z	Y	X	D
Java	V	B	C	D	A	Z
C++	G	J	H
C#	P	S	Q	V	D
Scala	A	D	A
```

> Duplikaty w wierszu (np. `A` w Scala) są ignorowane – w mapach każdy programista pojawia się raz na język.

### Oczekiwane wyjście (fragment)

```
@1 Mapa językow:
Groovy = [Z, Y, X, D]
Java = [V, B, C, D, A, Z]
C++ = [G, J, H]
C# = [P, S, Q, V, D]
Scala = [A, D]
@3 Języki posortowane wg liczby programistów:
Java = [V, B, C, D, A, Z]
C# = [P, S, Q, V, D]
Groovy = [Z, Y, X, D]
C++ = [G, J, H]
Scala = [A, D]
@4 Programiści posortowani wg liczby języków:
D = [Groovy, Java, C#, Scala]
A = [Java, Scala]
V = [Java, C#]
Z = [Groovy, Java]
B = [Java]
...
@7 Mapa programistów znających więcej niż 1 język:
Z = [Groovy, Java]
D = [Groovy, Java, C#, Scala]
V = [Java, C#]
A = [Java, Scala]
```

> Oryginalne mapy (`@5`, `@6`, `@8`) **nie mogą być modyfikowane** przez metody sortujące i filtrujące.
