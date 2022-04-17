import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Task extends Thread {
    String text;
    long time;
    long total;
    User user;
    Member member;
    MessageReceivedEvent channel;

    public Task(String text, long time, long total, User user, Member member, MessageReceivedEvent e){
        this.text = text;
        this.time = time;
        this.total=total;
        this.user = user;
        this.member = member;
        channel = e;
    }

    synchronized void ping() throws InterruptedException { //TODO Constantly read and count the amount of time the user has been online. At the end upload an image.
        wait(time);
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Hey there! Here to remind you to start `" + text+'`')).queue();
        wait(total);
        if(member.getOnlineStatus() == OnlineStatus.ONLINE) {
            user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Hi! :] did you finish `" + text+"`? ")).queue();

        }
    }

    public void run() {
        try {
            ping();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}