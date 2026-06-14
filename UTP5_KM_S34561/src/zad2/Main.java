
package zad2;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class Main extends JFrame {

  private final DefaultListModel<Task> model = new DefaultListModel<>();
  private final JList<Task> list = new JList<>(model);
  private final ExecutorService exec = Executors.newFixedThreadPool(2);
  private int counter = 0;

  public Main() {
    super("Zadania na liscie");

    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setFont(new Font("Monospaced", Font.PLAIN, 14));
    add(new JScrollPane(list), BorderLayout.CENTER);

    JPanel buttons = new JPanel();
    addButton(buttons, "Nowe zadanie", e -> newTask());
    addButton(buttons, "Stan",         e -> showState());
    addButton(buttons, "Anuluj",       e -> cancelSelected());
    addButton(buttons, "Wynik",        e -> showResult());
    addButton(buttons, "Shutdown",     e -> exec.shutdown());
    addButton(buttons, "ShutdownNow",  e -> exec.shutdownNow());
    add(buttons, BorderLayout.SOUTH);


    new javax.swing.Timer(250, e -> list.repaint()).start();

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(480, 360);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void addButton(JPanel p, String label, java.awt.event.ActionListener a) {
    JButton b = new JButton(label);
    b.addActionListener(a);
    p.add(b);
  }

  private void newTask() {
    int n = 8 + (int) (Math.random() * 12);
    Job job = new Job(n);
    Task task = new Task("Zadanie " + (++counter), job,
            () -> SwingUtilities.invokeLater(list::repaint));
    model.addElement(task);
    try {
      exec.execute(task);
    } catch (RejectedExecutionException ex) {
      JOptionPane.showMessageDialog(this, "Wykonawca zamkniety - zadanie odrzucone.");
      model.removeElement(task);
    }
  }

  private Task selected() {
    Task t = list.getSelectedValue();
    if (t == null) JOptionPane.showMessageDialog(this, "Najpierw zaznacz zadanie na liscie.");
    return t;
  }

  private void showState() {
    Task t = selected();
    if (t != null) JOptionPane.showMessageDialog(this, t + "\nStan: " + t.statusText());
  }

  private void cancelSelected() {
    Task t = selected();
    if (t != null) {
      boolean ok = t.cancel(true);
      list.repaint();
      JOptionPane.showMessageDialog(this,
              ok ? "Anulowano." : "Nie udalo sie anulowac (juz zakonczone?).");
    }
  }

  private void showResult() {
    Task t = selected();
    if (t != null) JOptionPane.showMessageDialog(this, t.resultText());
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Main::new);
  }
}


class Job implements Callable<Integer> {
  private final int n;
  private volatile int progress = 0;

  Job(int n) { this.n = n; }
  int progress() { return progress; }
  int total() { return n; }

  @Override
  public Integer call() throws Exception {
    int sum = 0;
    for (int i = 1; i <= n; i++) {
      if (Thread.currentThread().isInterrupted()) return sum;
      sum += i;
      progress = i;
      Thread.sleep(400);
    }
    return sum;
  }
}


class Task extends FutureTask<Integer> {
  private final String name;
  private final Job job;
  private final Runnable onDone;

  Task(String name, Job job, Runnable onDone) {
    super(job);
    this.name = name;
    this.job = job;
    this.onDone = onDone;
  }

  @Override
  protected void done() {
    onDone.run();
  }

  String statusText() {
    if (isCancelled()) return "ANULOWANE";
    if (isDone())      return "GOTOWE";
    if (job.progress() > 0) return "WYKONUJE SIE " + job.progress() + "/" + job.total();
    return "OCZEKUJE";
  }

  String resultText() {
    if (isCancelled()) return name + ": anulowane.";
    if (!isDone())     return name + ": jeszcze nie gotowe: " + statusText();
    try {
      return name + ": wynik = " + get();
    } catch (ExecutionException ex) {
      return name + ": wyjatek w zadaniu: " + ex.getCause();
    } catch (CancellationException ex) {
      return name + ": anulowane";
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      return name + ": oczekiwanie przerwane";
    }
  }

  @Override
  public String toString() {
    return String.format("%-12s [%s]", name, statusText());
  }
}