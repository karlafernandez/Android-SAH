package org.xmlrpc;
/**
 * 
 */

import java.io.Serializable;

public class QueryParams implements Serializable{
	private String query;

	
	private String descriptor;
	private String distanceFunction;
	private String dataset;
	private String resultPerPage;
	
	public QueryParams() {
		// TODO Auto-generated constructor stub
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public String getDistanceFunction() {
		return distanceFunction;
	}
	public void setDistanceFunction(String distanceFunction) {
		this.distanceFunction = distanceFunction;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getResultPerPage() {
		return resultPerPage;
	}
	public void setResultPerPage(String resultPerPage) {
		this.resultPerPage = resultPerPage;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getFileName() {
		int pos = query.lastIndexOf("/");
		return query.substring(pos+1);
	}
}