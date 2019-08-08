package com.member.domain.infra.member.process.get.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.member.domain.entity.member.Member;
import com.member.domain.infra.member.process.get.MemberGet;

import commonInterface.CommonGet;

public class MemberGetImpl implements MemberGet {

	public MemberGetImpl() {}
	
	/**
	 * 회원 조회 프로세스(Map 형태로 반환)
	 * 
	 * @author sinnakeWEB
	 * @param findMember 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @param name 회원 ID
	 * @return 회원 조회 결과 값
	 */
	public Map<String, Object> findMember(Function<String, List<Member>> findMember, String name) {
		
		return Optional.ofNullable(CommonGet.<Member, Map<String, Object>>convert(findMember.apply(name).get(0)
			, MemberGetImpl::convertProcess))
			.orElseGet(HashMap::new);	
	}
	
	public static HashMap<String, Object> convertProcess(Member m) {
		HashMap<String, Object> member = new HashMap<>();
		
		member.put("id", m.getUserName());
		member.put("lastLoginDate", m.getMemberAttempts().stream()
			.map(v -> v.getLastModified())
			.findAny()
				.orElse(null));			
		member.put("address", m.getMemberDetail().stream()
			.map(v -> {
				HashMap<String, Object> memberAddress = new HashMap<>();
				memberAddress.put("address", v.getAddress());
				memberAddress.put("addressDetail", v.getDetailAddress());
				memberAddress.put("postCode", v.getPostCode());
				
				return memberAddress;
			})
			.findAny()
				.filter(v -> v.size() != 0)
				.orElseGet(HashMap::new));
		
		return member;
	}
}
