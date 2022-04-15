import net.dv8tion.jda.api.entities.User;

public class Task { //TODO implement comparable based on time.
    String text;
    long time;
    User user;
    public Task(String text, long time, User user){

        this.text = text;
        this.time = time;
        this.user = user;
    }
}
