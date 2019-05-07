package sinnake.auth.spring.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * Spring Security에서 사용될 User 커스텀 엔티티
 * 
 * @author sinnakeWEB
 */
public class User implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = 420L;
	private Long memberId;
	private String password;
	private final String username;
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;
	private int attempts;
	private Date lastModified;
	
	/**
	 * @author sinnakeWEB
	 * @param memberId 사용자 채번 값
	 * @param username 사용자 ID
	 * @param password 사용자 비밀번호
	 * @param enabled 사용자 사용가능 여부 값
	 * @param accountNonExpired 사용자 계정 만료 여부 값
	 * @param credentialsNonExpired 사용자 비밀번호 만료 여부 값
	 * @param accountNonLocked 로그인 시도 실패로 인한 잠금 여부 값
	 * @param attempts 비밀번호 실패 횟수 값
	 * @param lastModified 사용자 로그인 날짜
	 * @param authorities 사용자 부여된 권한 값
	 */
	public User(Long memberId, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, int attempts, Date lastModified,
			Collection<? extends GrantedAuthority> authorities) {
		if (username != null && !"".equals(username) && password != null) {
			this.memberId = memberId;
			this.username = username;
			this.password = password;
			this.enabled = enabled;
			this.accountNonExpired = accountNonExpired;
			this.credentialsNonExpired = credentialsNonExpired;
			this.accountNonLocked = accountNonLocked;
			this.attempts = attempts;
			this.lastModified = lastModified;
			this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
		} else {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
	}
	
	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");

		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(
				new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
					"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}
	
	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
		
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			if (g2.getAuthority() == null) {
				return -1;
			}
		
			if (g1.getAuthority() == null) {
				return 1;
			}
		
			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}
	
	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public int getAttempts() {
		return attempts;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public Long getMemberId() {
		return this.memberId;		
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public boolean equals(Object rhs) {
		return rhs instanceof org.springframework.security.core.userdetails.User ? this.username.equals(((User) rhs).username) : false;
	}

	@Override
	public int hashCode() {
		return this.username.hashCode();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("MemberID: ").append(this.memberId).append("; ");
		sb.append("Username: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
		sb.append("attempts: ").append(this.attempts).append("; ");
		sb.append("lastModified: ").append(this.lastModified).append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");
		if (!this.authorities.isEmpty()) {
			sb.append("Granted Authorities: ");
			boolean first = true;
			Iterator<GrantedAuthority> var3 = this.authorities.iterator();

			while (var3.hasNext()) {
				GrantedAuthority auth = (GrantedAuthority) var3.next();
				if (!first) {
					sb.append(",");
				}

				first = false;
				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	public static UserBuilder withUsername(String username) {
		return new UserBuilder().username(username);
	}
	
	public static class UserBuilder {
		private Long memberId;
		private String username;
		private String password;
		private List<GrantedAuthority> authorities;
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;
		private int attempts;
		private Date lastModified;
		
		private UserBuilder() { }

		private UserBuilder username(String username) {
			Assert.notNull(username, "username cannot be null");
			this.username = username;
			return this;
		}

		public UserBuilder memberId(Long memberId) {
			Assert.notNull(memberId, "memberId cannot be null");
			this.memberId = memberId;
			return this;
		}

		public UserBuilder password(String password) {
			Assert.notNull(password, "password cannot be null");
			this.password = password;
			return this;
		}

		public UserBuilder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
					roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"), role
						+ " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		public UserBuilder authorities(GrantedAuthority... authorities) {
			return authorities(Arrays.asList(authorities));
		}

		public UserBuilder authorities(List<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<GrantedAuthority>(authorities);
			return this;
		}

		public UserBuilder authorities(String... authorities) {
			return authorities(AuthorityUtils.createAuthorityList(authorities));
		}

		public UserBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		public UserBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		public UserBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		public UserBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public UserBuilder attempts(int attempts) {
			this.attempts = attempts;
			return this;
		}
		
		public UserBuilder lastModified(Date lastModified) {
			this.lastModified = lastModified;
			return this;
		}
		
		public UserDetails build() {
			return new User(memberId, username, password, !disabled, !accountExpired,
					!credentialsExpired, !accountLocked, attempts, lastModified, authorities);
		}
	}	
}
