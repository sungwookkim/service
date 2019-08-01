package com.categories.domain.infra.categories.process.update;

import java.util.function.Supplier;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.infra.categories.process.add.CategoriesAdd;

public interface CategoriesUpdate extends CategoriesAdd {

	public void findCategory(Supplier<Categories> findCategory);

}
