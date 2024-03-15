package com.minIO.dao;

import org.springframework.data.repository.CrudRepository;

import com.minIO.entity.DownloadLog;

public interface DownloadLogDao extends CrudRepository<DownloadLog, Long> {

}