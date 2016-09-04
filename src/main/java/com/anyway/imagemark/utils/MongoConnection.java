/**
 * 
 */
package com.anyway.imagemark.utils;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * @author Kario
 *
 */
public class MongoConnection {
	private DB db;
	private MongoClient client;
       public  MongoConnection(String url,int port){
    	  try {
			MongoClient client=new MongoClient(url,port);
			db=client.getDB("picAd");
			} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	
       }
       public MongoClient getClient(){
    	   return this.client;
       }
       public DBCollection getCollection(String collName){
   		return db.getCollection(collName);
   	}
}
