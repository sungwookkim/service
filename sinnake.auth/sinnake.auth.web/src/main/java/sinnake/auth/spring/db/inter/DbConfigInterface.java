package sinnake.auth.spring.db.inter;

import javax.sql.DataSource;

/**
 * DB 접속에 대한 공용 인터페이스
 *
 * @author sinnakeWEB
 */
public interface DbConfigInterface {
	/**
	 * DB 접속 밴더에 따른 각기 다른 구현을 위한 메서드.
	 *
	 * @author sinnakeWEB
	 * @return DataSource
	 * @throws Exception 예외 발생.
	 */
	public DataSource connConfig() throws Exception;
}
