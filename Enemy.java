import java.util.Random;

class Enemy {
    public static Random rnd = new Random(); // Лучше сделать non-static и передавать в конструктор или в методы,
                                            // но для простоты игры можно оставить static пока.

    private int hp;        // здоровье
    private int str;       // сила (базовая атака)
    private int def;       // защита
    private int wp;        // оружие (дополнительная атака)
    private int sh;        // щит (дополнительная защита при поднятом щите)
    private int pc;        // зелья
    private boolean shSt;  // статус щита (поднят/опущен)
    private int enTp;      // Тип сущности (0-Герой, 1-Дракон, 2-Адская гончая)

    // Константы для типов сущностей (для лучшей читаемости)
    public static final int TYPE_HERO = 0;
    public static final int TYPE_DRAGON = 1;
    public static final int TYPE_HELLHOUND = 2;

    // Основной/полный конструктор
    public Enemy(int hp, int str, int def, int wp, int sh, int pc, int enTp) {
        this.hp = hp;
        this.str = str;
        this.def = def;
        this.wp = wp;
        this.sh = sh;
        this.pc = pc;
        this.enTp = enTp;
        this.shSt = false; // Щит по умолчанию опущен
    }

    // Перегруженный конструктор для противников (без щита и зелий по умолчанию)
    public Enemy(int hp, int str, int def, int wp, int enTp) {
        this(hp, str, def, wp, 0, 0, enTp); // Вызов основного конструктора
    }

    // --- Геттеры и Сеттеры ---
    public int getHp() {
        return hp;
    }

    // Сеттер HP должен проверять, чтобы HP не уходило в минус
    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getWp() {
        return wp;
    }

    public void setWp(int wp) {
        this.wp = wp;
    }

    public int getSh() {
        return sh;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public boolean getShStatus() {
        return shSt;
    }

    public void setShStatus(boolean shStatus) {
        this.shSt = shStatus;
    }

    public int getEnTp() {
        return enTp;
    }

    // --- Методы игровой логики ---

    // Метод для получения общего урона сущности при атаке
    public int getAttackDamage() {
        return str + wp;
    }

    // Метод для получения урона от фаербола (только для Дракона)
    public int getFireballDamage() {
        return str * 2;
    }

    // Универсальный метод для получения урона (заменяет getDmg и getDmgFromFrBl)
    // incomingBaseDamage - базовый урон от атакующего
    // isFireballAttack - флаг, является ли атака фаерболом
    public void takeDamage(int incomingBaseDamage, boolean isFireballAttack) {
        int finalDamage;

        if (isFireballAttack) {
            // Фаербол: если щит поднят, урон 0, иначе - полный базовый урон фаербола.
            // Дефенс НЕ влияет на фаербол, согласно вашей логике
            if (shSt) {
                finalDamage = 0;
            } else {
                finalDamage = incomingBaseDamage;
            }
        } else {
            // Обычная атака:
            if (shSt) {
                // Если щит поднят, учитываем защиту и щит
                finalDamage = Math.max(0, incomingBaseDamage - (def + sh));
            } else {
                // Если щит опущен, учитываем только защиту
                finalDamage = Math.max(0, incomingBaseDamage - def);
            }
        }

        this.setHp(this.hp - finalDamage); // Используем setHp, чтобы автоматически обрезать до 0

        System.out.println(" | " + getEnemyTypeName() + " получил " + finalDamage + " урона. Текущее HP: " + this.hp);
    }


    // Метод для использования зелья
    public void usePc() {
        if (pc > 0) {
            hp += 500; // Увеличение HP
            pc -= 1;   // Уменьшение количества зелий
            System.out.println("Герой выпил зелье. "); // Сообщение здесь
        } else {
            System.out.println("Зелий нет!");
        }
    }

    // Метод для проверки вероятности (промах/бездействие)
    // Возвращает true, если событие (промах/бездействие) происходит.
    // Если rnd.nextInt(min, max+1) > avg, то событие НЕ происходит, возвращаем false.
    // Если rnd.nextInt(min, max+1) <= avg, то событие происходит, возвращаем true.
    public boolean checkProbability(int min, int avg, int max) {
        // avg - это значение, которое нужно преодолеть для НЕ-события.
        // Если случайное число <= avg, то событие (например, промах или бездействие) происходит.
        return rnd.nextInt(min, max + 1) <= avg;
    }

    // Метод для получения строкового имени сущности
    public String getEnemyTypeName() {
        return switch (enTp) {
            case TYPE_HERO -> "Герой";
            case TYPE_DRAGON -> "Дракон";
            case TYPE_HELLHOUND -> "Адская гончая";
            default -> "Неизвестная сущность";
        };
    }

    // Метод для вывода статистики сущности
    // Теперь не нужно передавать enemy, т.к. это метод самого объекта
    public void printEnSt() {
        System.out.println("=== EnSt ===");
        System.out.printf("Enemy : %s%n", getEnemyTypeName()); // Используем новый метод
        System.out.println("hp  : " + hp);
        System.out.println("str : " + str);
        System.out.println("def : " + def);
        System.out.println("wp  : " + wp);
        if (enTp == TYPE_HERO) { // Дополнительные поля только для героя
            System.out.println("sh  : " + sh);
            System.out.println("pc  : " + pc);
        }
    }
}