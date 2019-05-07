package com.auth.domain.infra.oauth.process.oauthDel.impl;

import java.util.Optional;
import java.util.function.Function;

import com.auth.domain.entity.SpringOauth;
import com.auth.domain.infra.oauth.process.oauthDel.OauthDel;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

/**
 * oauth 삭제 비지니스 로직 구현 클래스
 * 
 * @author sinnakeWEB
 */
public class OauthDelImpl implements OauthDel {

	private String client_id;
	private Function<String, SpringOauth> clients;
	
	/**
	 * @author sinnakeWEB
	 * @param client_id 사용자 ID
	 * @param clients 사용자 조회 로직 람다 표현식
	 */
	public OauthDelImpl(String client_id, Function<String, SpringOauth> clients) {
		this.client_id = client_id;
		this.clients = clients;
	}
	
	/**
	 * 삭제 프로세스 메서드
	 * 
	 * @author sinnakeWEB
	 * @return ResultEntity 결과 응답 값
	 */
	@Override
	public ResultEntity<SpringOauth> process() {
		return validate();
	}

	/**
	 * 검증 프로세스 메서드
	 * 
	 * @author sinnakeWEB
	 * @return ResultEntity 결과 응답 값
	 */
	@Override
	public ResultEntity<SpringOauth> validate() {		

		if(this.chkClientId(this.client_id)) {
			return new ResultEntity<>("-1");
		}
		
		return this.chkClientId(client_id, clients);
	}
	
	/**
	 * 사용자 ID 검증 메서드
	 * 
	 * @param client_id 사용자 ID
	 * @return boolean 검증 실패 여부
	 */
	protected boolean chkClientId(String client_id) {
		return !new SinnakeValidate(client_id).required().getValidResult();
	}

	/**
	 * 사용자 DB 존재 여부 검증 도메인 추출 메서드
	 * 
	 * @param client_id 사용자 ID
	 * @param clients 사용자 조회 로직 람다 표현식
	 * @return ResultEntity 결과 응답 값
	 */
	protected ResultEntity<SpringOauth> chkClientId(String client_id, Function<String, SpringOauth> clients) {
		SpringOauth springOauths = Optional.of(clients)
			.map(c -> c.apply(client_id))
			.get();
			
		return Optional.ofNullable(springOauths)
			.map(so -> new ResultEntity<>("1", so))
			.orElseGet(() -> new ResultEntity<>("-2"));
	}
}
