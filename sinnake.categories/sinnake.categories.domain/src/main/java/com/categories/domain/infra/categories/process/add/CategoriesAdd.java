package com.categories.domain.infra.categories.process.add;


import java.util.function.Function;

import com.categories.domain.entity.categories.Categories;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonProcess;

public interface CategoriesAdd extends CommonProcess<Categories> {

	public ResultEntity<Categories> add();
	
	public void pareintId(Long parentId, Function<Long, Categories> findParentCategories);
	
	public void categoryName(String categoryName);
}
