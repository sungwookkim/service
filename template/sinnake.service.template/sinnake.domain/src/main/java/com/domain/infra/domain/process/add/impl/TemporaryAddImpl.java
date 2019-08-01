package com.domain.infra.domain.process.add.impl;

import com.domain.entity.Temporary;
import com.domain.infra.domain.process.add.TemporaryAdd;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

/**
 * 카테고리 저장 프로세스 클래스
 * 
 * @author sinnakeWEB
 */
public class TemporaryAddImpl implements TemporaryAdd {

	protected String text;
	
	public TemporaryAddImpl(String text) {
		this.text = text;				
	}
	
	@Override
	public ResultEntity<Temporary> process() {
		
		return this.add();
	}

	/**
	 * 템플릿 생성 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 생성할  템플릿 객체 반환
	 */	
	protected ResultEntity<Temporary> add() {

		ResultEntity<Temporary> result = this.validate();
		
		if(!result.sucess()) {
			return new ResultEntity<>(result.getCode());
		}

		return this.sucess(result);
	}
	
	/**
	 * 템플릿 검증 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Temporary> validate() {

		if(new SinnakeValidate(this.text).required().getValidResult()) {
			return new ResultEntity<>(ResultEntity.sucessCodeString());
		}

		return new ResultEntity<>(ResultEntity.failCodeString());
		
	}
	
	protected ResultEntity<Temporary> sucess(ResultEntity<Temporary> result) {
		return new ResultEntity<>(result.getCode(), new Temporary(this.text));
	}
}
