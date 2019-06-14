package com.categories.domain.entity.searchOption;

import java.util.Date;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.categories.domain.infra.searchOptionKind.process.add.SearchOptionKindAdd;
import com.categories.domain.infra.searchOptionKind.process.add.impl.SearchOptionKindAddImpl;
import com.categories.domain.infra.searchOptionKind.process.get.impl.SearchOptionKindGetImpl;
import com.categories.domain.infra.searchOptionKind.process.update.impl.SearchOptionKindUpdateImpl;
import com.sinnake.entity.ResultEntity;

/**
 * 검색 옵션 종류 리스트
 * 
 * @author sinnakeWEB
 *
 */
@Entity(name = "searchOptionKind")
public class SearchOptionKind {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kind_id")
	private Long id;

	@Column(name = "searchOption_name", length = 100, nullable = false)
	private String searchOptionName;
	
	@Column(name = "reg_date", nullable = false)
	private Date regDate = new Date();
	
	/********/
	/* 생성자 */
	/********/
	public SearchOptionKind() {}
	
	public SearchOptionKind(String searchOptionName) {
		this.searchOptionName = searchOptionName;
	}

	/***********************/
	/* Getter, Setter 메소드 */
	/***********************/
	public Long getId() { return id; }

	public String getSearchOptionName() { return searchOptionName; }
	public void setSearchOptionName(String searchOptionName) { this.searchOptionName = searchOptionName; }

	public Date getRegDate() { return regDate; }
	public void setRegDate(Date regDate) { this.regDate = regDate; }
	
	/**
	 * 검색옵션 종류 조회 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 조회 객체 반환
	 */
	public SearchOptionKindGetImpl get() { 
		return new SearchOptionKindGetImpl();				
	}
	
	/**
	 * 검색 옵션 종류 생성 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param searchOptionName 검색 옵션  종류 이름
	 * @return 생성 한 검색 옵션 객체 반환
	 */
	public ResultEntity<SearchOptionKind> add(String searchOptionName) {
		SearchOptionKindAddImpl searchOptionKindAddImpl = new SearchOptionKindAddImpl(searchOptionName);
		
		return this.add(searchOptionKindAddImpl);
	}
	
	/**
	 * 검색옵션 종류 수정 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param searchOptionName 수정할 검색 옵션 종류 이름
	 * @param getSearchOptionKind 수정할 SearchOptionKind 객체 반환 함수형 인터페이스
	 * @return 수정된 SearchOptionKind 객체 반환
	 */
	public ResultEntity<SearchOptionKind> update(String searchOptionName, Supplier<SearchOptionKind> getSearchOptionKind) {
			
		return this.add(new SearchOptionKindUpdateImpl(searchOptionName, getSearchOptionKind));			
	}
	
	
	/**
	 * 검색 옵션 종류 생성 및 커스텀 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param searchOptionKindAdd 
	 * @return 생성 한 검색 옵션 객체 반환 
	 */
	public ResultEntity<SearchOptionKind> add(SearchOptionKindAdd searchOptionKindAdd) {
		return searchOptionKindAdd.process();
	}

}
