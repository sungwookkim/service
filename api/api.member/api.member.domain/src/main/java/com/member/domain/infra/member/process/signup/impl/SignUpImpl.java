package com.member.domain.infra.member.process.signup.impl;

import java.util.List;
import java.util.function.Function;

import com.member.domain.entity.member.Member;
import com.member.domain.entity.member.MemberAttempts;
import com.member.domain.entity.member.MemberDetail;
import com.member.domain.entity.member.MemberRole;
import com.member.domain.entity.member.enumPack.MemberRoleEnum;
import com.member.domain.infra.member.process.signup.SignUp;
import com.sinnake.entity.ResultEntity;

import util.SinnakeAES256Util;
import util.SinnakeValidate;

/**
 * 회원가입 구현체
 * 
 * @author sinnakeWEB
 */
public class SignUpImpl implements SignUp {
	private String pwKey;
	private String userName;
	private String password;
	private String rePassword;
	private String address;
	private String detailAddress;
	private String postCode;
	private Function<String, List<Member>> users;	

	public SignUpImpl(String pwKey, String userName, String password, String rePassword, String address, String detailAddress
			, String postCode, Function<String, List<Member>> users) { 
		
		this.pwKey = pwKey;
		this.userName = userName;
		this.password = password;
		this.rePassword = rePassword;
		this.address = address;
		this.detailAddress = detailAddress;
		this.postCode = postCode;
		this.users = users;		
	}

	/**
	 * 회원가입 메인 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Member> process() {
		
		ResultEntity<Member> resultEntity = validate();
		if(!resultEntity.sucess()) {
			return resultEntity; 
		}
		
		return this.signUp();
	}
	
	/**
	 * 회원가입 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Member> signUp() {

		String password;
		
		try {
			password = new SinnakeAES256Util(this.pwKey).aesEncode(this.password);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		Member member = new Member(this.userName, password);

		member.addMemberAttempts(new MemberAttempts());
		member.addMemberDetail(new MemberDetail(this.address, this.detailAddress, this.postCode));
		member.addMemberRole(new MemberRole(MemberRoleEnum.ROLE_USER));
		
		return new ResultEntity<>("1", member);
	}
	
	/**
	 * 회원가입 검증 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Member> validate() {
	
		if(this.chkId(this.userName, this.users)) {
			return new ResultEntity<>("-1");
		}
		
		if(this.chkPwd(this.password, this.rePassword)) {
			return new ResultEntity<>("-2");
		}
		
		if(this.chkAddress(this.address)) {
			return new ResultEntity<>("-3");
		}
		
		if(this.chkDetailAddress(this.detailAddress)) {
			return new ResultEntity<>("-4");
		}
		
		if(this.chkPostCode(this.postCode)) {
			return new ResultEntity<>("-5");
		}
		
		return new ResultEntity<>(ResultEntity.sucessCodeString());
	}
	
	/**
	 * 회원 ID 검증
	 * 
	 * @author sinnakeWEB
	 * @param userName 회원 ID
	 * @param users 회원 조회 람다
	 * @return 결과 값
	 */
	protected boolean chkId(String userName, Function<String, List<Member>> users) {

		boolean idValid = new SinnakeValidate(userName)
			.required()
			.blankValid()
			.minLen(5)
			.maxLen(16)
			.idValid()
			.getValidResult();
		
		if(users.apply(userName).size() > 0 || !idValid) { return true; }

		return false;
	}

	/**
	 * 회원 비밀번호 검증
	 * 
	 * @author sinnakeWEB
	 * @param pwd 비밀번호
	 * @param rePwd 비밀번호 재확인
	 * @return 결과 값
	 */
	protected boolean chkPwd(String pwd, String rePwd) {
		
		SinnakeValidate passwordValid = new SinnakeValidate(pwd)
			.minLen(8)
			.maxLen(10)
			.blankValid()
			.required().pwdValid();
		
		SinnakeValidate rePasswrodValid = new SinnakeValidate(rePwd)
			.minLen(8)
			.maxLen(10)
			.blankValid()
			.required().pwdValid();
		
		return !passwordValid.getValidResult()
			|| !rePasswrodValid.getValidResult()
			|| !passwordValid.equalTo(rePwd).getValidResult();
	}

	/**
	 * 주소 검증
	 * 
	 * @author sinnakeWEB
	 * @param address 회원 주소
	 * @return 결과 값
	 */
	protected boolean chkAddress(String address) {
		return !new SinnakeValidate(address).required().getValidResult();
	}
	
	/**
	 * 상세주소 검증
	 * 
	 * @author sinnakeWEB
	 * @param detailAddress 상세주소
	 * @return 결과 값
	 */
	protected boolean chkDetailAddress(String detailAddress) {
		return !new SinnakeValidate(detailAddress).required().getValidResult();	
	}
	
	/**
	 * 우편번호 검증
	 * 
	 * @author sinnakeWEB
	 * @param postCode 우편번호
	 * @return 결과 값
	 */
	protected boolean chkPostCode(String postCode) {
		return !new SinnakeValidate(postCode.replaceAll("-", ""))
			.required()
			.number()
			.getValidResult();
	}
	
	public String getPwKey() { return pwKey; }
	public String getUserName() { return userName; }
	public String getPassword() { return password; }
	public String getRePassword() { return rePassword; }
	public String getAddress() { return address; }
	public String getDetailAddress() { return detailAddress; }
	public String getPostCode() { return postCode; }
	public Function<String, List<Member>> getUsers() { return users; }
}
