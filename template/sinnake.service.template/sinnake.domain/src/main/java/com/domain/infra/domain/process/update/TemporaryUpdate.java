package com.domain.infra.domain.process.update;

import java.util.function.Supplier;

import com.domain.entity.Temporary;
import com.domain.infra.domain.process.add.TemporaryAdd;

public interface TemporaryUpdate extends TemporaryAdd {

	public void findTemporary(Supplier<Temporary> findTemporary);

}
