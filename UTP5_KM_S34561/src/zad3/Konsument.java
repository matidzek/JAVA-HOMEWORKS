package zad3;

import java.util.concurrent.BlockingQueue;

public class Konsument implements Runnable {
    private BlockingQueue<Towar> kolejka;

    public Konsument(BlockingQueue<Towar> kolejka) {
        this.kolejka = kolejka;
    }

    public void run() {
        int count = 0;
        double sumaWag = 0;
        try {
            while (true) {
                Towar t = kolejka.take();
                if (t.getWaga() == -1) break;  // sygnał końca
                sumaWag += t.getWaga();
                count++;
                if (count % 100 == 0)
                    System.out.println("policzono wage " + count + " towarów");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Sumaryczna waga: " + sumaWag);
    }
}