package com.sinnake.searchOptionKind.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.categories.domain.service.command.searchOptionKind.SearchOptionKindCommandService;
import com.categories.domain.service.read.searchOptionKind.SearchOptionKindReadService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value = "/api/searchOption")
public class SearchOptionKindController {

	private SearchOptionKindCommandService searchOptionKindCommandService;
	private SearchOptionKindReadService searchOptionKindReadService;
	
	@Autowired
	public SearchOptionKindController(SearchOptionKindCommandService searchOptionKindCommandService
		, SearchOptionKindReadService searchOptionKindReadService) {
		
		this.searchOptionKindCommandService = searchOptionKindCommandService;
		this.searchOptionKindReadService = searchOptionKindReadService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/kind/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> get(@PathVariable Long id) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(()-> {
				
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.searchOptionKindReadService.findSearchOptionKind(id));
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/kind/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> update(@PathVariable Long id
		, @RequestParam(value = "searchOptionName", required = true) String searchOptionName
		, HttpServletRequest req) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				
				return this.searchOptionKindCommandService.searchOptionKindUpdate(id, searchOptionName);
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/kind")
	public ResponseEntity<ResultEntity<Long>> add(
		 @RequestParam(value = "searchOptionName", required = true) String searchOptionName			
		 , HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {
				
				return this.searchOptionKindCommandService.searchOptionKindAdd(searchOptionName);
			})
			.exec();
	}
	
}
