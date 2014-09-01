package org.xmlrpc;

 
public interface ImageSearch  {
	
	public String[] getDescriptors() ;

	public String[] getDistanceFunctions() ;
	
	public String[] getDatasets() ;

	public String[] getResultPerPage() ;

	public String[] getRandomSet(QueryParams queryParams);


	public String [] getResultForQuery(QueryParams queryParams) ;
	
 
}
