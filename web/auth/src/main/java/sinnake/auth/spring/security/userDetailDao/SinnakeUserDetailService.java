package sinnake.auth.spring.security.userDetailDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;

import sinnake.auth.spring.security.entity.User;

/**
 * 로그인 관련 사용자 정보 클래스.
 * 
 * @author sinnakeWEB
 */
@Service
public class SinnakeUserDetailService extends JdbcDaoImpl {

	/**
	 * 
	 * @param dataSource dataSoruce 객체
	 * @param sqlUsersByUsernameQuery 사용자 조회 쿼리
	 * @param sqlAuthoritiesByUsernameQuery 사용자 권한 쿼리
	 * @throws Exception 예외 발생
	 */
	@Autowired
	public SinnakeUserDetailService(DataSource authDataSource
		, @Value("#{authServerProp['auth.sqlUsersByUsernameQuery']}") String sqlUsersByUsernameQuery
		, @Value("#{authServerProp['auth.sqlAuthoritiesByUsernameQuery']}") String sqlAuthoritiesByUsernameQuery) throws Exception {
		super.setDataSource(authDataSource);
		super.setUsersByUsernameQuery(sqlUsersByUsernameQuery);
		super.setAuthoritiesByUsernameQuery(sqlAuthoritiesByUsernameQuery);
	}
		
	/**
	 * 사용자 조회 메서드 재정의
	 * 
	 * @author sinnakeWEB
	 * @param username 사용자 ID
	 * @return Map 
	 */
	@Override
	public List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(super.getUsersByUsernameQuery(), 
				new String[] { username }, new RowMapper<UserDetails>() {			
					@Override
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						Long memberId = rs.getLong("member_id");
						String username = rs.getString("username");
						String password = rs.getString("password");
						boolean enabled = rs.getBoolean("enabled");
						boolean accountNonExpired = rs.getBoolean("accountNonExpired");
						boolean credentialsNonExpired = rs.getBoolean("credentialsNonExpired");
						boolean accountNonLocked = rs.getBoolean("accountNonLocked");
						int attempts = rs.getInt("attempts");
						Date lastmodified = rs.getTimestamp("lastmodified");
												
						return new User(memberId, username, password, enabled, accountNonExpired
								, credentialsNonExpired, accountNonLocked, attempts, lastmodified, AuthorityUtils.NO_AUTHORITIES);
					}
				});
	}
	
	/**
	 * 로그인 성공 시 계정 정보 설정 메소드 재정의
	 * 
	 * @author sinnakeWEB
	 * @param username 사용자 ID
	 * @param userFromUserQuery 인증에 성공한 사용자 정보
	 * @param combinedAuthorities 사용자 권한 모음
	 * @return UserDetails 시스템에서 사용할 최종 UserDetails
	 */
	@Override
	public UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
		User user = (User) userFromUserQuery;
		
		String returnUsername = user.getUsername();

		if (!super.isUsernameBasedPrimaryKey()) {
			returnUsername = username;
		}
		
		return new User(user.getMemberId(), returnUsername, user.getPassword()
				, user.isEnabled(), user.isAccountNonExpired()
				, user.isCredentialsNonExpired(), user.isAccountNonLocked()
				, user.getAttempts(), user.getLastModified()
				, combinedAuthorities);
	}
	
}
