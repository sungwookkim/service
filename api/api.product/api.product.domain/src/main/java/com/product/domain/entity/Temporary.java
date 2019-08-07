package com.product.domain.entity;

import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.product.domain.infra.product.process.add.TemporaryAdd;
import com.product.domain.infra.product.process.add.impl.TemporaryAddImpl;
import com.product.domain.infra.product.process.get.impl.TemporaryGetImpl;
import com.product.domain.infra.product.process.update.TemporaryUpdate;
import com.product.domain.infra.product.process.update.impl.TemporaryUpdateImpl;
import com.sinnake.entity.ResultEntity;

@Entity(name = "temporary")
public class Temporary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;
	
	@Column(name = "text")
	String text;
	
	public Temporary() {}
	
	public Temporary(String text) {
		this.text = text;
	}
	
	/***********************/
	/* Getter, Setter 메소드 */
	/***********************/
	public String getText() { return this.text; }
	public Long getId() { return this.id; };

	public void setText(String text) { this.text = text; } 
	/**
	 * 템플릿 조회 프로세스
	 * 
	 * @author sinnakeWEB 
	 * @return 조회 객체 반환
	 */
	public TemporaryGetImpl get() {
		return new TemporaryGetImpl();
	}
	
	/**
	 * 템플릿 생성 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param text 임시 값
	 * @return 생성 한 템플릿 객체 반환
	 * @throws Exception 예외 발생
	 */
	public ResultEntity<Temporary> add(String text) {
		TemporaryAdd temporaryAdd = new TemporaryAddImpl(text);
		
		return this.add(temporaryAdd);
	}

	/**
	 * 템플릿 수정 프로세스
	 *
	 * @author sinnakeWEB
	 * @param findParentCategories 상위 카테고리의 존재 여부 확인을 위한 함수형 인터페이스 
	 * @param findCategory 수정할 카테고리를 조회할 함수형 인터페이스
	 * @return 수정한 Categories 객체 반환
	 */
	public ResultEntity<Temporary> update(Supplier<Temporary> findTemporary, String text) {

		TemporaryUpdate temporaryUpdate = new TemporaryUpdateImpl(text);
		temporaryUpdate.findTemporary(findTemporary);
		
		return this.add(temporaryUpdate);
	}
	
	/**
	 * 템플릿 추가 및 커스텀 프로세스
	 * 
	 * @param categoriesAdd
	 * @return 결과 값
	 * @throws Exception 에러 발생
	 */
	public ResultEntity<Temporary> add(TemporaryAdd temporaryAdd) {
		return temporaryAdd.process();
	}
	
}
