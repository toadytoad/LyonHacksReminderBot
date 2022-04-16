import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.time.LocalTime;
import java.util.*;

public class Bot extends ListenerAdapter { //TODO add a priority queue with Task as the object, and start a thread which constantly reads from this queue and checks once the head of the queue is less than the current time, at which point send the message and remove it from the queue.
    public static ArrayList<Task> tasks = new ArrayList<Task>();

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

        Guild g = jda.getGuildById("964587882771791985");
        g.upsertCommand("ping", "Calculate ping of the bot").queue();
        g.upsertCommand("add", "DM string in certain amount of milliseconds").queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("ping")) { // make sure we handle the right command
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().trim().split(" ");

        if(message[0].equals("!add")) {
            tasks.add(new Task(message[1], convertTime(message[2]), e.getAuthor(), e.getMember(), e));
            e.getChannel().sendMessage("I'll remind you about \"" + message[1] + "\" at " + message[2]).queue();
            tasks.get(tasks.size()-1).start();
        } else if(message[0].equals("!list")) {
            String list = "";
            for(Task task : tasks) {
                list += "- " + task.text + ", Time: " + task.time + "\n";
            }
            e.getChannel().sendMessageEmbeds(list(list)).queue();
        } else if(message[0].equals("!help")) {
            e.getChannel().sendMessageEmbeds(greeting()).queue();
        }
    }

    public long convertTime(String time) {
        String currentTime = "" + LocalTime.now();
        String[] t1 = currentTime.split(":");
        t1[2] = t1[2].substring(0, 2);

        long hours1 = Long.parseLong(t1[0]) * 360000;
        long mins1 = Long.parseLong(t1[1]) * 60000;
        long secs1 = Long.parseLong(t1[2]) * 1000;

        String[] t2 = time.split(":");

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
                "(makeshift description, this will def not be the real description)\n" +
                "\n" +
                "Here to help you out with your incurable addiction to Discord by reminding you when to stop using it ;)\n" +
                "We will be gifting you with a tamagotchi! (Whichhh is currently non-existent :dying:) To keep your beloved pet happy, all you have to do is follow your schedule instead of procrastinating on discord! But if you choose to rebel and waste time on discord, your tamagotchi will get sad :(\n" +
                "\n" +
                "**Commands**\n" +
                "- **!add [task] [time]** - add a task\n" +
                "- **!list** - lists your tasks in dms\n" +
                "- **!help** - to print this message again for whatever reason");

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