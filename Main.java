import java.util.Random;
import java.util.Scanner;

public class Main {
    private static Hero hero = new Hero(1000, 100, 120, 250, 150, 1);
    private static Dragon dragon = new Dragon(2000, 120, 150, 100);
    private static Hellhound hellhound = new Hellhound(800, 100, 250, 20);
    private static Scanner sc = new Scanner(System.in);
    private static Random rnd = new Random();
    private static boolean isGameOver = false;
    private static String enemy;
    private static String enemyUp;
    private static String ending;

    public static void main(String[] args) {
        if (rnd.nextInt(2) == 0) {
            enemyUp = "Дракон";
            enemy = "дракон";
            ending = "дракона";
        } else {
            enemyUp = "Адская гончая";
            enemy = "адская гончая";
            ending = "адской гончей";
        }
        while (!isGameOver) {
            readPlayerInput();
            if (enemy.equals("дракон")) {
                if (dragon.getHealthPoint() != 0) {
                    dragonAttack();
                }
            } else {
                if (hellhound.getHealthPoint() != 0) {
                    hellhoundAttack();
                }
            }
        }
        if (dragon.getHealthPoint() == 0) {
            System.out.println("Герой победил!");
        } else if (hero.getHealthPoint() == 0) {
            System.out.printf("%s победил!%n", enemyUp);
        }
        System.out.println("Конец игры!");
    }

    public static void readPlayerInput() { // считывает ввод и делает ход
        hero.setShieldStatus(false);
        System.out.println("===== player turn =====");
        System.out.printf(" | Перед вами %s!%n", enemy);
        System.out.println(" | Состояние героя");
        System.out.println(" | Здоровье : " + hero.getHealthPoint());
        System.out.println(" | Кол-во зелий : " + hero.getPointCount());

        System.out.println(" | Выбирите действие : ");
        System.out.println(" | 1.Атакавать");
        System.out.println(" | 2.Ничего не делать!");
        System.out.println(" | 3.Поднять щит.");
        System.out.println(" | 4.Выпить зелье.");
        System.out.print(" | Ввод: ");
        boolean isUserInputValid = false;
        while (!isUserInputValid) {
            if (sc.hasNextInt()) {
                int userInput = sc.nextInt();
                sc.nextLine();
                switch (userInput) { // тут в новых кейсах будут новые варианты ходов
                    case 0:
                        System.exit(0);
                        break;

                    case 1:
                        System.out.println(" | Атакую!"); // прописать реализацию
                        if (enemy.equals("дракон")) {
                            playerAttackDragon();
                        } else {
                            playerAttackHellhound();
                        }
                        isUserInputValid = true;
                        break;

                    case 2:
                        System.out.println(" | Герой ничего не делает!");
                        isUserInputValid = true;
                        break;

                    case 3:
                        System.out.println(" | Герой защищает себя щитом!");
                        hero.setShieldStatus(true);
                        isUserInputValid = true;
                        break;

                    case 4:
                        System.out.println(" | Герой выпивает зелье.");
                        hero.modifyHealth();
                        System.out.println(" | Здоровье героя: " + hero.getHealthPoint());
                        isUserInputValid = true;
                        break;

                    default:
                        System.out.println(" | Ошибка! Выбирите одно из действий.");
                        break;
                }
            } else {
                System.out.println(" | Ошибка! Введите натуральное число.");
                sc.nextLine();
            }
        }
    }

    public static void playerAttackDragon() { // ход атаки героя
        System.out.println("===== player turn =====");
        int damage = hero.attack();
        if (damage == 0) {
            System.out.println(" | Промах!");
        } else {
            dragon.damage(damage);
            System.out.printf(" | Попадание! Нанесено %d урона.%n", Math.max(damage - dragon.getDefence(), 0));
            if (dragon.getHealthPoint() == 0) {
                System.out.printf(" | %s убит!%n", enemyUp);
                isGameOver = true;
            } else {
                System.out.printf(" | У %s осталось %d HP%n", ending, dragon.getHealthPoint());
            }
        }
    }

    public static void playerAttackHellhound() { // ход атаки героя
        System.out.println("===== player turn =====");
        int damage = hero.attack();
        if (damage == 0) {
            System.out.println(" | Промах!");
        } else {
            hellhound.damage(damage);
            System.out.printf(" | Попадание! Нанесено %d урона.%n", Math.max(damage - hellhound.getDefence(), 0));
            if (hellhound.getHealthPoint() == 0) {
                System.out.printf(" | %s убит!%n", enemyUp);
                isGameOver = true;
            } else {
                System.out.printf(" | У %s осталось %d HP%n", ending, hellhound.getHealthPoint());
            }
        }
    }

    public static void dragonAttack() { // ход атаки дракона
        System.out.println("===== dragon turn =====");
        int damage = dragon.attack();
        if (damage == 0) {
            System.out.printf(" | %s бездействует!%n", enemyUp);
        } else {
            if (rnd.nextInt(2) == 0) {
                System.out.println(" | Дракон плюет в героя огненный шар!");
                System.out.println("Ожидаемый урон: " + dragon.fireball());
                if (hero.getShieldStatus() == false) {
                    damage = dragon.fireball();
                    hero.damageToFireboll(damage);
                } else {
                    damage = 0;
                }
                System.out.printf(" | %s наносит %d урона!%n", enemyUp, damage);
            } else {
                hero.damage(damage);
                System.out.printf(" | %s наносит %d урона!%n", enemyUp, Math.max(damage - hero.getDefence(), 0));
            }
            if (hero.getHealthPoint() == 0) {
                System.out.println(" | Герой убит!");
                isGameOver = true;
            } else {
                System.out.printf(" | У героя осталось %d HP%n", hero.getHealthPoint());
            }
        }
    }

    public static void hellhoundAttack() { // ход атаки дракона
        System.out.println("===== hellhound turn =====");
        int damage = hellhound.attack();
        if (damage == 0) {
            System.out.printf(" | %s бездействует!%n", enemyUp);
        } else {
            hero.damage(damage);
            System.out.printf(" | %s наносит %d урона!%n", enemyUp, Math.max(damage - hero.getDefence(), 0));
            if (hero.getHealthPoint() == 0) {
                System.out.println(" | Герой убит!");
                isGameOver = true;
            } else {
                System.out.printf(" | У героя осталось %d HP%n", hero.getHealthPoint());
            }
        }
    }
}