package com.member.domain.infra.member.process.signup;

import com.member.domain.entity.member.Member;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonProcess;

/**
 * 회원가입 인터페이스
 * 
 * @author sinnakeWEB
 */
public interface SignUp extends CommonProcess<Member> {

	public ResultEntity<Member> signUp() throws Exception;
}
