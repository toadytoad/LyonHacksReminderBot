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
    long amount;
    MessageReceivedEvent channel;
    int counter = 0;

    public Task(String text, long time, long total, User user, Member member, MessageReceivedEvent e, long amount) {
        this.text = text;
        this.time = time;
        this.total = total;
        this.user = user;
        this.member = member;
        this.amount = amount;
        channel = e;
    }

    synchronized void ping() throws InterruptedException { //TODO Constantly read and count the amount of time the user has been online. At the end upload an image.
        counter = 0;
        wait(time);
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Hey there! Here to remind you about " + text)).queue();
        wait(60000);
        if (member.getOnlineStatus() == OnlineStatus.ONLINE && counter + 60000 < amount) {
            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(false))).queue();
        } else if(counter >= amount) {
            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(true))).queue();
        }
    }

    synchronized void counter() throws InterruptedException {
        while(counter + 60000 < amount) {
            wait(1000);
            counter++;
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
