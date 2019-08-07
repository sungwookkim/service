package com.auth.domain.repo.command.oauth;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.auth.domain.entity.SpringOauth;

@Repository
public class OauthCommandRepository {
	
	@PersistenceContext
	EntityManager em;
	
	public void save(SpringOauth springOauth) {
		em.persist(springOauth);
	}
	
	public void remove(SpringOauth springOauth) {
		em.remove(springOauth);
	}
}
