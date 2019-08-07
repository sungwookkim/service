package com.member.domain.entity.member;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.member.domain.infra.member.process.signUpdate.impl.SignUpdateImpl;
import com.member.domain.infra.member.process.signup.SignUp;
import com.member.domain.infra.member.process.signup.impl.SignUpImpl;
import com.sinnake.entity.ResultEntity;

/**
 * 회원 도메인
 * 
 * @author sinnakeWEB
 */
@Entity(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	Long id;
		
	@Column(name = "username", length = 45, unique = true, nullable = false)
	String userName;
	
	@Column(name = "password", length = 45, nullable = false)
	String password;
	
	@Column(name = "enabled", length = 4, nullable = false)
	Integer enabled = 1;
	
	@Column(name = "accountNonExpired", length = 4, nullable = false)
	Integer accountNonExpired = 1;
	
	@Column(name = "accountNonLocked", length = 4, nullable = false)
	Integer accountNonLocked = 1;
	
	@Column(name = "credentialsNonExpired", length = 4, nullable = false)
	Integer credentialsNonExpired = 1;
	
	@Column(name = "regDate", nullable = false)
	Date regDate = new Date();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
	Set<MemberDetail> memberDetails = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
	Set<MemberRole> memberRoles = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
	Set<MemberAttempts> memberAttempts = new HashSet<>();

	/**********/
	/* 생성자 */
	/**********/
	public Member() {}

	public Member(String userName, String password, Integer enabled, Integer accountNonExpired
			, Integer accountNonLocked, Integer credentialsNonExpired) {
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Member(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/***********************/
	/* Getter, Setter 메소드 */
	/***********************/
	public Long getId() { return id; }
	public String getUserName() { return userName; }
	public String getPassword() { return password; }
	public Integer getEnabled() { return enabled; }
	public Integer getAccountNonExpired() { return accountNonExpired; }
	public Integer getAccountNonLocked() { return accountNonLocked; }
	public Integer getCredentialsNonExpired() { return credentialsNonExpired; }
	public Date getRegDate() { return regDate; }

	public void setPassword(String password) { this.password = password; }
	
	/**************/
	/* 연관 편의 메소드 */
	/*************/
	public Set<MemberDetail> getMemberDetail() { return memberDetails; }
	public void addMemberDetail(MemberDetail memberDetail) {
		Optional.ofNullable(this.memberDetails)
			.filter(md -> !md.contains(memberDetail))
			.ifPresent(md -> md.add(memberDetail));
		
		Optional.ofNullable(memberDetail)			
			.map(m -> Optional.ofNullable(m.getMember())
				.map(mm -> mm)
				.orElseGet(Member::new))
			.filter(m -> m != this)
			.ifPresent(m -> memberDetail.setMember(this));			
	}
	
	public Set<MemberRole> getMemberRole() { return memberRoles; }
	public void addMemberRole(MemberRole memberRole) {
		Optional.ofNullable(this.memberRoles)
			.filter(mr -> !mr.contains(memberRole))
			.ifPresent(mr -> mr.add(memberRole));
		
		Optional.ofNullable(memberRole)
			.map(m -> Optional.ofNullable(m.getMember())
				.map(mm -> mm)
				.orElseGet(Member::new))
			.filter(m -> m != this)
			.ifPresent(m -> memberRole.setMember(this));
	}
	
	public Set<MemberAttempts> getMemberAttempts() { return memberAttempts; }
	public void addMemberAttempts(MemberAttempts memberAttempts) {
		Optional.ofNullable(this.memberAttempts)
			.filter(ma -> !ma.contains(memberAttempts))
			.ifPresent(ma -> ma.add(memberAttempts));
		
		Optional.ofNullable(memberAttempts)
			.map(m -> Optional.ofNullable(m.getMember())
				.map(mm -> mm)
				.orElseGet(Member::new))
			.filter(m -> m != this)
			.ifPresent(m -> memberAttempts.setMember(this));
	}
	
	/********/
	/* 프로세스*/
	/********/
	/**
	 * 회원가입 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param pwKey 비밀번호 암호화 키
	 * @param userName 사용자 ID
	 * @param password 사용자 패스워드
	 * @param rePassword 사용자 패스워드
	 * @param address 주소
	 * @param detailAddress 상세 주소
	 * @param postCode 우편번호
	 * @param users 사용자 조회 람다
	 * @return 결과 값
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Member> signUp(String pwKey, String userName, String password, String rePassword
		, String address, String detailAddress, String postCode, Function<String, List<Member>> users) throws Exception {

		SignUp signUp = new SignUpImpl(pwKey, userName, password, rePassword, address, detailAddress, postCode, users);

		return this.sign(signUp);
	}

	/**
	 * 회원정보 수정 프로세스
	 * 
	 * @author sinnakeWEB 
	 * @param pwKey 비밀번호 암호화 키
	 * @param userName 사용자 ID
	 * @param password 비밀번호
	 * @param rePassword 비밀번호
	 * @param address 주소
	 * @param detailAddress 상세주소
	 * @param postCode 우편번호
	 * @param users 사용자 조회 람다
	 * @return 결과 값
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Member> signUpdate(String pwKey, String userName, String password, String rePassword
			, String address, String detailAddress, String postCode, Function<String, List<Member>> users) throws Exception {

		SignUp signUpdate = new SignUpdateImpl(pwKey, userName, password, rePassword, address, detailAddress, postCode, users);

		return this.sign(signUpdate);
	}
	
	/**
	 * 사용자 가입 및 수정 커스텀 프로세스
	 * 
	 * @param signUp
	 * @return 결과 값
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Member> sign(SignUp signUp) throws Exception {
		return signUp.process();		
	}

}
