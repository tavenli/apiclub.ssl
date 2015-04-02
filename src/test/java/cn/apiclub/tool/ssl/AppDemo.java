package cn.apiclub.tool.ssl;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import cn.apiclub.tool.ssl.TrustManager.NoneStrictX509TrustManager;
import cn.apiclub.tool.ssl.TrustManager.NullHostnameVerifier;
import cn.apiclub.tool.ssl.crypto.KeyPairType;
import cn.apiclub.tool.ssl.crypto.KeyStoreType;
import cn.apiclub.tool.ssl.crypto.SignatureType;

/**
 * 
 *
 * @author Taven
 *
 */
public class AppDemo {

	public static void main(String[] args) throws Exception {
		
		String outPutFilePath = "d:\\";
		String host = "github.com";
		int port = 443;
		
		host = "121.41.170.36";
		
		/**
		 * 方式1：将不信任的证书导入到jdk的信任证书中
		 */
		//将远程的https证书下载到本地，然后把本地的 jssecacerts 合并，生成一个新的 jssecacerts
		//InstallCert.downloadCert(host, port, "changeit", outPutFilePath);
		
		//将远程的https证书下载后加到前jdk中的 jssecacerts 文件中 (jdk的路径自动通过 java.home 环境变量查找)
		//InstallCert.installCertToJdk(host, port);
		
		//keytool命令导入
		// keytool -import -file <src_cer_file> –keystore <dest_cer_store>

		// keytool -import -alias tomcat -file server.cer -keystore D:\DevEnv\J2EE_PATH\jdk6\jre\lib\security\cacerts
		
		//将一个https的公钥导出到本地
		//SSLExtractor.extractor(host, port, outPutFilePath);
		
		
		/**
		 * 方式2：实现自己的证书信任管理器类，绕过信任证书检查
		 */
    	//下面对不信任证书的访问
    	
    	//会报错 PKIX path building failed
    	//httpsRequest1("https://121.41.170.36");
    	
    	//不信任的证书，也能正常访问
		//httpsRequest2("https://121.41.170.36");
		
		//使用自己封装的 SSLHttpRequest
		httpsRequest3("https://121.41.170.36");
    	
		/**
		 * 方式3：通过指定 javax.net.ssl.trustStore 属性，让 httpsRequest1 不报错
		 */
		//useProperty();
		
		/**
		 * 方式4：使用Jvm参数指定 Java App 和 Tomcat 都支持
		 */
		//useJvmOptions();
		
		//不使用keytool命令创建证书，直接使用java代码的方式来生成证书，
		createKeyStore();
		
	}
	
	private static void useProperty() throws Exception {
		//使用 InstallCert.downloadCert(host, port, "changeit", outPutFilePath);下载到 CLIENT_KEY_STORE 路径
		String CLIENT_KEY_STORE = "d:\\jssecacerts";
		// 将 CLIENT_KEY_STORE 设置到系统属性 javax.net.ssl.trustStore 中，在实际生产环境中使用时候，需要在应用最开始的时候执行
        System.setProperty("javax.net.ssl.trustStore", CLIENT_KEY_STORE);
        //客户端设置日志输出级别为DEBUG
        System.setProperty("javax.net.debug", "ssl,handshake");
        
        //直接调用 httpsRequest1 也不会报错了
        httpsRequest1("https://121.41.170.36");
        
        
        //以下是可选的属性设置项
/*        
        System.setProperty("javax.net.ssl.keyStore", "<some specific .jks file>");
        System.setProperty("javax.net.ssl.keyStorePassword", "<password>");
        System.setProperty("javax.net.ssl.trustStore", "<same .jks file>");
        System.setProperty("javax.net.ssl.trustStorePassword", "<password>"); 
        
        */
       
        
	}
	
