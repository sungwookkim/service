package com.categories.domain.entity.categories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.entity.searchOption.SearchOptionList;
import com.categories.domain.infra.categories.process.add.CategoriesAdd;
import com.categories.domain.infra.categories.process.add.impl.CategoriesAddImpl;
import com.categories.domain.infra.categories.process.get.impl.CategoriesGetImpl;
import com.categories.domain.infra.categories.process.update.CategoriesUpdate;
import com.categories.domain.infra.categories.process.update.impl.CategoriesUpdateImpl;
import com.sinnake.entity.ResultEntity;

/**
 * 카테고리 도메인
 * 
 * @author sinnakeWEB
 *
 */
@Entity(name = "categories")
public class Categories {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;
	
	@Column(name = "category_name", length = 100, nullable = false)
	private String categoryName;
	
	@Column(name = "parent_id", nullable = false)
	private Long parentId = 0L;
	
	@Column(name = "reg_date", nullable = false)
	private Date regDate = new Date();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "categories")
	private List<SearchOptionList> searchOptionLists = new ArrayList<>();
	
	/********/
	/* 생성자 */
	/********/
	public Categories() {}
	
	public Categories(String categoryName) {
		this.categoryName = categoryName;
	}

	public Categories(String categoryName, Long parentId) {
		this.categoryName = categoryName;
		this.parentId = parentId;
	}

	/***********************/
	/* Getter, Setter 메소드 */
	/***********************/
	public Long getId() { return id; }

	public String getCategoryName() { return categoryName; }
	public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

	public Long getParentId() { return parentId; }
	public void setParentId(Long parentId) { this.parentId = parentId; }

	public Date getRegDate() { return regDate; }
	public void setRegDate(Date regDate) { this.regDate = regDate; }

	/*************/
	/* 연관 편의 메소드 */
	/*************/
	public List<SearchOptionList> getSearchOptionList() { return searchOptionLists; }
	public void addSearchOptionList(SearchOptionList searchOptionList) {
		Optional.ofNullable(this.searchOptionLists)
			.filter(so -> !so.contains(searchOptionList))
			.ifPresent(so -> so.add(searchOptionList));
		
		Optional.ofNullable(searchOptionList)
				.map(so -> Optional.ofNullable(so.getCategories())
					.map(c -> c)
					.orElseGet(Categories::new))
				.filter(so -> so != this)
				.ifPresent(so -> searchOptionList.setCategories(this));
	}
	
	/**
	 * 카테고리 조회 프로세스
	 * 
	 * @author sinnakeWEB 
	 * @return 조회 객체 반환
	 */
	public CategoriesGetImpl get() {
		return new CategoriesGetImpl();
	}

	/**
	 * 카테고리 생성 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param categoryName 생성할 카테고리 이르
	 * @param parentId 상위 카테고리 Seq 번호
	 * @param findParentCategories 상위 카테고리의 존재 여부 확인을 위한 함수형 인터페이스
	 * @return 생성 한 카테고리 객체 반환
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Categories> add(Function<Long, Categories> findParentCategories
		, List<Map<String, String>> searchOptionList
		, Function<Long, SearchOptionKind> findSearchOptionKind) {		

		CategoriesAdd categoriesAdd = new CategoriesAddImpl();
		categoriesAdd.pareintId(this.parentId, findParentCategories);
		categoriesAdd.categoryName(this.categoryName);
		categoriesAdd.searchOptionList(searchOptionList, findSearchOptionKind);

		return this.add(categoriesAdd);
	}

	/**
	 * 카테고리 수정 프로세스
	 *
	 * @author sinnakeWEB
	 * @param findParentCategories 상위 카테고리의 존재 여부 확인을 위한 함수형 인터페이스 
	 * @param findCategory 수정할 카테고리를 조회할 함수형 인터페이스
	 * @return 수정한 Categories 객체 반환
	 */
	public ResultEntity<Categories> update(Function<Long, Categories> findParentCategories
		, Supplier<Categories> findCategory
		, List<Map<String, String>> searchOptionList
		, Function<Long, SearchOptionKind> findSearchOptionKind) {
		
		CategoriesUpdate categoriesUpdate = new CategoriesUpdateImpl();
		categoriesUpdate.findCategory(findCategory);
		categoriesUpdate.categoryName(this.categoryName);
		categoriesUpdate.pareintId(this.parentId, findParentCategories);
		categoriesUpdate.searchOptionList(searchOptionList, findSearchOptionKind);
		
		return this.add(categoriesUpdate);
	}
	
	/**
	 * 카테고리 추가 및 커스텀 프로세스
	 * 
	 * @param categoriesAdd
	 * @return 결과 값
	 * @throws Exception 에러 발생
	 */
	public ResultEntity<Categories> add(CategoriesAdd categoriesAdd) {
		return categoriesAdd.process();
	}
}
