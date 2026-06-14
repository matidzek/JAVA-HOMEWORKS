package zad3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;


public class Producent implements Runnable {
    private BlockingQueue<Towar> kolejka;
    private static final String PATH = "../Towary.txt";

    public Producent(BlockingQueue<Towar> kolejka) {
        this.kolejka = kolejka;
    }

    public void run() {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                String[] tokeny = linia.split(" ");
                int id = Integer.parseInt(tokeny[0]);
                double waga = Double.parseDouble(tokeny[1]);
                kolejka.put(new Towar(id, waga));
                count++;
                if (count % 200 == 0)
                    System.out.println("utworzono " + count + " obiektów");
            }
            kolejka.put(new Towar(-1, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
