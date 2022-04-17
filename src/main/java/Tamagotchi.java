public class Tamagotchi {
    static int happy = 0;
    static boolean increase = false;

    public void happyIncrease() {
        happy++;
    }

    public void happyDecrease() {
        happy--;
    }

    public String getNeutralDino() {
        return "main/res/dino.png";
    }

    public String getSadDino() {
        return "main/res/saddino.png";
    }

    public String response() {
        if(happy < 0 && increase) {
            return "Your tamagotchi is feeling a bit better, but its still a bit mentally traumatized. Complete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
        } else if(happy > 0 && increase) {
            return "You're on a roll! Your tamagotchi is feeling great! Keep up the good work :D\n\n**Happiness level:** " + happy;
        } else if(happy == 0 && increase) {
            return "Your tamagotchi is feeling back to normal again! Great job. Complete more tasks to make it feel even happier!\n\n**Happiness level:** 0";
        } else if(happy < 0) {
            return "Oh no! Your poor tamagotchi is feeling very sad now that you decided to procrastinate :(. Complete more tasks to make it feel better!\n\n**Successful task amount needed:** " + Math.abs(happy);
        } else if(happy > 0) {
            return "Your tamagotchi is feeling sader now that you decided to procrastinate :(... but its still feeling alright.\n\n**Happiness level** " + happy;
        } else {
            return "Your tamagotchi is on the brink of being very sad now that you decided to procrastinate :(. But don't worry, there is still hope! Complete more tasks to make it feel better!\n\n**Happiness level:** 0";
        }
    }
}
