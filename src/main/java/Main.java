import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDA bot = JDABuilder.createDefault("OTY0NTc4OTcwNDc5Nzc5ODkw.YlmsKw.d-d73EizppvbCJ1N0j5nyw2uEd0")
                .setActivity(Activity.playing("<redacted>"))
                .build();
    }
}
