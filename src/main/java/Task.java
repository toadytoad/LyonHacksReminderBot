import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class Task extends Thread implements Comparable<Task>{ //TODO implement comparable based on time.
    String text;
    long time;
    User user;
    MessageReceivedEvent channel;

    public Task(String text, long time, User user, MessageReceivedEvent e){
        this.text = text;
        this.time = time;
        this.user = user;
        channel = e;
    }
    @Override
    public int compareTo(@NotNull Task o) {
        return Long.compare(this.time, o.time);
    }
}