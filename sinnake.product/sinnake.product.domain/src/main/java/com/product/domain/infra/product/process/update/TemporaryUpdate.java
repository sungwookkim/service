package com.product.domain.infra.product.process.update;

import java.util.function.Supplier;

import com.product.domain.entity.Temporary;
import com.product.domain.infra.product.process.add.TemporaryAdd;

public interface TemporaryUpdate extends TemporaryAdd {

	public void findTemporary(Supplier<Temporary> findTemporary);

}
