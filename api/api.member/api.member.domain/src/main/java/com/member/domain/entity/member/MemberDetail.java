package com.member.domain.entity.member;

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
 * 회원 상세정보 엔티티
 * 
 * @author sinnakeWEB
 */
@Entity(name = "member_detail")
public class MemberDetail {
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", unique = true)
	Member member;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_detail_id")
	Long id;
	
	@Column(name = "address", length = 100, nullable = false)
	String address;
	
	@Column(name = "detail_address", length = 100, nullable = false)
	String detailAddress;
	
	@Column(name = "postCode", length = 15, nullable = false)
	String postCode;
	
	public MemberDetail() { }
	
	public MemberDetail(String address, String detailAddress, String postCode) {
		this.address = address;
		this.detailAddress = detailAddress;
		this.postCode = postCode;
	}

	public Long getId() { return id; }
	public String getAddress() { return address; }
	public String getDetailAddress() { return detailAddress; }
	public String getPostCode() { return postCode; }
	
	public void setAddress(String address) { this.address = address; }
	public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress;}
	public void setPostCode(String postCode) { this.postCode = postCode; }

	public Member getMember() { return member; }
	public void setMember(Member member) {
		Optional.ofNullable(this.member)
			.ifPresent(m -> m.getMemberDetail().remove(this));
		
		this.member = member;
		
		Optional.ofNullable(member)
			.map(Member::getMemberDetail)
			.filter(md -> !md.contains(this))
			.ifPresent(md -> md.add(this));
	}
}
