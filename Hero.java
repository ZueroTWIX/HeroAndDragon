import java.util.Random;

class Hero {
    private static Random rnd = new Random();

    private int healthPoint; // количество жизни 1000
    private int strength; // сила 100
    private int defence; // защита 120
    private int weapon; // оружие 250
    private int shield; // щит 150
    private int potionCount; //кол-во зелий
    private boolean isShieldUP;

    public Hero(int healthPoint, int strength, int defence, int weapon, int shield, int potionCount) {
        this.healthPoint = healthPoint;
        this.strength = strength;
        this.defence = defence;
        this.weapon = weapon;
        this.shield = shield;
        this.potionCount = potionCount;
    }

    int attack() { // расчет урона
        if (rnd.nextInt(4) == 0) {
            return 0;
        } else {
            return strength + weapon;
        }
    }

    void damage(int damage) { // получение урона
        if (isShieldUP == true) {
            healthPoint -= Math.max(damage - (defence + shield), 0);
        } else {
            healthPoint -= Math.max(damage - defence, 0);
        }
        if (healthPoint < 0) {
            healthPoint = 0;
        }
    }

    void damageToFireboll(int damage) {
            healthPoint -= damage;
        
    }

    int getDefence() {
        if (isShieldUP == true) {
            return defence + shield;
        } else {
            return defence;
        }
    }

    int getHealthPoint() {
        return healthPoint;
    }

    void setShieldStatus(boolean status) {
        isShieldUP = status;
    }

    boolean getShieldStatus() {
        return isShieldUP;
    }

    void modifyHealth() {
        if(potionCount > 0) {
            healthPoint += 500;
            potionCount -= 1;
        } else {
            System.out.println(" | У вас нет зелий!");
        }
    }

    int getPointCount() {
        return potionCount;
    }
}
