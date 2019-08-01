package com.domain.service.read.domain;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.domain.entity.Temporary;
import com.domain.repo.read.domain.TemporaryReadRepository;

/**
 * 템플릿 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class TemporaryReadService {
	
	private TemporaryReadRepository temporaryReadRepository;
	
	@Autowired
	public TemporaryReadService(TemporaryReadRepository temporaryReadRepository) {

		this.temporaryReadRepository = temporaryReadRepository;
	}
	
	/**
	 * 템플릿 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 템플릿 Seq 값
	 * @return 템플릿 조회 결과 값
	 */
	@Transactional(transactionManager = "temporaryTransactionManager")
	public Map<String, Object> findTemporary(Long id) {
		 
		return new Temporary().get().findTemporary(this.temporaryReadRepository::findId, id);
	}
}
