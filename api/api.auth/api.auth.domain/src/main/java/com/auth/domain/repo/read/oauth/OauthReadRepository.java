package com.auth.domain.repo.read.oauth;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.auth.domain.entity.SpringOauth;

@Repository
public class OauthReadRepository {

	@PersistenceContext
	EntityManager em;
	
	public OauthReadRepository() { }
	
	public SpringOauth findId(String client_id) {
		return em.find(SpringOauth.class, client_id);
	}
}
