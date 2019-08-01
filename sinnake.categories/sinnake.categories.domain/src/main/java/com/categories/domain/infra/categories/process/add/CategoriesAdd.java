package com.categories.domain.infra.categories.process.add;


import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonProcess;

public interface CategoriesAdd extends CommonProcess<Categories> {
	
	public void pareintId(Long parentId, Function<Long, Categories> findParentCategories);
	
	public void categoryName(String categoryName);
	
	public void searchOptionList(List<Map<String, String>> searchOptionList, Function<Long, SearchOptionKind> findSearchOptionKind);
	
	public ResultEntity<Categories> addSearchOptioinList(Categories categories);
}
