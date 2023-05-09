package org.example.repository;

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

    public String getProtocol() {
        return protocol;
    }

    public String getDriver() {
        return driver;
    }
}