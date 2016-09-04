package com.anyway.imagemark.utils;

import java.io.InputStream;
import java.util.Properties;

public class ParseProperties {
	public String getRelativePropertyValues(String propertiesFile,String item){
		Properties   props   =  new  Properties();
		InputStream in;
		try{
			in=getClass().getResourceAsStream("/"+propertiesFile);
			//in=getClass().getResourceAsStream("/org/www/kangva/commonfile/commonConfig.properties");  ---------------------这个是第一个文件传的路径（注意区别很简单，就是加上包的路径）
			props.load(in);
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
		if(props.isEmpty())
		{
			return "";
		}
		return props.getProperty(item);
	}
}
