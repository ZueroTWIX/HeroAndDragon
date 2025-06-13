import java.util.Random;
import java.util.Scanner;

public class heroDragon {
    // Используем константу TYPE_HERO из Enemy
    private static Enemy hero = new Enemy(1000, 100, 120, 250, 150, 1, Enemy.TYPE_HERO);

    private static Scanner sc = new Scanner(System.in);
    private static Random rnd = new Random();

    public static void main(String[] args) {
        Enemy enemy = selectEnemy(rnd.nextInt(2)); // rnd.nextInt(2) даст 0 или 1

        while (true) { // Бесконечный цикл, выход будет через break
            // Проверка на конец игры в начале каждого раунда
            if (isGameOver(enemy)) {
                break;
            }

            System.out.println("================================");
            System.out.println("Перед вами : ");
            enemy.printEnSt(); // Вызываем метод без передачи параметра
            System.out.println();
            System.out.println("Ваш персонаж : ");
            hero.printEnSt(); // Вызываем метод без передачи параметра
            System.out.println();

            System.out.println("=== Ход Героя ==="); // Исправлена опечатка Tutn
            menu(enemy); // Герой делает свой ход

            // !!! ВАЖНО: Проверяем Game Over СРАЗУ ПОСЛЕ ХОДА ГЕРОЯ !!!
            if (isGameOver(enemy)) {
                break; // Если враг умер от атаки героя, завершаем игру немедленно
            }

            System.out.println();
            System.out.println("=== Ход Противника ===");
            enemyTurn(enemy); // Противник делает свой ход
            // Проверка на Game Over после хода противника уже сделана в начале следующей итерации while
        }
        sc.close(); // Важно закрыть Scanner
        System.out.println("Игра завершена!");
    }

    private static void menu(Enemy enemy) {
        hero.setShStatus(false); // Сбрасываем статус щита героя в начале его хода
        System.out.println("=== Меню ==="); // Исправлена опечатка
        System.out.println("1 - Атаковать");
        System.out.println("2 - Бездействовать");
        System.out.println("3 - Защищаться");
        System.out.println("4 - Выпить зелье");
        System.out.println("0 - Выход");
        switch (userInput()) {
            case 0:
                System.out.println("Выход из игры...");
                System.exit(0);
                break;
            case 1:
                heroAttack(enemy);
                break;
            case 2:
                System.out.println("Герой бездействует!");
                break;
            case 3:
                hero.setShStatus(true);
                System.out.println("Герой защищает себя щитом!"); // Исправлена опечатка
                break;
            case 4:
                hero.usePc(); // usePc теперь сам печатает сообщение
                System.out.println("Текущее HP Героя: " + hero.getHp()); // Дополнительный вывод для ясности
                break;
            default:
                System.out.println("Непредвиденная ошибка ввода!");
                break;
        }
    }

    private static int userInput() {
        while (true) { // Бесконечный цикл, пока не будет валидный ввод
            System.out.print("Ввод: ");
            if (sc.hasNextInt()) {
                int userInput = sc.nextInt();
                sc.nextLine(); // Очищаем буфер после nextInt()
                if (userInput >= 0 && userInput <= 4) { // Проверяем диапазон
                    return userInput;
                } else {
                    System.out.println("Введите число от 0 до 4!");
                }
            } else {
                System.out.println("Введите целое число!");
                sc.nextLine(); // Очищаем буфер от некорректного ввода
            }
        }
    }

    private static void heroAttack(Enemy enemy) {
        // chPr(0, 1, 3) - если случайное число (0,1,2,3) <= 1, то true (промах). Это 2/4 = 50% шанс.
        if (hero.checkProbability(0, 1, 3)) { // Если промах
            System.out.println("Герой промахнулся!");
        } else {
            // Герой атакует врага. isFireball = false, так как герой не использует фаербол.
            dealDamage(hero, enemy, false);
        }
    }

    // Универсальный метод для нанесения урона
    // attacker - тот, кто атакует
    // defender - тот, кто получает урон
    // isFireballAttack - флаг, является ли атака "фаерболом" (для спец. логики Дракона)
    private static void dealDamage(Enemy attacker, Enemy defender, boolean isFireballAttack) {
        int baseDamage = attacker.getAttackDamage(); // Получаем базовый урон атакующего

        if (isFireballAttack) {
            baseDamage = attacker.getFireballDamage(); // Если это фаербол, берем урон фаербола
        }

        //defender.takeDamage() сам обрабатывает защиту и щит
        defender.takeDamage(baseDamage, isFireballAttack);
    }

    private static Enemy selectEnemy(int rndValue) { // выбор противника
        if (rndValue == 0) {
            // Дракон
            return new Enemy(2000, 120, 150, 100, Enemy.TYPE_DRAGON);
        } else {
            // Адская гончая
            return new Enemy(800, 150, 200, 150, Enemy.TYPE_HELLHOUND);
        }
    }

    private static boolean isGameOver(Enemy enemy) { // проверка на победителя
        String fmt = "%s победил!%n";
        if (hero.getHp() <= 0) { // Проверка, что HP героя 0 или меньше
            System.out.printf(fmt, enemy.getEnemyTypeName()); // Используем новое имя метода
            return true;
        } else if (enemy.getHp() <= 0) { // Проверка, что HP врага 0 или меньше
            System.out.printf(fmt, hero.getEnemyTypeName()); // Используем новое имя метода
            return true;
        } else {
            return false;
        }
    }

    private static void enemyTurn(Enemy enemy) {
        if (enemy.getHp() <= 0) { // !!! ВАЖНО: Проверяем, жив ли враг, перед его ходом !!!
            return; // Если враг уже мертв, он не делает ход
        }

        // enemy.chPr(0, 1, 2) - если случайное число (0,1,2) <= 1, то true (бездействие). Это 2/3 = 66% шанс.
        if (enemy.checkProbability(0, 1, 2)) { // Если враг бездействует
            System.out.printf("%s бездействует!%n", enemy.getEnemyTypeName());
        } else {
            boolean isFireballAttack = false;
            // Если враг - Дракон, проверяем шанс на фаербол
            if (enemy.getEnTp() == Enemy.TYPE_DRAGON) {
                // enemy.chPr(0, 1, 2) - если случайное число (0,1,3) <= 1, то true (фаербол). Это 1/2 = 50% шанс.
                if (enemy.checkProbability(0, 1, 3)) { // Если Дракон использует фаербол
                    isFireballAttack = true;
                    // Сообщение о фаерболе теперь здесь, а не в геттере
                    System.out.println("Дракон плюнул фаербол!");
                }
            }
            // Враг атакует героя
            dealDamage(enemy, hero, isFireballAttack);
        }
    }
}