import java.util.Random;

class Dragon {
    private static Random rnd = new Random();

    private int healthPoint; // количество жизни 2000
    private int strength; // сила 120
    private int defence; // защита 150
    private int weapon; // оружие 100

    public Dragon(int healthPoint, int strength, int defence, int weapon) {
        this.healthPoint = healthPoint;
        this.strength = strength;
        this.defence = defence;
        this.weapon = weapon;
    }

    int getHealthPoint() {
        if (healthPoint <= 0) {
            return 0;
        } else {
            return healthPoint;
        }
    }

    void damage(int damage) { // получение урона
        healthPoint -= Math.max(damage - defence, 0);
        if (healthPoint < 0) {
            healthPoint = 0;
        }
    }

    void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    int getDefence() {
        return defence;
    }

    int attack() { //расчет урона
        if(rnd.nextInt(2) == 0) {
            return 0;
        } else {
            return strength + weapon;
        }
    }

    int fireball() {
        return strength * 2;
    }
}
