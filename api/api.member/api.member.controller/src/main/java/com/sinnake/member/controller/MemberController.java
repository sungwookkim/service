package com.sinnake.member.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.member.domain.entity.member.Member;
import com.member.domain.service.command.member.MemberCommandService;
import com.member.domain.service.read.member.MemberReadService;
import com.sinnake.entity.ResultEntity;

import util.PresentationProcess;

@RestController
@RequestMapping(value="/api/v1/member")
public class MemberController {

	private MemberCommandService memberCommandService;
	private MemberReadService memberReadService;

	@Autowired
	public MemberController(MemberCommandService memberCommandService
		, MemberReadService memberReadService) {
		
		this.memberCommandService = memberCommandService;
		this.memberReadService = memberReadService;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/sign")
	public ResponseEntity<ResultEntity<Member>> signUpdate(HttpServletRequest req, Principal principal) throws Exception {

		final String userName = principal.getName();
		final String password = req.getParameter("password");
		final String rePassword = req.getParameter("rePassword");
		final String address = req.getParameter("address");
		final String detailAddress = req.getParameter("detailAddress");
		final String postCode = req.getParameter("postCode");
		
		return new PresentationProcess<Member>()
			.process(() -> {
				ResultEntity<Member> resultEntity = null; 
				
				try {
					resultEntity = this.memberCommandService.signUpdate(userName
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/sign")
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
					resultEntity = this.memberCommandService.signUp(userName
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
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public ResponseEntity<ResultEntity<Map<String, Object>>> login(HttpServletRequest req) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {

				Map<String, Object> member = this.memberReadService
					.findMember(req.getParameter("username").toString());

				return new ResultEntity<>(ResultEntity.sucessCodeString(), member);
			})
			.exec();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logout")
	public ResponseEntity<ResultEntity<Map<String, Object>>> logout(Principal principal) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {
				HashMap<String, Object> value = new HashMap<>();				
				value.put("userName", principal.getName());
				value.put("logoutDate", new Date());

				return new ResultEntity<>(ResultEntity.sucessCodeString(), value);
			})
			.exec();
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/info")
	public ResponseEntity<ResultEntity<Map<String, Object>>> info(Principal principal) {
		
		return new PresentationProcess<Map<String, Object>>()
			.process(() -> {

				Map<String, Object> member = this.memberReadService
					.findMember(principal.getName());

				return new ResultEntity<>(ResultEntity.sucessCodeString(), member);
			})
			.exec();
	}
}
