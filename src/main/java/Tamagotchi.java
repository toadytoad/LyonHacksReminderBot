import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Tamagotchi {
    static int happy = 0;
    String title;
    String response;
    String image;
    String sorta = "https://media.discordapp.net/attachments/965058497039437864/965110113016881202/sortasaddino.png?width=571&height=571";
    String neutral = "https://media.discordapp.net/attachments/965058497039437864/965107004119068753/neutraldino.png?width=571&height=571";
    String happy1 = "https://media.discordapp.net/attachments/965058497039437864/965085474559504514/dino.png?width=571&height=571";
    String happy2 = "https://media.discordapp.net/attachments/965058497039437864/965107003875815494/superhappidino.png?width=571&height=571";
    String sad = "https://media.discordapp.net/attachments/965058497039437864/965085474794377276/saddino.png?width=571&height=571";

    public MessageEmbed tamagotchi(boolean increase) {
        EmbedBuilder eb = new EmbedBuilder();

        if(increase) {
            happy++;
            title = "Your tamagotchi is happier!";
            if(happy < 0) {
                response = "Your tamagotchi is feeling a bit better, but its still a bit mentally traumatized. Complete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
                image = sorta;
            } else if(happy > 0 ) {
                response = "You're on a roll! Your tamagotchi is feeling great! Keep up the good work :D\n\n**Happiness level:** " + happy;
                image = happy2;
            } else {
                response = "Your tamagotchi is feeling back to normal again! Great job. Complete more tasks to make it feel even happier!\n\n**Happiness level:** 0";
                image = happy1;
            }
        } else {
            happy--;
            title = "Uh oh! Your tamagotchi is feeling more upset!";
            if(happy < 0) {
                response = "Oh no! Your poor tamagotchi is feeling very sad now that you decided to procrastinate :(\nComplete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
                image = sad;
            } else if(happy > 0) {
                response = "Your tamagotchi is feeling more sad now that you decided to procrastinate :(\n... but its still feeling alright.\n\n**Happiness level** " + happy;
                image = happy1;
            } else {
                response = "Your tamagotchi is on the brink of being very sad now that you decided to procrastinate :(\nBut don't worry, there is still hope! Complete more tasks to make it feel better!\n\n**Happiness level:** 0";
                image = neutral;
            }
        }

        eb.setTitle(title);
        eb.setAuthor("ReMind");
        eb.setDescription(response);
        eb.setImage(image);
        eb.setFooter("Keep on going! You got this!");

        return eb.build();
    }
}
