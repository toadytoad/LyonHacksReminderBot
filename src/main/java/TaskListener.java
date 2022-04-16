import java.util.PriorityQueue;

public class TaskListener extends Thread {
    static TaskListener t = new TaskListener();
    static PriorityQueue<Task> taskQueue = new PriorityQueue<>();

    private TaskListener() {
    }

    public synchronized void run() {
        System.out.println("Running!");
        for (; ; ) {
            System.out.println("Checking next task!");
            Task next = taskQueue.peek();
            if (next == null) {
                try {
                    System.out.println("Waiting for new task...");
                    wait();
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            System.out.println("Not a null task!");
            long time = System.currentTimeMillis();

            System.out.println("Current time is " + time + "\nTask set to execute at " + next.time);
            if (next.time <= time) {
                System.out.println("Going to execute task");
                next.user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Trolling :clown:. Here to remind you about " + next.text)).queue();
                System.out.println("Executed task");
                taskQueue.poll();
                System.out.println("Removed from queue");
            } else {
                System.out.println("It is not time yet to execute");
                try {
                    System.out.println("Going to wait");
                    wait(next.time - time);
                    System.out.println("Finished waiting");
                } catch (InterruptedException ignored) {
                }
            }

        }
    }
}
