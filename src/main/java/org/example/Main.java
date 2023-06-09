package org.example;

import org.example.repository.DBRepository;
import org.example.repository.DatabaseManagementSystems;
import org.example.repository.JDBCRepository;
import org.example.dto.*;

import java.util.Arrays;
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
//        checkJdbc();
    }

    public static void log(Object obj, String msg){
        if (DEBUG) System.out.println(obj.getClass().getSimpleName() + ": " + msg);
    }

    // region // testing

    private static void checkJdbc(){
        DBRepository manager = JDBCRepository.getInstance();

        Set<Villains> villains = manager.getVillains();
        System.out.println(villains);

        Villain dto =
//                manager.getVillain(new Villains("Джокер"));
                manager.getVillain(new Villains("Том Реддл"));
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