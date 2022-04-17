import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.time.LocalTime;
import java.util.*;

public class Bot extends ListenerAdapter {
    public static Tamagotchi dino = new Tamagotchi();
    public static Map<User, List<Task>> map = new HashMap<>();
    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        // args[0] should be the token
        // We don't need any intents for this bot. Slash commands work without any intents!
        JDA jda = JDABuilder.createDefault(args[0])
                .addEventListeners(new Bot())
                .setActivity(Activity.playing("Type !help"))
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
        String[] message = e.getMessage().getContentRaw().trim().split(" ", -1);
        String endTime = "";

        switch (message[0]) {
            case "!add" -> {
                try {
                    String[] temp = message[message.length-2].trim().split(":");
                    Integer.parseInt(temp[0]);
                    Integer.parseInt(temp[1]);
                    long needed;
                    long val;
                    try {
                        String totS = message[message.length-1].trim().toLowerCase(Locale.ROOT);
                        char finalChar = totS.charAt(totS.length() - 1);
                        val = Long.parseLong(totS.substring(0, totS.length() - 1));
                        switch (finalChar) {
                            case 'm' -> {
                                needed = val * 60000;
                                String mini;
                                if((mini = ("" + ((Long.parseLong(temp[1]) + val) % 60))).length() == 1)
                                    mini = "0" + mini;
                                endTime = (Long.parseLong(temp[0]) + (Long.parseLong(temp[1]) + val) / 60) + ":" + mini;
                            }
                            case 'h' -> {
                                needed = val * 3600000;
                                endTime = (Long.parseLong(temp[0]) + val) + ":" + temp[1];
                            }
                            default -> throw new IllegalArgumentException();
                        }
                    } catch (IllegalArgumentException x) {
                        e.getChannel().sendMessage("Please enter a valid total amount of time needed. [m, h]").queue();
                        return;
                    }
                    if (convertTime(message[message.length-2]) <= 0) {
                        e.getChannel().sendMessage("Please enter a time that is after the current time in 24 hour format!").queue();
                    } else {
                        StringBuilder taskDesc = new StringBuilder();
                        for(int i = 1; i<message.length-2; i++){
                            taskDesc.append(message[i]).append(' ');
                        }
                        Task current = new Task(taskDesc.toString().trim(), convertTime(message[message.length-2]), message[message.length-2], endTime, e.getAuthor(), e.getMember(), e, needed);
                        if (!map.containsKey(current.user)) {
                            map.put(current.user, new ArrayList<>());
                        }
                        map.get(current.user).add(current);
                        e.getChannel().sendMessage("I'll remind you about \"" + taskDesc.toString().trim() + "\" at " + message[message.length-2].trim()).queue();
                        current.start();
                    }
                } catch (NumberFormatException x) {
                    e.getChannel().sendMessage("Please enter a proper time in 24 hour format").queue();
                } catch (ArrayIndexOutOfBoundsException x) {
                    e.getChannel().sendMessage("Invalid command, please use `!add, [reminder], [time start], [time needed]`").queue();
                }
            }
            case "!list" -> {
                if (map.containsKey(Objects.requireNonNull(e.getMember()).getUser()) && map.get(e.getAuthor()).size() > 0) {
                    StringBuilder list = new StringBuilder();
                    for (Task task : map.get(e.getMember().getUser())) {
                        list.append("- ").append(task.text).append(", Time: ").append(task.writtenTime).append(" - ").append(task.endTime).append("\n");
                    }
                    e.getChannel().sendMessageEmbeds(list(list.toString())).queue();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("You have no tasks to do!");
                    eb.setDescription("Use !add to add some tasks.");
                    eb.setImage("https://cdn.discordapp.com/attachments/965058497039437864/965085474559504514/dino.png");
                    e.getChannel().sendMessageEmbeds(eb.build()).queue();
                }
            }
            case "!help" -> e.getChannel().sendMessageEmbeds(greeting()).queue();
            case "!remove" -> {
                User usr;
                try {
                    usr = Objects.requireNonNull(e.getMember()).getUser();
                } catch(NullPointerException x) {
                    e.getChannel().sendMessage("Oops! Something went wrong, the user does not seem to exist!").queue();
                    return;
                }

                List<Task> taskList = map.get(usr);
                if(message.length<2){
                    e.getChannel().sendMessage("Invalid command, please use `!remove ").queue();
                }
                StringBuilder taskDesc = new StringBuilder();
                for(int i = 1; i<message.length; i++) taskDesc.append(message[i]).append(" ");
                String s = taskDesc.toString().trim();
                for(int i = 0; i<taskList.size(); i++){
                    if(taskList.get(i).text.equals(s)){
                        taskList.get(i).interrupt();
                        taskList.remove(i);
                        e.getChannel().sendMessage("Successfully removed your task!").queue();
                        return;
                    }
                }
                e.getChannel().sendMessage("The task you wanted to remove was not found. Please check your task name and try again.").queue();
            }
        }
    }

    public long convertTime(String time) {
        String currentTime = "" + LocalTime.now();
        String[] t1 = currentTime.split(":");
        t1[2] = t1[2].substring(0, 2);

        long hours1 = Long.parseLong(t1[0]) * 3600000;
        long mins1 = Long.parseLong(t1[1]) * 60000;
        long secs1 = Long.parseLong(t1[2]) * 1000;

        String[] t2 = time.trim().split(":");

        long hours2 = Long.parseLong(t2[0]) * 3600000;
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
                        Here to help you out with your incurable addiction to Discord by reminding you when to stop using it ;)
                        We will be gifting you with a tamagotchi! To keep your beloved pet happy, all you have to do is follow your schedule instead of procrastinating on discord! But if you choose to rebel and waste time on discord, your tamagotchi will get sad :(

                        **Commands**
                        - **!add [task] [time start] [time needed][m/h]** - add a task (m = minutes, h = hours)
                        - **!list** - lists your tasks
                        - **!remove [task]** - removes a task
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