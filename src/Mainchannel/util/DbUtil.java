//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbUtil {
    private String url;
    private String user;
    private String pass;
    private String driver;

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.pass);
    }

    public Statement createStatement() throws SQLException {
        return this.connect().createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connect().prepareStatement(sql);
    }

    public DbUtil(String fileName) {
        try {
            Properties e = new Properties();
            e.load(new FileInputStream(fileName));
            this.url = e.getProperty("url");
            this.user = e.getProperty("user");
            this.pass = e.getProperty("pass");
            this.driver = e.getProperty("driver");
            Class.forName(this.driver);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
