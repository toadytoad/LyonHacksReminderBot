import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        JDA bot = JDABuilder.createDefault("OTY0NTc4OTcwNDc5Nzc5ODkw.YlmsKw.d-d73EizppvbCJ1N0j5nyw2uEd0")
                .setActivity(Activity.playing("<redacted>"))
                .build();
    }
}
