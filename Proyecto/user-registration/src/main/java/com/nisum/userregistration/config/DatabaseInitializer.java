package com.nisum.userregistration.config;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void initialize() {
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			String createTriggerSQL = "CREATE TRIGGER trg_users_before_insert " + "BEFORE INSERT ON USERS "
					+ "FOR EACH ROW " + "BEGIN " + "    IF NEW.ID_USER IS NULL THEN "
					+ "        SET NEW.ID_USER = NEXT VALUE FOR user_seq; " + "    END IF; " + "END;";

			statement.execute(createTriggerSQL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
