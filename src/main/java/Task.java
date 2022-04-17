import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Task extends Thread {
    String text;
    long time;
    User user;
    Member member;
    long amount;
    int counter = 30000;
    MessageReceivedEvent channel;

    public Task(String text, long time, User user, Member member, MessageReceivedEvent e, long amount) {
        this.text = text;
        this.time = time;
        this.user = user;
        this.member = member;
        this.amount = amount;
        channel = e;
    }

    synchronized void ping() throws InterruptedException {
        wait(time);
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Hey there! Here to remind you about " + text)).queue();
        wait(counter);
        while(counter < amount) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(false))).queue();
                Bot.map.get(user).remove(this);
                return;
            }
            wait(1000);
            counter+=1000;
        }
        user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(true))).queue();
        Bot.map.get(user).remove(this);
    }

    public void run() {
        try {
            ping();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
