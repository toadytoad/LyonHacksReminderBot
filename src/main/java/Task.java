import net.dv8tion.jda.api.entities.User;

public class Task extends Thread { //TODO implement comparable based on time.
    String text;
    long time;
    User user;
    public Task(String text, long time, User user){
        this.text = text;
        this.time = time;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }
}