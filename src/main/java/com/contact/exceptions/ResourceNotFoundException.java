package com.contact.exceptions;

public class ResourceNotFoundException extends RuntimeException
{
	private String resource;
	
	private Integer resouceId;
	
	private String resourceName;
	
	 public ResourceNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	 
	public ResourceNotFoundException(String resource, Integer resouceId, String resourceName) {
		super(String.format("%s not found with %s having value : %d",resource,resourceName,resouceId));
		this.resource = resource;
		this.resouceId = resouceId;
		this.resourceName = resourceName;
	}


	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Integer getResouceId() {
		return resouceId;
	}

	public void setResouceId(Integer resouceId) {
		this.resouceId = resouceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}


	@Override
	public String toString() {
		return "ResourceNotFoundException [resource=" + resource + ", resouceId=" + resouceId + ", resourceName="
				+ resourceName + "]";
	}
	
}
