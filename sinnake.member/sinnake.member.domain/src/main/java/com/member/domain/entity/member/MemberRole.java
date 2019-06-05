package com.member.domain.entity.member;

import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.member.domain.entity.member.enumPack.MemberRoleEnum;

/**
 * 회원 룰 엔티티
 * 
 * @author sinnakeWEB
 */
@Entity(name = "user_roles")
public class MemberRole {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id")
	Member member;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_role_id")
	Long id;
		
	@Enumerated(value = EnumType.STRING)
	@Column(name = "role", nullable = false)
	MemberRoleEnum memberRoleEnum;
	
	public MemberRole() { }
	
	public MemberRole(MemberRoleEnum memberRoleEnum) {
		this.memberRoleEnum = memberRoleEnum;
	}

	public Long getId() { return id; }
	public MemberRoleEnum getMemberRole() { return memberRoleEnum; }
	
	public Member getMember() { return member;}
	public void setMember(Member member) {
		Optional.ofNullable(this.member)
			.ifPresent(m -> m.getMemberRole().remove(this));
		
		this.member = member;
		
		Optional.ofNullable(member)
			.map(Member::getMemberRole)
			.filter(mr -> !mr.contains(this))
			.ifPresent(mr -> mr.add(this));		
	}
}