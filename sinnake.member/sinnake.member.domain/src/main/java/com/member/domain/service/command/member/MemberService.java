package com.member.domain.service.command.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.member.domain.entity.member.Member;
import com.member.domain.repo.command.member.MemberCommandRepository;
import com.member.domain.repo.read.member.MemberReadRepository;
import com.sinnake.entity.ResultEntity;

import config.serverList.ServerListProp;
import config.serverList.ServerListProp.ServerList;
import util.RestProcess;

/**
 * 회원 Service 클래스
 * 
 * @author sinnakeWEB
 */
@Service
public class MemberService {

	private MemberCommandRepository memberCommandRepository;
	private MemberReadRepository memberReadRepository;
	private RestTemplate restTemplate;
	private String pwKey;
	private ServerListProp serverListProp;
	private ServerList oauthServerInfo;
	
	@Autowired
	public MemberService(MemberCommandRepository memberCommandRepository
		, MemberReadRepository memberReadRepository
		, RestTemplate restTemplate
		, @Value("#{serverProp['pwAes256.key']}") String pwKey
		, ServerListProp serverListProp) {

		this.memberCommandRepository = memberCommandRepository;		
		this.memberReadRepository = memberReadRepository;
		this.restTemplate = restTemplate;
		this.pwKey = pwKey;
		this.serverListProp = serverListProp;	
		
		this.oauthServerInfo  = this.serverListProp.getServerList("oauthServer");
	}

	/**
	 * 회원 수정 메서드
	 * 
	 * @author sinnakeWEB
	 * @param userName 회원 ID
	 * @param password 회원 비밀번호
	 * @param rePassword 회원 비밀번호 재확인
	 * @param address 주소
	 * @param detailAddress 상세주소
	 * @param postCode 우편번호
	 * @return 결과 값
	 * @throws Exception 예외 발생
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Member> signUpdate(String userName, String password, String rePassword
		, String address, String detailAddress, String postCode) throws Exception {

		ResultEntity<Member> resultEntity = new Member().signUpdate(pwKey
			, userName
			, password
			, rePassword
			, address
			, detailAddress
			, postCode
			, memberReadRepository::findName);
		
		String code = resultEntity.getCode();

		if("1".equals(resultEntity.getCode())) {
			this.memberCommandRepository.save(resultEntity.getResult());
			resultEntity = new ResultEntity<>(code);
		}
		
		return resultEntity;
	}
	
	/**
	 * 회원가입 메서드
	 * 
	 * @param userName 회원 ID
	 * @param password 회원 비밀번호
	 * @param rePassword 회원 비밀번호 재확인
	 * @param address 주소
	 * @param detailAddress 상세주소
	 * @param postCode 우편번호
	 * @return 결과 값
	 * @throws Exception 예외 발생
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Member> signUp(String userName, String password, String rePassword, String address
		, String detailAddress, String postCode) throws Exception {

		ResultEntity<Member> resultEntity = new Member().signUp(this.pwKey
			, userName
			, password
			, rePassword
			, address
			, detailAddress
			, postCode
			, memberReadRepository::findName);
		
		if("1".equals(resultEntity.getCode())) {
			this.memberCommandRepository.save(resultEntity.getResult());	

			resultEntity = new RestProcess<Member>()
				.call(() -> {
					String rtn = this.restTemplate.postForObject(this.oauthServerInfo.getHttpFullAddress("saveOauth")
						, new HashMap<String ,String>() {
							private static final long serialVersionUID = 1L;

							{
								put("client_id", userName);
							}
						}
						, String.class);
					
					@SuppressWarnings("unchecked")
					HashMap<String, String> val = Optional.ofNullable(new Gson().fromJson(rtn, HashMap.class)).orElse(new HashMap<>());					
					
					if(!"1".equals(Optional.of(val.get("code")).get())) {
						throw new RuntimeException();
					}

					return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode());
				}).fail(() -> {
					HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(new HashMap<String ,String>() {
						private static final long serialVersionUID = 1L;

						{
							put("client_id", userName);
						}
					});
					
					this.restTemplate.exchange(this.oauthServerInfo.getHttpFullAddress("delOauth")
						, HttpMethod.DELETE
						, httpEntity
						, String.class);
					
					return new ResultEntity<>(ResultEntity.ResultCode.FAIL.getCode());
				}).exec();

		}

		return resultEntity;
	}
}
