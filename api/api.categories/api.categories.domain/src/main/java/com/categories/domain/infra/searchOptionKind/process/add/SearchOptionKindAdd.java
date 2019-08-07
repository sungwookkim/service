package com.categories.domain.infra.searchOptionKind.process.add;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.sinnake.entity.ResultEntity;

import commonInterface.CommonProcess;

public interface SearchOptionKindAdd extends CommonProcess<SearchOptionKind>{

	public ResultEntity<SearchOptionKind> add();
}
