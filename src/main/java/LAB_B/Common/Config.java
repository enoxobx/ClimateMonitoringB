package LAB_B.Common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import LAB_B.Database.DatabaseImpl;

public class Config {
    private final String fileConfigPath = "config.properties";

    private String host;
    private int port;
    private String db_username;
    private String db_password;
    private String db_name;
    private int rmi_port;
    private String rmi_db_name;

    public Config() {
        try (InputStream input = DatabaseImpl.class.getClassLoader().getResourceAsStream(fileConfigPath)) {
            if (input == null) {
                System.err.println("File configurazione con trovato");
            } else {
                Properties conf = new Properties();
                conf.load(input);
                host = conf.getProperty("host");
                port = Integer.parseInt(conf.getProperty("port"));
                db_username = conf.getProperty("db.username");
                db_password = conf.getProperty("db.password");
                db_name = conf.getProperty("db.name");
                rmi_port = Integer.parseInt(conf.getProperty("rmi.port"));
                rmi_db_name = conf.getProperty("rmi.db.name");
            }
        } catch (IOException e) {
            System.err.println("configurazione non caricata");
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDbUsername() {
        return db_username;
    }

    public String getDbName() {
        return db_name;
    }

    public String getDbPassword() {
        return db_password;
    }

    public int getRmiPort() {
        return rmi_port;
    }

    public String getRmiDbName() {
        return rmi_db_name;
    }

    public String getUrlDb() {
        return "jdbc:postgresql://" + host + ":" + port + "/" + db_name;
    }

}
