package junyi.ddz.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
/**
 * 控制文件读写，得分记录
 * @author junyi
 *
 */
public class PropertiesUtil {
	public final static String SCORE_FILE = "data" + File.separator + "score.properties";
	private Properties prop;
	private String f_path;
	
	/**
	 * 数据存储文件
	 * @param path 文件地址
	 */
	public PropertiesUtil(String path){
		prop = new Properties();
		this.f_path = path;
		try{
			FileInputStream fis = new FileInputStream(f_path);
			prop.load(fis);
			fis.close();
			System.out.println("properties load success!");
		} catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}
	
	/**
	 * 获取某key的值
	 * @param key 属性名
	 * @return
	 */
	public String getProperty(String key){
		try{
			if(!prop.containsKey(key)){
				FileInputStream fis = new FileInputStream(f_path);
				prop.load(fis);
				fis.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	/**
	 * 修改或者添加 
	 * @param key 属性
	 * @param value 属性值
	 */
	public void setProper(String key,String value){
		try {
			prop.setProperty(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 数据存回
	 */
	public void store(){
		try {
			FileOutputStream fos = new FileOutputStream(f_path);
			prop.store(fos, new Date().toString());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}