package cn.apiclub.tool.ssl;

import java.io.File;

public class Constant {
	
	/**
	 * 当前系统的文件目录分割符
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * 当前系统文件编码
	 */
	public static final String FILE_ENCODING = System.getProperty("file.encoding");

	/**
	 * 当前OS的换行符
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	public static final String JDK_SECURITY_DIR = System.getProperty("java.home") + File.separatorChar + "lib"	+ File.separatorChar + "security" ;
	
	
	public static final String CACERTS = JDK_SECURITY_DIR + FILE_SEPARATOR + "cacerts";
	
	public static final String JSSE_CACERTS = JDK_SECURITY_DIR + FILE_SEPARATOR + "jssecacerts";
	
	
	public static final String JDK_CERTS_PASSWORD = "changeit";
	
	
	
}
