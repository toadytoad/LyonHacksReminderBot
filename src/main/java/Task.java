import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Task extends Thread { //TODO implement comparable based on time.
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

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }

    synchronized void ping() throws InterruptedException {
        this.wait(time);
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Trolling :clown:. Here to remind you about " + text)).queue();
    }

    public void run() {
        try {
            ping();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}