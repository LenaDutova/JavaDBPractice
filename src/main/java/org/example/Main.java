package org.example;

import org.example.db.DBManager;
import org.example.db.DatabaseManagementSystems;
import org.example.db.JDBCManager;
import org.example.dto.Minion;
import org.example.dto.Villain;
import org.example.dto.Villains;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * Проект создаем со сборщиком Maven
 * (в связи с тем, что в Университете
 * не работает gradle)
 *
 * Чтобы развернуть резервную копию базы данных:
 * 1) Резервная копия базы данных приложена в resources/backup.sql
 * 2) Подключитесь к локальному серверу PostgreSQL через PgAdmin (или терминал)
 * 3) Создайте пустую базу данных headHunter
 * 4) Восстановите в ней приложенную резервную копию базы данных
 */
public class Main {

    private static final String URL_LOCALE_NAME = "localhost/";         // ваш компьютер
    private static final String URL_LOCALE_ADDRESS = "127.0.0.1:5432/"; // и это тоже ваш локальный компьютер
    private static final String URL_REMOTE = "10.242.64.207:5432/";     // IP-адрес кафедрального сервера

    private static final String DATABASE_NAME = "headHunter";   // имя базы

    public static final String USER_NAME = "postgres";          // имя пользователя
    public static final String DATABASE_PASS = "admin";         // пароль базы данных
//    public static final String DATABASE_PASS = "postgres";

    private static final boolean DEBUG = true;
    private static final String URL = URL_LOCALE_NAME;
    public static final DatabaseManagementSystems DATABASE = DatabaseManagementSystems.POSTGRES_SQL;
    public static final String DATABASE_URL = DATABASE.getProtocol() + URL + DATABASE_NAME;

    public static void main(String[] args) {
        Optional<Villains> optional1 = Optional.of(new Villains("a"));
        Optional<Villains> optional2 = Optional.empty();
        checkJdbc();
    }

    public static void log(Object obj, String msg){
        if (DEBUG) System.out.println(obj.getClass().getSimpleName() + ": " + msg);
    }

    // region // testing

    private static void checkJdbc(){
        DBManager manager = JDBCManager.getInstance();

        System.out.println(manager.getVillains());

        Villain dto =
//                manager.getVillain("Джокер");
                manager.getVillain("Том Реддл");
        System.out.println(dto);

        Set<Minion> minions = manager.getFreeMinions();
        System.out.println(minions);

        Minion[] choosen = minions.stream()
                .filter(minion ->
                        minion.getName().equalsIgnoreCase("Питер Питегрю") ||
                        minion.getName().equalsIgnoreCase("Профессор Квирелл"))
                .toArray(Minion[] :: new);
        System.out.println(Arrays.toString(choosen));
        System.out.println();

        manager.addContract(dto, choosen);
        manager.addContract(dto, choosen);
        System.out.println();

        manager.removeContract(dto, choosen);
        manager.removeContract(dto, choosen);
    }

    // endregion
}