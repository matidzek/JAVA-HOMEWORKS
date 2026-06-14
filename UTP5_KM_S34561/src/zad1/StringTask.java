package zad1;

public class StringTask implements Runnable {
    enum TaskState {
        CREATED,
        RUNNING,
        ABORTED,
        READY
    }

    private final String str;
    private final int    count;

    private String    result = "";
    private TaskState  state  = TaskState.CREATED;
    private Thread     thread;

    public StringTask(String str, int count) {
        this.str   = str;
        this.count = count;
    }

    @Override
    public void run() {
        setState(TaskState.RUNNING);

        String r = "";
        for (int i = 0; i < count; i++) {
            if (Thread.currentThread().isInterrupted()) {
                finish(r, TaskState.ABORTED);
                return;
            }
            r = r + str;
        }

        finish(r, TaskState.READY);
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void abort() {
        Thread t = thread;
        if (t != null) t.interrupt();
    }

    public synchronized String getResult() {
        return result;
    }

    public synchronized TaskState getState() {
        return state;
    }

    public boolean isDone() {
        TaskState s = getState();
        return s == TaskState.READY || s == TaskState.ABORTED;
    }

    private synchronized void setState(TaskState s) {
        state = s;
    }

    private synchronized void finish(String r, TaskState s) {
        result = r;
        state  = s;
    }
}
