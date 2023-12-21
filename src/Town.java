/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;

    private double tough;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";
        boolean hunted = false;
        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        tough = toughness;

    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + Colors.WHITE + hunter.getHunterName() + Colors.RESET+ ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + Colors.PURPLE+  item + Colors.RESET + " to cross the " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET +  ".";
            if (checkItemBreak() && (tough > 0.1)) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + Colors.PURPLE+ item + Colors.RESET + ".";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Colors.PURPLE + terrain.getNeededItem() + Colors.RESET + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop.";

    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = Colors.BLACK + "You couldn't find any trouble" + Colors.RESET;
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (tough == 0) {
                printMessage = "The brawler, seeing your sword, realizes he picked a losing fight and gives you his gold";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW +  goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else if (Math.random() > noTroubleChance) {
                printMessage += Colors.BLUE + "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW +  goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += "\nYou lost the brawl and pay " +Colors.YELLOW +  goldDiff + Colors.RESET+ " gold.";
                hunter.changeGold(-goldDiff);
            }
        }
        if(hunter.getGold() < 0){
            TreasureHunter.lose = true;
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " +Colors.CYAN+  terrain.getTerrainName() + Colors.RESET +  ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int)((Math.random() * 12) + 1);
        if (rnd < 2) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < 4) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < 6) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < 8) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 10){
            return new Terrain("Jungle", "Machete");
        }   else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
    public String randomTreasure(){
        String item = "";
        int rand = (int) ((Math.random() * 4) - 1);
        if (rand == 1){
            item = "Crown";
        } else if (rand == 2){
            item = "Trophy";
        } else if (rand == 3){
            item = "Gem";
        } else{
            item = "Dust";
        }

        return item;
    }
    public void hunt(String item){
        System.out.println("You found " + item + "!");
        if (!item.equals("Dust")){
            hunter.addTreasure(item);
        }
    }
}