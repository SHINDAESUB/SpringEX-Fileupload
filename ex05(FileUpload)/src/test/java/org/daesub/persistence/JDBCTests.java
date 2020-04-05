package org.daesub.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class JDBCTests {

	static {
		try {
			Class.forName("Oracle.jdbc.driver.OracleDriver");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testConnection() {
		
		try (Connection con=
				DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:orcl",
						"ds_admin",
						"ds_admin"
						)	
							){
			log.info(con);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
}



