package com.domain.service.command.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.domain.entity.Temporary;
import com.domain.repo.command.domain.TemporaryCommandRepository;
import com.domain.repo.read.domain.TemporaryReadRepository;
import com.sinnake.entity.ResultEntity;

import util.RestProcess;

/**
 * 템플릿 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class TemporaryCommandService {

	private TemporaryCommandRepository temporaryCommandRepository;
	private TemporaryReadRepository temporaryReadRepository;
	
	@Autowired
	public TemporaryCommandService(TemporaryCommandRepository temporaryCommandRepository
		, TemporaryReadRepository temporaryReadRepository) {
		
		this.temporaryCommandRepository = temporaryCommandRepository;
		this.temporaryReadRepository = temporaryReadRepository;
	}
	
	/**
	 * 템플릿  추가 서비스
	 * 
	 * @author sinnakeWEB
	 * @param text 임시 명 
	 * @return 템플릿 결과 조회 값
	 */
	@Transactional(transactionManager = "temporaryTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Long> temporaryAdd(String text) {
		
		return new RestProcess<Long>()
			.call(() -> {

				ResultEntity<Temporary> temporary = new Temporary().add(text);

				if(temporary.sucess()) {
					this.temporaryCommandRepository.save(temporary.getResult());

					return new ResultEntity<>(ResultEntity.sucessCodeString()
						,temporary.getResult().getId());
				}
								
				return new ResultEntity<>(ResultEntity.failCodeString()
					, Long.parseLong(temporary.getCode()) );
			})
			.fail(e -> {

				return new ResultEntity<>(ResultEntity.failCodeString(), -99L);
			})
			.exec();
	}

	@Transactional(transactionManager = "temporaryTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Long> temporaryUpdate(Long id, String text) {
		
		return new RestProcess<Long>()
			.call(() -> {

				ResultEntity<Temporary> temporary = new Temporary()
					.update(() -> this.temporaryReadRepository.findId(id), text);

				if(temporary.sucess()) {
					this.temporaryCommandRepository.save(temporary.getResult());

					return new ResultEntity<>(ResultEntity.sucessCodeString()
						,temporary.getResult().getId());
				}
								
				return new ResultEntity<>(ResultEntity.failCodeString()
					, Long.parseLong(temporary.getCode()) );
			})
			.fail(e -> {

				return new ResultEntity<>(ResultEntity.failCodeString(), -99L);
			})
			.exec();
	}
	
}
