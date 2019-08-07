package db.inter;

import javax.sql.DataSource;

/**
 * DB 접속 정보 인터페이스
 * 
 * @author sinnakeWEB
 */
public interface DbConfigInterface {
	public DataSource connConfig() throws Exception;
}
