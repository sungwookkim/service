package com.categories.domain.entity.searchOption;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.categories.domain.entity.categories.Categories;

/**
 * 검색 옵션 리스트
 * 
 * @author sinnakeWEB
 *
 */
@Entity(name = "search_option_list")
public class SearchOptionList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_id")
	private Long id;
	
	@Column(name = "searchOption_name", length = 50, nullable = false)	
	private String searchOptionName;
	
	@Column(name = "type", length = 100, nullable = false)
	private String type = "checkbox";
	
	@Column(name = "reg_date", nullable = false)
	private Date regDate = new Date();
	
	@ManyToOne
	@JoinColumn(name = "kind_id")
	private SearchOptionKind searchOptionKind;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Categories categories;
	
	public SearchOptionList() {}

	public SearchOptionList(String searchOptionName, String type) {
		this.searchOptionName = searchOptionName;		
		this.type = type;
	}
	
	public SearchOptionList(String searchOptionName, String type, SearchOptionKind searchOptionKind) {
		this.searchOptionName = searchOptionName;
		this.searchOptionKind = searchOptionKind;
		this.type = type;
	}
	
	public SearchOptionList(String searchOptionName, String type, SearchOptionKind searchOptionKind, Categories categories) {
		this.searchOptionName = searchOptionName;
		this.searchOptionKind = searchOptionKind;
		this.categories = categories;
		this.type = type;
	}

	/***********************/
	/* Getter, Setter 메소드 */
	/***********************/
	public Long getId() { return id; }

	public String getSearchOptionName() { return searchOptionName; }
	public void setSearchOptionName(String searchOptionName) { this.searchOptionName = searchOptionName; }

	public Date getRegDate() { return regDate; }
	public void setRegDate(Date regDate) { this.regDate = regDate; }

	public SearchOptionKind getSearchOptionKind() { return searchOptionKind; }
	public void setSearchOptionKind(SearchOptionKind searchOptionKind) { this.searchOptionKind = searchOptionKind; }

	public Categories getCategories() { return categories; }
	public void setCategories(Categories categories) {
		Optional.ofNullable(this.categories)
			.ifPresent(c -> c.getSearchOptionList().remove(this));

		this.categories = categories;
		
		if(!categories.getSearchOptionList().contains(this)) {
			categories.getSearchOptionList().add(this);
		}
	}
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
}
