package com.auth.domain.service.command.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auth.domain.entity.SpringOauth;
import com.auth.domain.infra.oauth.process.oauthDel.OauthDel;
import com.auth.domain.infra.oauth.process.oauthDel.impl.OauthDelImpl;
import com.auth.domain.repo.command.oauth.OauthCommandRepository;
import com.auth.domain.repo.read.oauth.OauthReadRepository;
import com.sinnake.entity.ResultEntity;

/**
 * oauth service 클래스
 * 
 * @author sinnakeWEB
 */
@Service
public class OauthService {

	private OauthCommandRepository oauthCommandRepository;
	private OauthReadRepository oauthReadRepository;
	
	@Autowired
	public OauthService(OauthCommandRepository oauthCommandRepository
		, OauthReadRepository oauthReadRepository) {

		this.oauthCommandRepository = oauthCommandRepository;
		this.oauthReadRepository = oauthReadRepository;
	}
	
	/**
	 * oauth 저장
	 * 
	 * @author sinnakeWEB
	 * @param client_id 사용자 ID
	 * @return ResultEntity
	 */
	@Transactional(transactionManager="authTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<SpringOauth> oauthSave(String client_id) {

		ResultEntity<SpringOauth> resultEntity = new SpringOauth(client_id, "ROLE_USER").oauthSave();
		
		String code = resultEntity.getCode();
		if("1".equals(code)) {
			this.oauthCommandRepository.save(resultEntity.getResult());
			resultEntity = new ResultEntity<>(code);
		}
		
		return resultEntity;
	}
	
	/**
	 * oauth 삭제
	 * 
	 * @author sinnakeWEB
	 * @param client_id 사용자 ID
	 * @return ResultEntity
	 */
	@Transactional(transactionManager="authTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<SpringOauth> oauthDel(String client_id) {

		OauthDel oauthDel = new OauthDelImpl(client_id, this.oauthReadRepository::findId);
		ResultEntity<SpringOauth> resultEntity = SpringOauth.oauthDel(oauthDel);
		
		String code = resultEntity.getCode();
		if("1".equals(code)) {
			this.oauthCommandRepository.remove(resultEntity.getResult());
			resultEntity = new ResultEntity<>(code);
		}
		
		return resultEntity;
	}
}
