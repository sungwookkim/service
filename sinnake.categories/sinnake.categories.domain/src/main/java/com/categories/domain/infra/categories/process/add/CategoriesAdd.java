package com.categories.domain.infra.categories.process.add;

import com.categories.domain.entity.Categories;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonProcess;

public interface CategoriesAdd extends CommonProcess<Categories> {

	public ResultEntity<Categories> add();
}
