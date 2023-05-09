package org.example;

import java.sql.*;


/**
 * Чтобы подключить JDBC-драйвер к проекту:
 * 1) File -> Project Structure -> Libraries
 * 2) На плюсе (+) выберите From Maven...
 * 3) В поиске найдите (search) драйвер,
 * соответствующий вашей базе данных:
 * - mysql:mysql-connector-java:8.0.30 [или старшая версия]
 * - com.oracle.database.jdbc:ojdbc11:23.2.0.0 [или старшая версия ojdbc]
 * - org.postgresql:postgresql:42.6.0  [или старшая версия]
 * 4) И подтвердите подключение - ОК -> Apply
 *
 * Чтобы развернуть резервную копию базы данных:
 * 1) Резервная копия базы данных приложена в resources/backup.sql
 * 2) Подключитесь к локальному серверу PostgreSQL через PgAdmin (или терминал)
 * 3) Создайте пустую базу данных headHunter
 * 4) Восстановите в ней приложенную резервную копию базы данных
 *
 * Чтобы осуществить запрос к базе данных необходимо:
 * 1) Открыть соединение с базой данных - {@link java.sql.Connection}
 * 2) Определить оператор для SQL-запроса - {@link java.sql.Statement} или его дочерние классы
 * 2.1) При необходимости, вложить данные в SQL-запрос
 * 3) Выполнить SQL-запрос и в зависимости от типа проанализировать результат:
 * 3.1) Простой запрос - executeQuery - результат запроса {@link java.sql.ResultSet}
 * 3.2) Запрос на изменение - executeUpdate - результат запроса представляет собой количество измененных строк
 * (также можно затребовать возвращаемые данные и считать из через {@link java.sql.ResultSet})
 */
public class SimpleJDBC {

    public static void main(String[] args) {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost/headHunter";
        String user = "postgres";       // имя пользователя по умолчанию
        String password = "postgres";   // пароль к серверу базы данных
        // FIXME если при установке сервера PostgreSQL у вас другой пароль, то измените значение для переменной 'password'

        try {
            Class.forName(driver);
            System.out.println("Connected JDBC Driver");
        } catch (ClassNotFoundException e) {
            // FIXME Вы забыли подключить JDBC драйвер
            System.out.println("JDBC Driver is not found. Include it in your library path ");
            throw new RuntimeException("JDBC Driver is not found. Include it in your library path");
        }
        System.out.println();

        Connection connection =  null;  // соединение
        Statement statement = null;     // оператор запроса
        ResultSet rs = null;            // результат запроса
        try {
            connection = DriverManager.getConnection(url, user, password); // открытие соединения с базой данных

            // region 1) Список всех злодеев (поиск)
            System.out.println("Список всех злодеев: ");
            statement = connection.createStatement();     // оператор запроса
            rs = statement.executeQuery("SELECT name FROM villain;"); // результат запроса на поиск

            while (rs.next()){  // пока есть данные
                System.out.println(rs.getString("name"));   // вывести значение атрибута, заданного по имени
//                System.out.println(rs.getString(1));        // или по индексу, начиная с 1

//                System.out.println(rs.getInt("name"));          // error - невозможно привести значение атрибута к заданному типу
//                System.out.println(rs.getString("nickname"));   // error - нет такого атрибута
            }
            System.out.println();
            // endregion

            // region 2) Описание конкретного злодея (поиск по параметру)
            System.out.println("Описание конкретного злодея: ");
            statement = connection.prepareStatement("SELECT * FROM villain WHERE name = ?;"); // оператор запроса с "включаемыми" параметрами - ?
            ((PreparedStatement) statement).setString(1, "Грю"); // добавление значений в запрос с учетом их типа; индексация с 1
            rs = ((PreparedStatement) statement).executeQuery(); // результат запроса на поиск

            while (rs.next()){
                System.out.print(rs.getString("name") + ", ");
                System.out.print(rs.getString("nickname") + ", ");
                System.out.println(rs.getInt("evilness"));
            }
            System.out.println();
            // endregion

            // region 3) Описание конкретного пособника (описание возвращаемой схемы)
            System.out.println("Описание возвращаемой схемы для конкретного пособника: ");
            statement = connection.prepareStatement("SELECT * FROM minion WHERE name = ?;"); // оператор запроса с "включаемыми" параметрами - ?
            ((PreparedStatement) statement).setString(1, "Боб"); // добавление значений в запрос с учетом их типа; индексация с 1
            rs = ((PreparedStatement) statement).executeQuery(); // результат запроса на поиск

            // Возможно уточнить схему отношений из @ResultSetMetaData
            int count = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++){
                System.out.println(
                        "label - " + rs.getMetaData().getColumnLabel(i) +
                        ", type - " + rs.getMetaData().getColumnType(i) +
                        ", typeName - " + rs.getMetaData().getColumnTypeName(i) +
                        ", class - " + rs.getMetaData().getColumnClassName(i) +
                        ", size - " + rs.getMetaData().getColumnDisplaySize(i)
                );
            }
            System.out.println();
            // endregion

            // region 4) Заключение контракта (добавление)
            System.out.println("Заключение контракта:");
            statement = connection.createStatement();   // оператор запроса
            int rows = statement.executeUpdate(         // запрос на обновление
                    "INSERT INTO contract (\"nameVillain\", \"nameMinion\") VALUES ('Грю', 'Доктор Нефарио') returning number;",
                    Statement.RETURN_GENERATED_KEYS);   // и возвращение ключа (или массива атрибутов, заданных по индексам или именам)
            rs = statement.getGeneratedKeys();
            int number = 0;

            while (rs.next()) {
                number = rs.getInt(1);
            }
            System.out.println("Добавлено записей: " + rows);
            System.out.println("number = " + number);
            System.out.println();
            // endregion

            // region 5) Перенос даты начала контракта (изменение)
            System.out.println("Перенос даты начала контракта:");
            statement = connection.createStatement();     // исполнение запроса
            rs = statement.executeQuery("SELECT start FROM contract WHERE \"nameVillain\"='Грю' AND \"nameMinion\"='Доктор Нефарио';");
            Date startDate = null;

            while (rs.next()) {
                startDate = rs.getDate(1);  // узнаем вписанное значение даты
                startDate.setYear(startDate.getYear() - 2);
            }

            rows = statement.executeUpdate(
                    "UPDATE contract SET start=\'" + startDate + "\' WHERE \"nameVillain\"='Грю' AND \"nameMinion\"='Доктор Нефарио';",
                    4);
            rs = statement.getGeneratedKeys();
            while (rs.next()) {
                startDate = rs.getDate(4);
            }

            System.out.println("Обновлено записей: " + rows);
            System.out.println("startDate =  " + startDate);
            System.out.println();
            // endregion

            // region 6) Разрыв контракта (удаление)
            System.out.println("Разрыв контракта:");
            statement = connection.createStatement();     // исполнение запроса
            rows = statement.executeUpdate("DELETE FROM contract WHERE \"nameVillain\"='Грю' AND \"nameMinion\"='Доктор Нефарио';");
            System.out.println("Удалено записей: " + rows);
            System.out.println();
            // endregion

            // закрываем соединение с базой данных
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
//        finally { // или так закрываем соединение с базой данных, но close() также выбрасывает SQLException
//            if (statement != null) statement.close();
//            if (connection != null) connection.close();
//        }
    }
}
