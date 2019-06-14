package com.sinnake.categories.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	public ResponseEntity<ResultEntity<Long>> add(@PathVariable Long parentId, HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {
				String categoryName = req.getParameter("categoryName").toString();

				return this.categoriesService.categoriesAdd(categoryName, parentId);
			})
			.exec();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")	
	public ResponseEntity<ResultEntity<Map<String, Object>>> update(@PathVariable Long id, HttpServletRequest req) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				Long parentId = Optional.ofNullable(req.getParameter("parentId"))
					.map(p -> Long.parseLong(p))
					.orElse(-1L);

				return this.categoriesService.categoriesUpdate(id, parentId, req.getParameter("categoryName"));				
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
