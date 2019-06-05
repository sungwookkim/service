package com.sinnake.categories.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(method = RequestMethod.POST, value = "/add/{parentId}")	
	public ResponseEntity<ResultEntity<Long>> add(@PathVariable Long parentId, HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {
				String categoryName = req.getParameter("categoryName").toString();

				return this.categoriesService.categoriesAdd(categoryName, parentId);
			})
			.exception(() -> {
				return new ResultEntity<>("-99");
			})
			.exec();
	}	

	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public ResponseEntity<ResultEntity<Map<Long, List<Map<String, Object>>>>> get() {
		
		return new PresentationProcess<Map<Long, List<Map<String, Object>>>>()
			.process(() -> {
				
				return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode()
					, this.categoriesService.findAllCategories());
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/get/{categoryId}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> get(@PathVariable Long categoryId) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				
				return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode()
					, this.categoriesService.findCategories(categoryId));
			})
			.exec();						
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/get/child/{parentId}")
	public ResponseEntity<ResultEntity<List<Map<String, Object>>>> childGet(@PathVariable Long parentId) {
		
		return new PresentationProcess<List<Map<String, Object>>>()
			.process(() -> {

				return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode()
					, this.categoriesService.findChildCategories(parentId));
			})
			.exec();
	}
}
