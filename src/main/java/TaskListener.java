public class TaskListener{
    static TaskListener t = new TaskListener();
    private TaskListener(){

    }
    public TaskListener getTaskListener() {
        return t;
    }

    public synchronized void invoke(){
        Task next = Bot.taskQueue.peek();
        if(next==null) return;
        long time = System.currentTimeMillis();
        if(next.time<time){

        }
    }



    public static class TaskInvoker extends Thread{

    }
}
