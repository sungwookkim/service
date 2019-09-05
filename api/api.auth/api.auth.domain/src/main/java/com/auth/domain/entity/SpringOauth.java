package com.auth.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.auth.domain.infra.oauth.process.oauthDel.OauthDel;
import com.auth.domain.infra.oauth.process.oauthSave.OauthSave;
import com.auth.domain.infra.oauth.process.oauthSave.impl.OauthSaveImpl;
import com.sinnake.entity.ResultEntity;

/**
 * Spring에서 사용되는 oauth 도메인
 * (Spring에서 제공하는 테이블)
 * 
 * @author sinnakeWEB
 */
/*-- 테이블 sinnake.oauth_client_details
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;*/

@Entity(name = "oauth_client_details")
public class SpringOauth {
	@Id
	@Column(length = 256)
	String client_id;
	
	@Column(length = 256)
	String resource_ids;
	
	@Column(length = 256)
	String client_secret;
	
	@Column(length = 256)
	String scope;
	
	@Column(length = 256)
	String authorized_grant_types;
	
	@Column(length = 256)
	String web_server_redirect_uri;
	
	@Column(length = 256)
	String  authorities;
	
	@Column(length = 11)
	Integer access_token_validity;
	
	@Column(length = 11)	
	Integer refresh_token_validity;
	
	@Column(length = 4096)
	String additional_information;
	
	@Column(length = 256)
	String autoapprove;
		
	/**********/
	/* 생성자 */
	/**********/
	public SpringOauth() {}

	public SpringOauth(String client_id, String authorities) {
		this.client_id = client_id;
		this.client_secret = client_id;
		this.scope = "read,write";
		this.authorized_grant_types = "authorization_code,password,implicit,refresh_token";
		this.authorities = authorities;
		this.access_token_validity = 600;
		this.refresh_token_validity = 1800;
	}
	
	public SpringOauth(String client_id
		, String resource_ids
		, String client_secret
		, String scope
		, String authorized_grant_types
		, String web_server_redirect_uri
		, String authorities
		, Integer access_token_validity
		, Integer refresh_token_validity
		, String additional_information
		, String autoapprove) {
		
		this.client_id = client_id;
		this.resource_ids = resource_ids;
		this.client_secret = client_id;
		this.scope = scope;
		this.authorized_grant_types = authorized_grant_types;
		this.web_server_redirect_uri = web_server_redirect_uri;
		this.authorities = authorities;
		this.access_token_validity = access_token_validity;
		this.refresh_token_validity = refresh_token_validity;
		this.additional_information = additional_information;
		this.autoapprove = authorities;
	}
	
	/*************************/
	/* Getter, Setter 메소드 */
	/*************************/
	public String getClient_id() { return client_id; }
	public String getResource_ids() { return resource_ids; }
	public String getClient_secret() { return client_secret; }
	public String getScope() { return scope; }
	public String getAuthorized_grant_types() { return authorized_grant_types; }
	public String getWeb_server_redirect_uri() { return web_server_redirect_uri; }
	public String getAuthorities() { return authorities; }
	public Integer getAccess_token_validity() { return access_token_validity; }
	public Integer getRefresh_token_validity() { return refresh_token_validity; }
	public String getAdditional_information() { return additional_information; }
	public String getAutoapprove() { return autoapprove; }

	/***********/
	/* 프로세스*/
	/***********/
	/**
	 * oauth 정보 저장 메서드
	 * 
	 * @author sinnakeWEB
	 * @return 결과 응답 값
	 */
	// TODO : oAuth 정보 저장
	public ResultEntity<SpringOauth> oauthSave() {
		return this.oauthSave(new OauthSaveImpl(this));
	}

	/**
	 * oauth 정보 저장 메서드
	 * 
	 * @author sinnakeWEB
	 * @param oauthSave oauth 저장 비지니스 구현체 
	 * @return 결과 응답 값
	 */
	public ResultEntity<SpringOauth> oauthSave(OauthSave oauthSave) {
		return oauthSave.process();
	}
	
	/**
	 * oauth 정보 삭제 메서드
	 * (회원가입 시도 등 실패 했을 경우 삭제 함.)
	 * 
	 * @author sinnakeWEB
	 * @param oauthDel oauth 삭제 비지니스 구현체
	 * @return 결과 응답 값
	 */
	// TODO : oAuth 정보 삭제	
	public static ResultEntity<SpringOauth> oauthDel(OauthDel oauthDel) {
		return oauthDel.process();
	}
}
