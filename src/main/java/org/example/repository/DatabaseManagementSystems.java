package org.example.repository;

import java.sql.SQLException;

public enum DatabaseManagementSystems {

    POSTGRES_SQL("jdbc:postgresql://", "org.postgresql.Driver"),
    MY_SQL("jdbc:mysql://" ,"org.mysql.Driver"),
    ORACLE("jdbc:oracle:thin:@", "oracle.jdbc.driver.OracleDriver");

    private String protocol = "jdbc:default:connection:";
    private String driver;

    DatabaseManagementSystems(String protocol, String driver) {
        this.protocol = protocol;
        this.driver = driver;
    }

    /**
     * протокол адреса подключения (URL) для выбранной СУБД
     *
     * @return протокол адреса
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * JDBC-драйвер, соответствующий выбранной СУБД
     *
     * @return имя JDBC-драйвера
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Согласно стандарту SQL:2008 в ситуациях нарушения
     * ограничений уникальности (в т.ч. дублирования данных)
     * возникают ошибки соответствующие статусу (или дочерние ему):
     * SQLState 23000 - Integrity Constraint Violation
     *
     * @param sql возникшая ошибка
     * @return true, если это ошибка дублирования данных
     */
    public boolean isDuplicateInsert (SQLException sql){
        if (sql.getSQLState().startsWith("23")) return true;
        return false;
    }
}