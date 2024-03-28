public class Human extends Entity {
    // Additional fields specific to humans
    private int health;
    private int hunger;
    private int thirst;
    private boolean isAlive;

    // Constructor
    public Human(int x, int y, String name, int health, int hunger, int thirst) {
        super(x, y, name);
        this.health = health;
        this.hunger = hunger;
        this.thirst = thirst;
        this.isAlive = true;
    }

    // Method to simulate human actions
    public void performAction() {
        // Implement human-specific actions here
        if (isAlive) {
            // Example: Eating when hungry
            if (hunger > 0) {
                eat();
            }
            // Example: Drinking when thirsty
            if (thirst > 0) {
                drink();
            }
        }
    }

    // Example method: Eating
    public void eat() {
        // Increment health and reduce hunger
        health += 10;
        hunger -= 10;
        System.out.println(name + " is eating.");
    }

    // Example method: Drinking
    public void drink() {
        // Increment health and reduce thirst
        health += 5;
        thirst -= 10;
        System.out.println(name + " is drinking.");
    }

    // Getters and setters for additional fields
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
