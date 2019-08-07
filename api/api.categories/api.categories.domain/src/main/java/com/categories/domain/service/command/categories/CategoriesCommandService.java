package com.categories.domain.service.command.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.infra.categories.process.get.impl.CategoriesGetImpl;
import com.categories.domain.repo.command.categories.CategoriesCommandRepository;
import com.categories.domain.repo.read.categories.CategoriesReadRepository;
import com.categories.domain.repo.read.categories.SearchOptionKindReadRepository;
import com.google.gson.Gson;
import com.sinnake.entity.ResultEntity;

import util.RestProcess;

/**
 * 카테고리 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class CategoriesCommandService {

	private CategoriesCommandRepository categoriesCommandRepository;
	private CategoriesReadRepository categoriesReadRepository;
	private SearchOptionKindReadRepository searchOptionKindReadRepository;
	
	@Autowired
	public CategoriesCommandService(CategoriesCommandRepository categoriesCommandRepository
			, CategoriesReadRepository categoriesReadRepository
			, SearchOptionKindReadRepository searchOptionKindReadRepository) {

		this.categoriesCommandRepository = categoriesCommandRepository;
		this.categoriesReadRepository = categoriesReadRepository;
		this.searchOptionKindReadRepository = searchOptionKindReadRepository;
	}
	
	/**
	 * 카테고리  추가 서비스
	 * 
	 * @author sinnakeWEB
	 * @param categoryName 카테고리 명
	 * @param parentId 상위 카테고리 Seq 값
	 * @return 카테고리 결과 조회 값
	 */
	@SuppressWarnings("unchecked")
	@Transactional(transactionManager = "categoriesTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Long> categoriesAdd(String categoryName, Long parentId, String searchOptionList) {
		
		return new RestProcess<Long>()
			.call(() -> {
				List<Map<String, String>> searchOptionLists = Optional.ofNullable(searchOptionList)
					.filter(s -> s.length() > 0)
					.map(s -> new Gson().fromJson(s, ArrayList.class ))
					.orElseGet(ArrayList::new);
				
				ResultEntity<Categories> categories = new Categories(categoryName, parentId)
					.add(this.categoriesReadRepository::findId
						, searchOptionLists
						, this.searchOptionKindReadRepository::findId);

				if(categories.sucess()) {
					this.categoriesCommandRepository.add(categories.getResult());

					return new ResultEntity<>(ResultEntity.sucessCodeString()
						,categories.getResult().getId());
				}
								
				return new ResultEntity<>(ResultEntity.failCodeString()
					, Long.parseLong(categories.getCode()) );
			})
			.fail(e -> {
				e.printStackTrace();
				
				return new ResultEntity<>(ResultEntity.failCodeString(), -99L);
			})
			.exec();
	}

	/**
	 * 카테고리 수정 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 수정할 카테고리 Seq 값
	 * @param parentId 수정할 상위 카테고리 Seq 값
	 * @param categoryName 수정할 카테고리 이름 값
	 * @return 수정된 Categories 객체를 Map으로 변환해서 반환
	 */
	@SuppressWarnings("unchecked")
	@Transactional(transactionManager = "categoriesTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Map<String, Object>> categoriesUpdate(Long id, Long parentId, String categoryName, String searchOptionList) {

		return new RestProcess<Map<String, Object>>()
			.call(() -> {
				List<Map<String, String>> searchOptionLists = Optional.ofNullable(searchOptionList)
					.filter(s -> s.length() > 0)
					.map(s -> new Gson().fromJson(s, ArrayList.class ))
					.orElseGet(ArrayList::new);
				
				ResultEntity<Categories> resultEntity = new Categories(categoryName, parentId).update(
					i -> this.categoriesReadRepository.findId(i)
					, () -> this.categoriesReadRepository.findId(id)
					, searchOptionLists
					, this.searchOptionKindReadRepository::findId);
					
				if(!resultEntity.sucess()) {
					return new ResultEntity<>(resultEntity.getCode());
				} 

				Categories categories = resultEntity.getResult();
				
				this.categoriesCommandRepository.add(categories);

				return new ResultEntity<>(resultEntity.getCode()
					, CategoriesGetImpl.convertProcess(categories));
			})
			.fail(e -> {
				e.printStackTrace();
				
				return new ResultEntity<Map<String,Object>>(ResultEntity.failCodeString());
			})
			.exec();

	}
	
}
