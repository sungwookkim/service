package com.sinnake.categories.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.categories.domain.service.command.categories.CategoriesService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoriesController {

	private CategoriesService categoriesService;
	
	@Autowired
	public CategoriesController(CategoriesService categoriesService) {
		this.categoriesService = categoriesService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<ResultEntity<Map<Long, List<Map<String, Object>>>>> get() {
		
		return new PresentationProcess<Map<Long, List<Map<String, Object>>>>()
			.process(() -> {
				
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.categoriesService.findAllCategories());
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{parentId}")
	public ResponseEntity<ResultEntity<Long>> add(@PathVariable Long parentId
		, @RequestParam(value = "categoryName", required = true) String categoryName
		, @RequestParam(value = "searchOptionList", required = false, defaultValue = "") String searchOptionList
		, HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {				

				return this.categoriesService.categoriesAdd(categoryName, parentId, searchOptionList);
			})
			.exec();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> update(@PathVariable Long id
		, @RequestParam(value = "categoryName", required = true) String categoryName
		, @RequestParam(value = "searchOptionList", required = false, defaultValue = "") String searchOptionList
		, @RequestParam(value = "parentId", required = false, defaultValue = "-1L") Long parentId		
		, HttpServletRequest req) {

		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				
				return this.categoriesService.categoriesUpdate(id, parentId, categoryName, searchOptionList);				
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{categoryId}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> get(@PathVariable Long categoryId) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.categoriesService.findCategories(categoryId));
			})
			.exec();						
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/child/{parentId}")
	public ResponseEntity<ResultEntity<List<Map<String, Object>>>> childGet(@PathVariable Long parentId) {
		
		return new PresentationProcess<List<Map<String, Object>>>()
			.process(() -> {

				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.categoriesService.findChildCategories(parentId));
			})
			.exec();
	}
}
