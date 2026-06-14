
package zad3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
  public static void main(String[] args) {
    BlockingQueue<Towar> kolejka = new ArrayBlockingQueue<>(1000);

    Thread watekA = new Thread(new Producent(kolejka));
    Thread watekB = new Thread(new Konsument(kolejka));

    watekA.start();
    watekB.start();
  }
}
