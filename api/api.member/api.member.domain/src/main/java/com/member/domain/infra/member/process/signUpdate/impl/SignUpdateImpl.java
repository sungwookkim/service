package com.member.domain.infra.member.process.signUpdate.impl;

import java.util.List;
import java.util.function.Function;

import com.member.domain.entity.member.Member;
import com.member.domain.entity.member.MemberDetail;
import com.member.domain.infra.member.process.signUpdate.SignUpdate;
import com.member.domain.infra.member.process.signup.impl.SignUpImpl;
import com.sinnake.entity.ResultEntity;

import util.SinnakeAES256Util;
import util.SinnakeValidate;

/**
 * 회원수정 구현체
 * 
 * @author sinnakeWEB
 */
public class SignUpdateImpl extends SignUpImpl implements SignUpdate {

	private Member member;

	public SignUpdateImpl(String pwKey, String userName, String password, String rePassword, String address, String detailAddress
			, String postCode, Function<String, List<Member>> users) {

		super(pwKey, userName, password, rePassword, address, detailAddress, postCode, users);
	}

	/**
	 * 회원수정 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Member> signUp() {
		
		String password;
		
		try {
			password = new SinnakeAES256Util(this.getPwKey()).aesEncode(this.getPassword());
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		this.member.setPassword(password);
		
		MemberDetail memberDetail = this.member.getMemberDetail().iterator().next();
		memberDetail.setAddress(this.getAddress());
		memberDetail.setDetailAddress(this.getDetailAddress());
		memberDetail.setPostCode(this.getPostCode());
		
		return new ResultEntity<>(ResultEntity.sucessCodeString(), this.member);
	}
	
	/**
	 * 회원 ID 검증
	 * 
	 * @author sinnakeWEB
	 * @param userName 사용자 ID
	 * @param users 사용자 조회 람다
	 * @return 결과 값
	 */
	@Override
	protected boolean chkId(String userName, Function<String, List<Member>> users) {
		
		boolean idValid = new SinnakeValidate(userName)
			.required()
			.blankValid()
			.minLen(5)
			.maxLen(16)
			.idValid()
			.getValidResult();
		
		List<Member> members = users.apply(userName); 
		
		if(members.size() == 0 || !idValid) { return true; }
		
		this.member = members.get(0);

		return false;
	}
}
