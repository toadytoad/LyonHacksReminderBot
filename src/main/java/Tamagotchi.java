import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Tamagotchi {
    static int happy = 0;
    String title;
    String response;
    String image;

    public MessageEmbed tamagotchi(boolean increase) {
        EmbedBuilder eb = new EmbedBuilder();

        if(increase) {
            title = "Your tamagotchi is happier!";
            if(happy < 0) {
                response = "Your tamagotchi is feeling a bit better, but its still a bit mentally traumatized. Complete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
                image = "main/res/dino.png";
            } else if(happy > 0 ) {
                response = "You're on a roll! Your tamagotchi is feeling great! Keep up the good work :D\n\n**Happiness level:** " + happy;
                image = "main/res/dino.png";
            } else {
                response = "Your tamagotchi is feeling back to normal again! Great job. Complete more tasks to make it feel even happier!\n\n**Happiness level:** 0";
                image = "main/res/dino.png";
            }
        } else {
            title = "Uh oh! Your tamagotchi is feeling more upset!";
            if(happy < 0) {
                response = "Oh no! Your poor tamagotchi is feeling very sad now that you decided to procrastinate :(. Complete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
                image = "main/res/saddino.png";
            } else if(happy > 0) {
                response = "Your tamagotchi is feeling sader now that you decided to procrastinate :(... but its still feeling alright.\n\n**Happiness level** " + happy;
                image = "main/res/dino.png";
            } else {
                response = "Your tamagotchi is on the brink of being very sad now that you decided to procrastinate :(. But don't worry, there is still hope! Complete more tasks to make it feel better!\n\n**Happiness level:** 0";
                image = "main/res/dino.png";
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
