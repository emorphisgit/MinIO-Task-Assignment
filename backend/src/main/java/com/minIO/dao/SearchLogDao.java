package com.minIO.dao;

import org.springframework.data.repository.CrudRepository;

import com.minIO.entity.SearchLog;

public interface SearchLogDao extends CrudRepository<SearchLog, Long> {
	
	
}