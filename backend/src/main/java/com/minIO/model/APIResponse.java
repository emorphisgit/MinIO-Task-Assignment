package com.minIO.model;

public class APIResponse {
	private String message;
	private Boolean status;
	private Object object;
	private Long senderCount;
	private String id;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "APIResponse [message=" + message + ", status=" + status + "]";
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Long getSenderCount() {
		return senderCount;
	}

	public void setSenderCount(Long senderCount) {
		this.senderCount = senderCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}


