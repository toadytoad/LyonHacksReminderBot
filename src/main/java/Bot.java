import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.time.LocalTime;
import java.util.*;

public class Bot extends ListenerAdapter { //TODO add a priority queue with Task as the object, and start a thread which constantly reads from this queue and checks once the head of the queue is less than the current time, at which point send the message and remove it from the queue.
    public static ArrayList<Task> tasks = new ArrayList<>();
    public static Tamagotchi dino = new Tamagotchi();

    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        // args[0] should be the token
        // We don't need any intents for this bot. Slash commands work without any intents!
        JDA jda = JDABuilder.createDefault(args[0])
                .addEventListeners(new Bot())
                .setActivity(Activity.playing("Type /ping"))
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .build();

        // Just making sure that guild runs at the right time
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().trim().split(",");

        switch (message[0]) {
            case "!add":
                try {
                    String[] temp = message[2].trim().split(":");
                    Integer.parseInt(temp[0]);
                    Integer.parseInt(temp[1]);
                    long needed;
                    try {
                        String totS = message[3].trim().toLowerCase(Locale.ROOT);
                        char finalChar = totS.charAt(totS.length()-1);
                        long val = Long.parseLong(totS.substring(0, totS.length() - 1));
                        switch(finalChar){
                            case 's' -> needed = val *1000;
                            case 'm' -> needed = val *60000;
                            case 'h' -> needed = val *3600000;
                            default -> throw new IllegalArgumentException();
                        }
                    } catch (IllegalArgumentException x){
                        e.getChannel().sendMessage("Please enter a valid total amount of time needed. [s, m, h]").queue();
                        return;
                    }
                    if (convertTime(message[2]) < 0) {
                        e.getChannel().sendMessage("Please enter a time that is after the current time in 24 hour format!").queue();
                    } else {
                        tasks.add(new Task(message[1], convertTime(message[2]), needed, e.getAuthor(), e.getMember(), e));
                        e.getChannel().sendMessage("I'll remind you about \"" + message[1] + "\" at " + message[2]).queue();
                        tasks.get(tasks.size() - 1).start();
                    }
                } catch (NumberFormatException x) {
                    e.getChannel().sendMessage("Please enter a proper time in 24 hour format").queue();
                } catch (ArrayIndexOutOfBoundsException x) {
                    e.getChannel().sendMessage("Invalid command, please use `!add, [reminder], [time start], [time needed]`").queue();
                }
                break;
            case "!list":
                StringBuilder list = new StringBuilder();
                for (Task task : tasks) {
                    list.append("- ").append(task.text).append(", Time: ").append(task.time).append("\n");
                }
                e.getChannel().sendMessageEmbeds(list(list.toString())).queue();
                break;
            case "!help":
                e.getChannel().sendMessageEmbeds(greeting()).queue();
                break;
        }
    }

    public long convertTime(String time) {
        String currentTime = "" + LocalTime.now();
        String[] t1 = currentTime.split(":");
        t1[2] = t1[2].substring(0, 2);

        long hours1 = Long.parseLong(t1[0]) * 360000;
        long mins1 = Long.parseLong(t1[1]) * 60000;
        long secs1 = Long.parseLong(t1[2]) * 1000;

        String[] t2 = time.trim().split(":");

        long hours2 = Long.parseLong(t2[0]) * 360000;
        long mins2 = Long.parseLong(t2[1]) * 60000;

        return hours2 + mins2 - hours1 - mins1 - secs1;
    }

    public MessageEmbed greeting() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Welcome to ReMind!");
        eb.setAuthor("ReMind");
        eb.setFooter("Have fun :D");
        eb.setDescription(
                """
                        (makeshift description, this will def not be the real description)

                        Here to help you out with your incurable addiction to Discord by reminding you when to stop using it ;)
                        We will be gifting you with a tamagotchi! (Whichhh is currently non-existent :dying:) To keep your beloved pet happy, all you have to do is follow your schedule instead of procrastinating on discord! But if you choose to rebel and waste time on discord, your tamagotchi will get sad :(

                        **Commands**
                        - **!add [task] [time start] [time needed][s/m/h]** - add a task
                        - **!list** - lists your tasks in dms
                        - **!help** - to print this message again for whatever reason""");

        return eb.build();
    }

    public MessageEmbed list(String list) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Your tasks");
        eb.setAuthor("ReMind");
        eb.setDescription(list);
        eb.setFooter(":D");

        return eb.build();
    }
}