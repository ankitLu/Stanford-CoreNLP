package com.work.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CreateCorpus {
	
	String corpus;
	

	public CreateCorpus(String path) {
		// TODO Auto-generated constructor stub
		try {
			
			String folder = System.getProperty("user.dir");
			//folder+="/corpus/HealthNews";
			folder += path;
			File dir = new File(folder);
	        File[] files = dir.listFiles();
			StringBuilder sb = new StringBuilder();
			
			 for (File f : files) {
		            if(f.isFile()) {
		            	System.out.println("Reading file "+ f.getName());
		                BufferedReader inputStream = null;

		                try {
		                    inputStream = new BufferedReader(
		                                    new FileReader(f));
		                    String line;

		                    while ((line = inputStream.readLine()) != null) {
		                        sb.append(line);
		                    }
		                }
		                finally {
		                    if (inputStream != null) {
		                        inputStream.close();
		                    }
		                }
		            }
		        }
			 String string = sb.toString();
			 System.out.println(string);
			 this.corpus = string;
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	
	public static void main(String args[]){
		
		
		new CreateCorpus("/corpus/HealthNews");
	}
	
	public String getCorpus(){
		return this.corpus;
	}
	
	
	
}
