import java.util.PriorityQueue;

public class TaskListener extends Thread {
    static TaskListener t = new TaskListener();
    public static PriorityQueue<Task> taskQueue = new PriorityQueue<>();
    public Thread ti;

    private TaskListener() {

    }

    public TaskListener getTaskListener() {
        return t;
    }

    public synchronized void run() {
        System.out.println("Started listening!");
        for(;;){
            try {
                wait();
            } catch (InterruptedException ignored) {}
            System.out.println("Invoked!");
            invoke();
        }
    }

    public synchronized void invoke() {
        try {
            ti.interrupt();
        } catch (NullPointerException ignored) {}
        Task next = taskQueue.peek();
        if (next == null) return;
        long time = System.currentTimeMillis();
        if (next.time <= time) {
            next.user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Trolling :clown:. Here to remind you about " + next.text)).queue();
            taskQueue.poll();
        } else {
            System.out.println("Starting a new invoker");
            ti = new Thread(new TaskInvoker(next.time-time));
            ti.start();
        }
    }
    public synchronized void add(Task task) {
        taskQueue.add(task);
        t.notify();
    }


    public static class TaskInvoker extends Thread {
        long ms;
        public TaskInvoker(long ms){
            this.ms=ms;
        }
        public synchronized void run() {
            System.out.println("Task Invoker told to invoke in "+ms);
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {}
            System.out.println("Task Invoker Notifying!");
            System.out.println(t.getName());
            t.notify();
        }

    }
}
