package com.sinnake.member.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.member.domain.entity.member.Member;
import com.member.domain.service.command.member.MemberService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value="/member")
public class MemberController {

	private MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@RequestMapping(method = RequestMethod.PUT
		, consumes = {
			MediaType.APPLICATION_JSON_VALUE
		}
		, value = "/signUpdate")
	public ResponseEntity<ResultEntity<Member>> signUpdate(@RequestBody Map<String, String> reqBody, Principal principal) throws Exception {

		final String userName = principal.getName();
		final String password = reqBody.get("password");
		final String rePassword = reqBody.get("rePassword");
		final String address = reqBody.get("address");
		final String detailAddress = reqBody.get("detailAddress");
		final String postCode = reqBody.get("postCode");
		
		return new PresentationProcess<Member>()
			.process(() -> {
				ResultEntity<Member> resultEntity = null; 
				
				try {
					resultEntity = this.memberService.signUpdate(userName
						, password
						, rePassword
						, address
						, detailAddress
						, postCode);
				} catch(Exception e) {
					throw new RuntimeException(e.getMessage());
				}
				
				return resultEntity;
			})
			.exec();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/signUp")
	public ResponseEntity<ResultEntity<Member>> signUp(HttpServletRequest req) throws Exception {
		final String userName = req.getParameter("userName");
		final String password = req.getParameter("password");
		final String rePassword = req.getParameter("rePassword");
		final String address = req.getParameter("address");
		final String detailAddress = req.getParameter("detailAddress");
		final String postCode = req.getParameter("postCode");
		
		 return new PresentationProcess<Member>(HttpStatus.CREATED)
			.process(() -> {
				ResultEntity<Member> resultEntity = null; 
				
				try {
					resultEntity = this.memberService.signUp(userName
						, password
						, rePassword
						, address
						, detailAddress
						, postCode);
				} catch(Exception e) {
					throw new RuntimeException(e.getMessage());
				}
				
				return resultEntity;
			}).exec();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/authTest")
	public ResponseEntity<String> authTest(HttpServletRequest req) {
		
		return ResponseEntity.status(HttpStatus.OK)
			.body("{'ok' : 'authTest'}");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/test")
	public ResponseEntity<String> test(HttpServletRequest req) {
		
		return ResponseEntity.status(HttpStatus.OK)
			.body("{'ok' : 'test'}");
	}	
}
