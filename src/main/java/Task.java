import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Task extends Thread { //TODO implement comparable based on time.
    String text;
    long time;
    User user;
    Member member;
    MessageReceivedEvent channel;

    public Task(String text, long time, User user, Member member, MessageReceivedEvent e){
        this.text = text;
        this.time = time;
        this.user = user;
        this.member = member;
        channel = e;
    }

    synchronized void ping() throws InterruptedException {
        wait(time);
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Hey there! Here to remind you about " + text)).queue();
        wait(60000);
        if(member.getOnlineStatus() == OnlineStatus.ONLINE) {
            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(false))).queue();
        } else {
            user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(Bot.dino.tamagotchi(true))).queue();
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