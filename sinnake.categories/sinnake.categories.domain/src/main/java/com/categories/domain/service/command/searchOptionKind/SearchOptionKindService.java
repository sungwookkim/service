package com.categories.domain.service.command.searchOptionKind;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.repo.command.categories.SearchOptionKindCommandRepository;
import com.categories.domain.repo.read.categories.SearchOptionKindReadRepository;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonGet;
import util.RestProcess;

/**
 * 검색 옵션 종류 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class SearchOptionKindService {

	private SearchOptionKindCommandRepository searchOptionKindCommandRepository;
	private SearchOptionKindReadRepository searchOptionKindReadRepository;
	
	@Autowired
	public SearchOptionKindService(SearchOptionKindCommandRepository searchOptionKindCommandRepository
		, SearchOptionKindReadRepository searchOptionKindReadRepository) {
		
		this.searchOptionKindCommandRepository = searchOptionKindCommandRepository;
		this.searchOptionKindReadRepository = searchOptionKindReadRepository;
	}
	
	/**
	 * 검색옵션 종류 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 검색옵션 종류 Seq 값
	 * @return 검색옵션 종류 결과 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager")
	public Map<String, Object> findSearchOptionKind(Long id) {
		
		return new SearchOptionKind()
			.get().findSearchOptionKind(searchOptionKindReadRepository::findId, id);
	}
	
	/**
	 * 검색 옵션 종류 추가 서비스
	 * 
	 * @author sinnakeWEB
	 * @param searchOptionName 검색 옵션 종류 이름
	 * @return 검색 옵션 종류 결과 조회 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Long> searchOptionKindAdd(String searchOptionName) {
		
		return new RestProcess<Long>()
			.call(() -> {
				ResultEntity<SearchOptionKind> searchOptionKind = new SearchOptionKind().add(searchOptionName);
								
				String code = searchOptionKind.getCode();

				if(searchOptionKind.sucess()) {
					this.searchOptionKindCommandRepository.add(searchOptionKind.getResult());

					return new ResultEntity<>(code, searchOptionKind.getResult().getId());
				}
								
				return new ResultEntity<>(ResultEntity.failCodeString()
					, Long.parseLong(code) );
			})
			.fail(e -> {
				e.printStackTrace();
				
				return new ResultEntity<>(ResultEntity.failCodeString(), -99L);
			})
			.exec();		
	}
	
	/**
	 * 상품옵션 종류 수정 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 수정할 상품옵션 종류 Seq 값
	 * @param searchOptionName 수정할 상품옵션 종류 이름 값
	 * @return 변경된 값 반환
	 */
	@Transactional(transactionManager = "categoriesTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Map<String, Object>> searchOptionKindUpdate(Long id, String searchOptionName) {
		
		return new RestProcess<Map<String, Object>>()
			.call(() -> {

				ResultEntity<SearchOptionKind> result = new SearchOptionKind().update(searchOptionName
					, () -> this.searchOptionKindReadRepository.findId(id));
				
				Map<String, Object> searchOptionKind = Optional.ofNullable(result)
					.filter(v -> v.sucess())
					.map(v -> {

						SearchOptionKind value = v.getResult();
						this.searchOptionKindCommandRepository.add(value);

						return CommonGet.<SearchOptionKind, Map<String, Object>>convert(value
							, s -> { 
								HashMap<String, Object> rtn = new HashMap<>();
								rtn.put("id", s.getId());
								rtn.put("regDate", s.getRegDate());
								rtn.put("searchOptionName", s.getSearchOptionName());
								
								return rtn;
							});
					})
					.orElse(new HashMap<String, Object>());
				
				return new ResultEntity<>(result.getCode(), searchOptionKind);
			})
			.fail(e -> {
				e.printStackTrace();
				
				return new ResultEntity<>(ResultEntity.failCodeString());				
			})
			.exec();
				
	}
}
