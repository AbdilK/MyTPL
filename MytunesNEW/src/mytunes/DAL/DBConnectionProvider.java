/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;

/**
 *
 * @author Abdil-K
 */
public class DBConnectionProvider
{
    private static final String SERVER_NAME = "10.176.111.31";
    private static final String DATABASE_NAME = "MyTunes1234";
    private static final String USER = "CS2018A_5";
    private static final String PASSWORD = "CS2018A_5";
           
    private final SQLServerDataSource db;

    public DBConnectionProvider() 
    {
        db = new SQLServerDataSource();
        db.setServerName(SERVER_NAME);
        db.setDatabaseName(DATABASE_NAME);
        db.setUser(USER);
        db.setPassword(PASSWORD);
    }

    public Connection getConnection() throws SQLServerException
    {
        Connection con = db.getConnection();
        return con;
    }

}
