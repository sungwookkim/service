package com.categories.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.categories.domain.infra.categories.process.add.CategoriesAdd;
import com.categories.domain.infra.categories.process.add.impl.CategoriesAddImpl;
import com.categories.domain.infra.categories.process.get.impl.CategoriesGetImpl;
import com.google.common.base.Function;
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
	
	/**********/
	/* 생성자 */
	/**********/	
	public Categories() {}
	
	public Categories(String categoryName) {
		this.categoryName = categoryName;
	}

	public Categories(String categoryName, Long parentId) {
		this.categoryName = categoryName;
		this.parentId = parentId;
	}

	/*************************/
	/* Getter, Setter 메소드 */
	/*************************/
	public Long getId() { return id; }

	public String getCategoryName() { return categoryName; }
	public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

	public Long getParentId() { return parentId; }
	public void setParentId(Long parentId) { this.parentId = parentId; }

	public Date getRegDate() { return regDate; }

	public void setRegDate(Date regDate) { this.regDate = regDate; }

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
	 * @param categoryFun 상위 카테고리의 존재 여부 확인을 위한 로직
	 * @return 생성 할 카테고리 객체 반환
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Categories> add(String categoryName, Long parentId, Function<Long, Categories> categoryFun) {
		CategoriesAdd categoriesAdd = new CategoriesAddImpl(parentId
			, categoryName
			, categoryFun);
		
		return this.add(categoriesAdd);
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