	private static void useJvmOptions() throws Exception {
		
		/**
		 * Convert PEM-encoded Cert to DER Encoding
		 * openssl x509 -in etc/pki/incommon-root-cert.pem -out tmp/incommon-root-cert.der -outform DER
		 * 
		 * Keytool Import Command
		 * keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -file tmp/incommon-root-cert.der -alias incommon
		 * 
		 * List Trusted Certificates
		 * keytool -v -list -keystore $JAVA_HOME/jre/lib/security/cacerts
		 * 
		 * 
		 */
		
		
		//Sample setenv.sh Tomcat Script
/*		
		# Uncomment the next 4 lines for custom SSL keystore
		# used by all deployed applications
		#KEYSTORE="$HOME/path/to/custom.keystore"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.keyStore=$KEYSTORE"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.keyStoreType=BKS"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.keyStorePassword=changeit"
		 
		# Uncomment the next 4 lines to allow custom SSL trust store
		# used by all deployed applications
		#TRUSTSTORE="$HOME/path/to/custom.truststore"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.trustStore=$TRUSTSTORE"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.trustStoreType=BKS"
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.ssl.trustStorePassword=changeit"
		 
		# Uncomment the next line to print SSL debug trace in catalina.out
		#CATALINA_OPTS=$CATALINA_OPTS" -Djavax.net.debug=ssl"
		 
		export CATALINA_OPTS
		*/
		
		
		
		
	}
	

	
    private static void httpsRequest1(String httpsUrl) throws Exception{
    	//防止出现 No subject alternative names matching IP address
    	HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
    	
    	// 创建URL对象
        URL myURL = new URL(httpsUrl);
 
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
 
        // 取得该连接的输入流，以读取响应内容
        InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
 
        // 读取服务器的响应内容并显示
        int respInt = insr.read();
        while (respInt != -1) {
            System.out.print((char) respInt);
            respInt = insr.read();
        }
    	
    }
    private static void httpsRequest2(String httpsUrl) throws Exception{
    	
    	//防止出现 No subject alternative names matching IP address 方式1：
    	HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
    	
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
    	// NoneStrictX509TrustManager 和 NoneStrictX509TrustManager 的使用
    	//TrustManager[] tm = { new CrossX509TrustManager() };
        TrustManager[] tm = { new NoneStrictX509TrustManager() };        
        
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        
        // 创建URL对象
        URL myURL = new URL(httpsUrl);
        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
        
        //防止出现 No subject alternative names matching IP address 方式2：
        //httpsConn.setHostnameVerifier(new NullHostnameVerifier());
        
        //关键步骤，把自定义的 NoneStrictX509TrustManager 设置到 HttpsURLConnection
        httpsConn.setSSLSocketFactory(ssf);
        
        // 取得该连接的输入流，以读取响应内容
        InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
        // 读取服务器的响应内容并显示
        int respInt = insr.read();
        while (respInt != -1) {
            System.out.print((char) respInt);
            respInt = insr.read();
        }
        
    }
	
	private static void httpsRequest3(String httpsUrl) throws Exception{
		//
		SSLHttpRequest request = SSLHttpRequest.get(httpsUrl);  
	    //针对单项证书给予忽略（注意，双向证书需要导入证书文件）  
	    request.trustAllCerts();
	    //信任所有地址
	    request.trustAllHosts();
	    
	    String response = request.body();
	    
	    System.out.println(response);
	}
	
	
	private static void createKeyStore() throws Exception {
		
        //直接使用java代码生成证书
        //new KeyTool().createKeyStore(new KeyStoreEntity("d:\\server.keystore"));
        
        KeyStoreEntity keyStoreEntity = new KeyStoreEntity();
        
        keyStoreEntity.setCommonName("www.apiclub.cn");
        keyStoreEntity.setOrganisationUnit("apiclub.cn");
        keyStoreEntity.setOrganisation("apiclub");
        keyStoreEntity.setLocality("ShangHai");
        keyStoreEntity.setState("SH");
        keyStoreEntity.setCountryCode("CN");
        keyStoreEntity.setEmail("service@apiclub.cn");
        
        //JKS证书：Java Key Store (keytool 工具支持的证书类型)
        keyStoreEntity.setKeyStoreType(KeyStoreType.JKS);
        keyStoreEntity.setKeyPairType(KeyPairType.RSA);
        keyStoreEntity.setKeyPairSize(2048);
        keyStoreEntity.setSignatureType(SignatureType.SHA256withRSA);
        keyStoreEntity.setDays(3650);
        keyStoreEntity.setKeyStoreFile(new File("d:\\server.keystore"));
        
        new KeyTool().createKeyStore(keyStoreEntity);
        
        
	}
	
}
