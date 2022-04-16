import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Bot extends ListenerAdapter { //TODO add a priority queue with Task as the object, and start a thread which constantly reads from this queue and checks once the head of the queue is less than the current time, at which point send the message and remove it from the queue.
    static Thread t = new Thread(TaskListener.t);

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
        t.start();
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
            if(message.length<3){
                //get mad
            } else {
                StringBuilder text = new StringBuilder();
                for(int i = 2; i<message.length; i++){
                    text.append(message[i]).append(" ");
                }
                try {
                    Task toAdd = new Task(text.toString().trim(), System.currentTimeMillis()+Long.parseLong(message[1]), e.getMember().getUser(), e);
                    TaskListener.t.add(toAdd);
                } catch (NumberFormatException ex) {
                    //get mad about bad number
                } catch (NullPointerException ex) {
                    //get mad about bad user
                }
            }
        } else if(message[0].equals("!list")) {

        }
    }
}