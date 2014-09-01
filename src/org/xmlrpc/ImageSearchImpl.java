package org.xmlrpc;



import java.util.ArrayList;

import org.xmlrpc.android.XMLRPCException;

  
 
public class ImageSearchImpl implements ImageSearch {


	private String[] datasets = new String[] {"CBIRPIBAP", "E-80", /*"Caltech-256"*/};

	private String[] descriptors = new String[] {".rgb", ".jpg.eGeometricFeatures2", ".txt.pip", /*".jpg.oRGBHistograms", */};

	private String[] distanceFunctions = new String[] { "L2", "LInf", "L1",  "DWT"};

	private ImageSearchService imageSearch = new ImageSearchService();

	
	private static String DATASET_PATH = "C:/xampp/htdocs/pibapdb/";

	private ArrayList<String> albumNames;

	String[] resultPerPage = {"10 images", "20 images", "30 images"};

	public String[] getResultPerPage() {
		return resultPerPage;
	}

	public String[] getDescriptors() {
		return descriptors;
	}

	public String[] getDistanceFunctions() {
		return distanceFunctions;
	}
	public String[] getDatasets() {
		return datasets;
	}


 
	public String[] getRandomSet(QueryParams queryParams) {

		String datasetPath = "C:/xampp/htdocs/" + queryParams.getDataset();
		String dataExtension = queryParams.getDescriptor();

		String[] ret = null;
		try {
			ret = imageSearch.getRandomSet(datasetPath, dataExtension);
		} catch (XMLRPCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("randomset: " + ret);
		return ret;

	}
	public String [] getResultForQuery(QueryParams queryParams) {

		String datasetPath = "C:/xampp/htdocs/" + queryParams.getDataset();
		String dataExtension = queryParams.getDescriptor();
		String query = queryParams.getFileName();
		
		System.err.println("datasetPath:" + datasetPath);
		System.err.println("dataExtension:" + dataExtension);
		System.err.println("query:" + query);
		
		String[] ret = null;
		try {
			ret = imageSearch.query(datasetPath, dataExtension, query);
		} catch (XMLRPCException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	 

	
	
}
