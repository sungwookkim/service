package com.member.domain.entity.member;

import java.util.Date;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 회원 비밀번호 횟수 관련 엔티티
 * 
 * @author sinnakeWEB
 */
@Entity(name = "member_attempts")
public class MemberAttempts {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", unique = true)
	Member member;
	
	@Id
	@Column(name = "member_attempts_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(name = "attempts", length = 45, nullable = false)
	Integer attempts = 0;
	
	@Column(name = "lastModified", nullable = false)
	Date lastModified = new Date();
	
	public MemberAttempts() { }

	public MemberAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Integer getId() { return id; }
	public Integer getAttempts() { return attempts; }
	public Date getLastModified() { return lastModified; }
	
	public Member getMember() { return member; }
	public void setMember(Member member) {
		Optional.ofNullable(this.member)
			.ifPresent(m -> m.getMemberAttempts().remove(this));
		
		this.member = member;
		
		Optional.ofNullable(member)
			.map(Member::getMemberAttempts)
			.filter(ma -> !ma.contains(this))
			.ifPresent(ma -> ma.add(this));
	}

}