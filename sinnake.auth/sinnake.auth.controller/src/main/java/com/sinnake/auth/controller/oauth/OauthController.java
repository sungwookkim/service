package com.sinnake.auth.controller.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth.domain.entity.SpringOauth;
import com.auth.domain.service.command.oauth.OauthService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

/**
 * oauth controller 클래스
 * 
 * @author sinnakeWEB
 */
@RestController
@RequestMapping(value = "/oauth")
public class OauthController {

	private OauthService oauthService;
	
	@Autowired
	public OauthController(OauthService oauthService) {
		this.oauthService = oauthService;
	}
	
	/**
	 * oauth 저장 URL
	 * 
	 * @author sinnakeWEB 
	 * @param param 저장 요청 값
	 * @return 응답 값
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveOauth")
	public ResponseEntity<ResultEntity<SpringOauth>> oauthSave(@RequestBody Map<String, String> param) {
		
		final String client_id = param.get("client_id");
		
		return new PresentationProcess<SpringOauth>(HttpStatus.CREATED)
			.process(() -> this.oauthService.oauthSave(client_id))
			.exec();
	}
	
	/**
	 * oauth 삭제 URL
	 * 
	 * @author sinnakeWEB
	 * @param param 삭제 요청 값
	 * @return 응답 값
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delOauth")
	public ResponseEntity<ResultEntity<SpringOauth>> delOauth(@RequestBody Map<String, String> param) {
	
		final String client_id = param.get("client_id");

		return new PresentationProcess<SpringOauth>()
			.process(() -> this.oauthService.oauthDel(client_id))
			.exec();			
	}
}
