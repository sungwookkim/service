package com.domain.infra.domain.process.update.impl;

import java.util.Optional;
import java.util.function.Supplier;

import com.domain.entity.Temporary;
import com.domain.infra.domain.process.add.impl.TemporaryAddImpl;
import com.domain.infra.domain.process.update.TemporaryUpdate;
import com.sinnake.entity.ResultEntity;

public class TemporaryUpdateImpl extends TemporaryAddImpl implements TemporaryUpdate {

	Supplier<Temporary> findTemporary;
	
	public TemporaryUpdateImpl(String text) {
		super(text);
	}

	@Override
	public void findTemporary(Supplier<Temporary> findTemporary) {
		this.findTemporary = findTemporary;
	}

	@Override
	protected ResultEntity<Temporary> sucess(ResultEntity<Temporary> result) {
		ResultEntity<Temporary> temp = Optional.of(this.findTemporary)
			.map(f -> {
				return Optional.of(f.get())
					.map(v -> new ResultEntity<>(ResultEntity.sucessCodeString(), v))
					.orElse(new ResultEntity<>("-2"));
			})
			.orElseThrow(RuntimeException::new);
		
		if(!temp.sucess()) { return temp; }
		
		if(!this.updateId(temp.getResult()).sucess()) { return temp; }
		
		return temp;
	}
	
	protected ResultEntity<Temporary> updateId(Temporary temporary) {
		temporary.setText(text);
		
		return new ResultEntity<>(ResultEntity.sucessCodeString(), temporary);
	}


}
