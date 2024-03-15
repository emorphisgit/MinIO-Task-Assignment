package com.minIO.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "searchlogs")
public class SearchLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "timestamp")
	private Timestamp timestamp;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "search_result", length = 1055)
	private String searchResult;

	@Column(name = "search_query")
	private String searchquery;

	@Column(name = "totalFiles")
	private Long totalFiles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}

	public String getSearchquery() {
		return searchquery;
	}

	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}

	public Long getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(Long totalFiles) {
		this.totalFiles = totalFiles;
	}

	@Override
	public String toString() {
		return "SearchLog [id=" + id + ", timestamp=" + timestamp + ", userId=" + userId + ", searchResult="
				+ searchResult + ", searchquery=" + searchquery + ", totalFiles=" + totalFiles + "]";
	}

}

