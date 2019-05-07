package com.auth.domain.infra.oauth.process.oauthSave.impl;

import com.auth.domain.entity.SpringOauth;
import com.auth.domain.infra.oauth.process.oauthSave.OauthSave;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

/**
 * oauth 저장 비지니스로 로직 구현 클래스
 * 
 * @author sinnakeWEB
 */
public class OauthSaveImpl implements OauthSave {

	private SpringOauth springOauth;

	public OauthSaveImpl(SpringOauth springOauth) {
		this.springOauth = springOauth;
	}
	
	/**
	 * 저장 프로세스 메서드
	 * 
	 * @author sinnakeWEB
	 * @return ResultEntity 결과 응답 값
	 */
	@Override
	public ResultEntity<SpringOauth> process() {
		ResultEntity<SpringOauth> resultEntity = this.validate();
		
		if("1".equals(resultEntity.getCode()) ) {
			resultEntity = new ResultEntity<>(resultEntity.getCode(), this.springOauth);
		} else {
			resultEntity = new ResultEntity<>(resultEntity.getCode());
		}
		
		return resultEntity;
	}

	/**
	 * 검증 프로세스 메서드
	 * 
	 * @author sinnakeWEB
	 * @return ResultEntity 결과 응답 값
	 */
	@Override
	public ResultEntity<SpringOauth> validate() {
		ResultEntity<SpringOauth> resultEntity = new ResultEntity<>("1");

		if(!new SinnakeValidate(this.springOauth.getClient_id())
			.required().getValidResult()) {
			resultEntity = new ResultEntity<>("-1");
		}

		return resultEntity;
	}
}
