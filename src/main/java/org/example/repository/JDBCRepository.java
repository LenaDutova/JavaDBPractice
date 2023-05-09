package org.example.repository;

import org.example.Main;
import org.example.dto.Minion;
import org.example.dto.Villain;
import org.example.dto.Villains;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.example.Main.*;

public class JDBCRepository
        implements DBRepository {

    private static final String SEARCH_VILLAINS = "SELECT name FROM villain;";
    private static final String SEARCH_VILLAIN_BY_NAME = "SELECT nickname, evilness, \"nameMinion\", weakness, \"eyeCount\"" +
            "FROM villain AS x " +
            "LEFT JOIN contract ON x.name = \"nameVillain\" " +
            "LEFT JOIN minion ON \"nameMinion\" = minion.name " +
            "WHERE x.name = ?;";
    private static final String SEARCH_FREE_MINIONS = "SELECT name, \"eyeCount\", weakness FROM minion WHERE NOT EXISTS (SELECT \"nameMinion\" FROM contract WHERE contract.\"nameMinion\" = minion.name);";
    private static final String ADD_CONTRACT = "INSERT INTO contract (\"nameVillain\", \"nameMinion\") VALUES (?, ?);";
    private static final String REMOVE_CONTRACT = "DELETE FROM contract WHERE \"nameVillain\" = ? AND \"nameMinion\" = ?;";

//    public static final JDBCManager INSTANCE = new JDBCManager(); // простой Singleton, для обязательных в использовании объектов
    private static JDBCRepository INSTANCE;    // Singleton, с "ленивой" инициализацией (по требованию)

    /**
     * Приватный конструктор
     */
    private JDBCRepository() {
        checkDriver();
    }

    /**
     * По требованию создает единственный экземпляр этого класса,
     * или возвращает ссылку на него
     *
     * @return возвращает ссылку на экземпляр этого класса
     */
    public static synchronized JDBCRepository getInstance() {
        if (INSTANCE == null){
            INSTANCE = new JDBCRepository();
        }
        return INSTANCE;
    }

    /**
     * Проверка подключения драйвера jdbc базы данных к проекту
     *
     * Чтобы подключить драйвер к проекту:
     * 1) File -> Project Structure -> Libraries
     * 2) На плюсе (+) выберите From Maven...
     * 3) В поиске найдите (search) драйвер,
     * соответствующий вашей базе данных:
     * - mysql:mysql-connector-java:8.0.30 [или старшая версия]
     * - com.oracle.database.jdbc:ojdbc11:23.2.0.0 [или старшая версия ojdbc]
     * - org.postgresql:postgresql:42.6.0  [или старшая версия]
     * 4) И подтвердите подключение - ОК -> Apply
     *
     * @throws RuntimeException в случае, если вы забыли подключить драйвер к проекту
     */
    private void checkDriver () {
        try {
            Class.forName(Main.DATABASE.getDriver());
            Main.log(this, "Connected JDBC Driver - " + Main.DATABASE.getDriver());
        } catch (ClassNotFoundException e) {
            Main.log(this, "JDBC Driver is not found. Include it in your library path ");
            throw new RuntimeException(e);
        }
    }

    /**
     * Открытие соединения с базой данных
     *
     * @return ссылка на открытое соединение
     */
    private Connection openConnection () throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
    }

    @Override
    public Set<Villains> getVillains() {
        Set<Villains> villains = null;

        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(SEARCH_VILLAINS);

            while (rs.next()) {
                if (villains == null) villains = new HashSet<Villains>();

                String name = rs.getString("name");
                villains.add(new Villains(name));
            }
        } catch (SQLException e) {
            Main.log(this, "SQLState: " + ((SQLException)e).getSQLState());
            Main.log(this, "Message: " + e.getMessage());
            Main.log(this, "Something wrong with query - " + SEARCH_VILLAINS);
            e.printStackTrace();
        } catch (NullPointerException e){
            Main.log(this, "Something wrong with connection to database");
        }

        return villains;
    }

    @Override
    public Villain getVillain(String name) {
        // проверка на дурака
//        if (name == null || name.isBlank()){
//            Main.log(this, "Need villain name");
//            return null;
//        }

        Villain dto = null;
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_VILLAIN_BY_NAME)) {
            statement.setString(1, name); // индексация с 1

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (dto == null) {
                    String nickname = rs.getString("nickname");
                    int evilness = rs.getInt("evilness");
                    dto = new Villain(name, nickname, evilness);
                }

                // а есть ли пособнички?
                if (rs.getString("nameMinion") != null) {
                    String minionName = rs.getString("nameMinion");
                    String weakness = rs.getString("weakness");
                    int eyeCount = rs.getInt("eyeCount");
                    dto.addMinion(new Minion(minionName, weakness, eyeCount));
                }
            }
        } catch (SQLException e) {
            Main.log(this, "SQLState: " +
                    ((SQLException)e).getSQLState());
            Main.log(this, "Message: " + e.getMessage());
            Main.log(this, "Something wrong with query - " + SEARCH_VILLAIN_BY_NAME);
            e.printStackTrace();
        } catch (NullPointerException e){
            Main.log(this, "Something wrong with connection to database");
        }
        return dto;
    }

    @Override
    public Set<Minion> getFreeMinions() {
        Set<Minion> minions = null;

        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(SEARCH_FREE_MINIONS);

            System.out.println();

            while (rs.next()) {
                if (minions == null) minions = new HashSet<Minion>();

                String name = rs.getString("name");
                String weakness = rs.getString("weakness");
                int eyesCount = rs.getInt("eyeCount");
                minions.add(new Minion(name, weakness, eyesCount));
            }
        } catch (SQLException e) {
            Main.log(this, "Something wrong with query - " + SEARCH_FREE_MINIONS);
            Main.log(this, "SQLState: " +
                    ((SQLException)e).getSQLState());
            Main.log(this, "Error Code: " +
                    ((SQLException)e).getErrorCode());
            Main.log(this, "Message: " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e){
            Main.log(this, "Something wrong with connection to database");
        }

        return minions;
    }

    @Override
    public boolean addContract(Villain villain, Minion... minions) {
        if (villain == null){
            Main.log(this, "Need villain to make contract with him");
            return false;
        }

        if (minions.length == 0 || (minions.length == 1 && minions[0] == null)){
            Main.log(this, "Need minion(s) to create a contract");
            return false;
        }

        int counter = minions.length;
        for (Minion minion: minions) {
            if (minion != null) {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement(ADD_CONTRACT)) {

                    statement.setString(1, villain.getName());
                    statement.setString(2, minion.getName());
                    counter -= statement.executeUpdate();
                } catch (SQLException e) {
                    if (e.getSQLState().equalsIgnoreCase("23505")){
                        Main.log(this, "Contract between " + villain.getName() + " and " + minion.getName() + " already concluded");
                    } else {
                        Main.log(this, "SQLState: " + ((SQLException) e).getSQLState());
                        Main.log(this, "Message: " + e.getMessage());
                        Main.log(this, "Something wrong with query - " + ADD_CONTRACT);
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    Main.log(this, "Something wrong with connection to database");
                }
            } else {
                Main.log(this, "Need pair to create contract");
            }
        }

        if (counter == 0){
            Main.log(this, "All contracts was created");
            return true;
        } else return false;
    }

    @Override
    public boolean removeContract(Villain villain, Minion... minions) {
        if (villain == null){
            Main.log(this, "Need villain to remove contract with him");
            return false;
        }
        if (minions.length == 0 || (minions.length == 1 && minions[0] == null)){
            Main.log(this, "Need minion to remove contract with him");
            return false;
        }

        int counter = minions.length;
        for (Minion minion: minions) {
            if (minion != null) {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement(REMOVE_CONTRACT)) {

                    statement.setString(1, villain.getName());
                    statement.setString(2, minion.getName());
                    int count = statement.executeUpdate();
                    if (count == 0) Main.log(this, "Contract between " + villain.getName() + " and " + minion.getName() + " already broken or not concluded");
                    else counter -= count;
                } catch (SQLException e) {
                    Main.log(this, "SQLState: " + ((SQLException) e).getSQLState());
                    Main.log(this, "Message: " + e.getMessage());
                    Main.log(this, "Something wrong with query - " + REMOVE_CONTRACT);
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Main.log(this, "Something wrong with connection to database");
                }
            } else {
                Main.log(this, "Need pair to create contract");
            }
        }

        if (counter == 0){
            Main.log(this, "All contracts was removed");
            return true;
        } else return false;
    }
}
