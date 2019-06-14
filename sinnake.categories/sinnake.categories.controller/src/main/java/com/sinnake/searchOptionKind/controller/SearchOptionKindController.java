package com.sinnake.searchOptionKind.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.categories.domain.service.command.searchOptionKind.SearchOptionKindService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value = "/api/searchOption")
public class SearchOptionKindController {

	private SearchOptionKindService searchOptionKindService;
	
	@Autowired
	public SearchOptionKindController(SearchOptionKindService searchOptionKindService) {
		this.searchOptionKindService = searchOptionKindService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/kind/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> get(@PathVariable Long id) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(()-> {
				
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.searchOptionKindService.findSearchOptionKind(id));
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/kind/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> update(@PathVariable Long id, HttpServletRequest req) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {

				String searchOptionName = req.getParameter("searchOptionName");
				
				return this.searchOptionKindService.searchOptionKindUpdate(id, searchOptionName);
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/kind")
	public ResponseEntity<ResultEntity<Long>> add(HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {

				String searchOptionName = req.getParameter("searchOptionName");
				
				return this.searchOptionKindService.searchOptionKindAdd(searchOptionName);
			})
			.exec();
	}
	
}
