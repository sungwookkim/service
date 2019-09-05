package com.sinnake.temporary;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.domain.service.command.domain.TemporaryCommandService;
import com.domain.service.read.domain.TemporaryReadService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value = "/api/v1/temporary")
public class TemporaryController {
	
	private TemporaryCommandService temporaryCommandService;
	private TemporaryReadService temporaryReadService;
	
	@Autowired
	public TemporaryController(TemporaryCommandService temporaryCommandService
		, TemporaryReadService temporaryReadService) {

		this.temporaryCommandService = temporaryCommandService;
		this.temporaryReadService = temporaryReadService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<ResultEntity<Map<String, Object>>> getId(@PathVariable Long id) {

		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, this.temporaryReadService.findTemporary(id));
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	public ResponseEntity<ResultEntity<Long>> add(HttpServletRequest req) {
		
		return new PresentationProcess<Long>()
			.process(() -> {
		
				return this.temporaryCommandService.temporaryAdd(req.getParameter("text").toString());
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<ResultEntity<Long>> update(@PathVariable Long id
		, HttpServletRequest req) {

		return new PresentationProcess<Long>()
			.process(() -> {
				
				return this.temporaryCommandService.temporaryUpdate(id, req.getParameter("text").toString());
			})
			.exec();
	}
}
