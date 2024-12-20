package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
@CrossOrigin
public abstract class MySqlDaoBase {
    private final DataSource dataSource;
@Autowired
    public MySqlDaoBase(DataSource dataSource) { // Ensure this constructor exists
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
