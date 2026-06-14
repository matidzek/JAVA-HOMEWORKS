# Zadania – Programowanie Współbieżne w Javie

---

## Zadanie 1 – Uruchamianie i zatrzymywanie równoległego działania kodów

### Opis

Zbudować klasę `StringTask`, symulującą długotrwałe obliczenia polegające na konkatenacji napisów.

### Wymagania klasy `StringTask`

- **Konstruktor** przyjmuje:
  - napis do powielenia,
  - liczbę określającą ile razy napis ma być powielony.
- Klasa implementuje interfejs `Runnable`.
- W metodzie `run()` powielanie napisu odbywa się za pomocą operatora `+` na zmiennych typu `String` (**warunek obowiązkowy**).
- Obiekt klasy `StringTask` jest zadaniem mogącym wykonywać się równolegle z innymi.

### Stany zadania (`TaskState`)

| Stan      | Opis                                                    |
|-----------|---------------------------------------------------------|
| `CREATED` | Zadanie utworzone, ale jeszcze nie rozpoczęte           |
| `RUNNING` | Zadanie wykonuje się w odrębnym wątku                   |
| `ABORTED` | Wykonanie zadania zostało przerwane                     |
| `READY`   | Zadanie zakończyło się pomyślnie, wyniki są dostępne    |

### Metody klasy `StringTask`

| Metoda                      | Opis                                                                 |
|-----------------------------|----------------------------------------------------------------------|
| `String getResult()`        | Zwraca wynik konkatenacji                                            |
| `TaskState getState()`      | Zwraca aktualny stan zadania                                         |
| `void start()`              | Uruchamia zadanie w odrębnym wątku                                   |
| `void abort()`              | Przerywa wykonanie zadania i działanie wątku                         |
| `boolean isDone()`          | Zwraca `true`, gdy zadanie zakończyło się normalnie lub przez przerwanie |

### Klasa `Main`

```java
public class Main {

    public static void main(String[] args) throws InterruptedException {
        StringTask task = new StringTask("A", 70000);
        System.out.println("Task " + task.getState());
        task.start();
        if (args.length > 0 && args[0].equals("abort")) {
            /*<- tu zapisać kod przerywający działanie tasku po sekundzie
                 i uruchomić go w odrębnym wątku
            */
        }
        while (!task.isDone()) {
            Thread.sleep(500);
            switch (task.getState()) {
                case RUNNING: System.out.print("R."); break;
                case ABORTED: System.out.println(" ... aborted."); break;
                case READY:   System.out.println(" ... ready.");   break;
                default:      System.out.println("unknown state");
            }
        }
        System.out.println("Task " + task.getState());
        System.out.println(task.getResult().length());
    }
}
```

### Oczekiwane wyjście

**Bez argumentu:**
```
Task CREATED
R.R.R.R.R.R.R.R.R. ... ready.
Task READY
70000
```

**Z argumentem `abort`:**
```
Task CREATED
R. ... aborted.
Task ABORTED
31700
```

### Uwagi

1. Plik `Main.java` może być modyfikowany **tylko w miejscu oznaczonym** `/*<- */`.
2. **Nie wolno** używać metody `System.exit(...)`.
3. W tym zadaniu **nie należy** stosować `Executor` / `ExecutorService`.

---

## Zadanie 2 – Lista zadań z GUI (Swing)

### Opis

Napisać program, w którym uruchamiane zadania wyświetlane są na liście `javax.swing.JList`.

### Wymagania

- Zadania z listy można:
  - odpytywać o ich stan,
  - anulować,
  - wyświetlać ich wyniki, gdy są gotowe.
- Zadania realizowane są jako `Future`.

---

## Zadanie 3 – Towary (8 punktów)

### Opis

Program składa się z dwóch współbieżnych wątków odczytujących i przetwarzających dane o towarach z pliku.

### Wątek A – Czytanie danych

- Czyta z pliku `../Towary.txt` dane w postaci:
  ```
  id_towaru waga
  ```
- Tworzy obiekty klasy `Towar` z odczytanymi danymi.
- Na konsolę wypisuje informację o liczbie utworzonych obiektów **co 200 obiektów**:
  ```
  utworzono 200 obiektów
  utworzono 400 obiektów
  utworzono 600 obiektów
  ...
  ```

### Wątek B – Sumowanie wag

- Pobiera obiekty tworzone przez wątek A.
- Sumuje wagę towarów.
- Na konsolę wypisuje informację o postępie **co 100 obiektów**:
  ```
  policzono wage 100 towarów
  policzono wage 200 towarów
  policzono wage 300 towarów
  ...
  ```
- Na końcu podaje sumaryczną wagę wszystkich towarów.

### Uwagi

1. Plik `Towary.txt` powinien zawierać **co najmniej 10 000** opisów towarów (należy wygenerować go programistycznie, ale w osobnym programie). Nazwa pliku (wraz ze ścieżką) jest obowiązkowa — **nie dołączać pliku do projektu**.
2. Zapewnić **synchronizację i koordynację** pracy obu wątków.
3. Forma wydruku na konsoli jest **obowiązkowa**.
4. Wszystkie klasy w programie winny być **publiczne** (w osobnych plikach).
5. Wykonanie programu musi zaczynać się w metodzie `main()` obowiązkowej klasy `Main`.
6. Plik `Towary.txt` musi znajdować się w **nadkatalogu projektu** (katalogu workspace'u).

> **Uwaga:** Za niespełnienie powyższych warunków nie będą przyznawane punkty.
