package cn.apiclub.tool.ssl;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;

import cn.apiclub.tool.ssl.crypto.KeyPairType;
import cn.apiclub.tool.ssl.crypto.KeyPairUtil;
import cn.apiclub.tool.ssl.crypto.KeyStoreType;
import cn.apiclub.tool.ssl.crypto.KeyStoreUtil;
import cn.apiclub.tool.ssl.crypto.KeyStoreWrapper;
import cn.apiclub.tool.ssl.crypto.SignatureType;
import cn.apiclub.tool.ssl.crypto.X509CertUtil;

/**
 * 
 *
 * @author Taven
 *
 */
public class KeyTool {
	
	private void comment(){
		/**
		 * 
		 * 手工方式生成SSL证书
		 * 
		 * 生成server.keystore，3650表示10年有效期
		 * keytool -genkey -alias server -keyalg RSA -validity 3650 -keystore server.keystore
		 * 或
		 * keytool -genkey -alias server -keyalg RSA -keystore server.keystore
		 * 
		 * ★快捷生成证书★
		 * keytool -genkey -v -alias server -keyalg RSA -validity 3650 -keystore server.keystore -dname "CN=www.apiclub.cn,OU=apiclub,O=apiclub,L=CS,ST=HN,C=CN" -storepass 123456 -keypass 123456
		 * 
		 * 导出server端信任证书为 server.cer
		 * keytool -export -alias server -keystore server.keystore -file server.cer
		 * 或
		 * keytool -export -alias server -keystore server.keystore -file server.crt
		 * 
		 * 生成client.keystore
		 * keytool -genkey -alias server -keyalg RSA -validity 3650 -keystore client.keystore
		 * 
		 * 导出client端信任证书为 client.cer
		 * keytool -export -alias client -keystore client.keystore -file client.cer
		 * 
		 * 
		 * ★创建truststore证书★
		 * 
		 * 导入 server 端的 server.cer 到 client 端 truststore 文件 client.truststore 中，并加以别名server
		 * keytool -import -file server.cer -keystore client.truststore -alias server
		 * 
		 * 导入 client 端的 client.cer 到 server 端 truststore 文件 server.truststore 中，并加以别名client
		 * keytool -import -file client.cer -keystore server.truststore -alias client
		 * 
		 * 
		 * openssl req -new -x509 -days 3650 -nodes -out server.crt -keyout server.key
		 * 
		 * (注意生成域名证书的时候，CN名称 Common Name 要为域名的名称)
		 *
		 * ★查看server.keystore内容★
		 * keytool -list -keystore server.keystore
		 * 
		 * ★keytool命令导入到JDK的信任证书中★
		 * keytool -import -file <src_cer_file> –keystore <dest_cer_store>
		 * 	
		 * keytool -import -alias server-xxx -file server.cer -keystore D:\jdk6\jre\lib\security\cacerts
		 * 
		 * keytool -import -alias server-xxx -file server.cer -keystore D:\jdk6\jre\lib\security\cacerts -keypass changeit
		 * 
		 * keytool -import -trustcacerts -alias server-xxx -file server.cer -keystore D:\jdk6\jre\lib\security\cacerts -keypass changeit
		 * 
		 * JVM中可使用的参数
		 * 
		 * -Djavax.net.ssl.keyStore=clientKeys  
		 * -Djavax.net.ssl.keyStoreType=BKS
		 * -Djavax.net.ssl.keyStorePassword=password 
		 * 
		 * -Djavax.net.ssl.trustStore=clientTrusts
		 * -Djavax.net.ssl.trustStoreType=BKS
		 * -Djavax.net.ssl.trustStorePassword=password
		 * 
		 * 
		 * Tomcat下单向认证
		 * <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true" maxThreads="150" scheme="https" secure="true" clientAuth="false" 
		 * sslProtocol="TLS" keystoreFile="server.keystore" keystorePass="changeit" keystoreType="jks" />  
		 * 
		 * Tomcat下双向认证（未验证）
		 * <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true" maxThreads="150" scheme="https" secure="true" clientAuth="true" 
		 * sslProtocol="TLS" keystoreFile="server.keystore" keystorePass="changeit" keystoreType="jks" truststoreFile="client.truststore" 
		 * truststorePass="changeit" truststoreType="jks" />
		 * 
		 * 
		 * sslProtocol 中的 TLS 和 SSL 区别：TLS是SSL的扩展，TLS属于更高安全的协议
		 * TLS：(Transport Layer Security，传输层安全协议)
		 * SSL：（Secure Socket Layer，安全套接字层）
		 * 
		 */
		
		
	}
	
	
	public void createKeyStore(KeyStoreEntity keyStoreEntity) throws Exception{
		
		this.loadBCProvider();
		
		KeyStoreType keyStoreType = keyStoreEntity.getKeyStoreType();
		KeyPairType keyPairType = keyStoreEntity.getKeyPairType();
		int keyPairSize = keyStoreEntity.getKeyPairSize();
		SignatureType signatureType = keyStoreEntity.getSignatureType();
		int days = keyStoreEntity.getDays();
		char[] cPassword = keyStoreEntity.getPassword() !=null ? keyStoreEntity.getPassword().toCharArray() : Constant.JDK_CERTS_PASSWORD.toCharArray();
		File keystoreFile = keyStoreEntity.getKeyStoreFile();
		
		KeyStore newKeyStore = KeyStoreUtil.createKeyStore(keyStoreType);
		KeyStoreWrapper keyStoreWrap = new KeyStoreWrapper(newKeyStore);
		
		KeyPair keyPair = KeyPairUtil.generateKeyPair(keyPairType, keyPairSize);
		
		X509Certificate certificate = X509CertUtil.generateCert(keyStoreEntity.getCommonName(), keyStoreEntity.getOrganisationUnit(), keyStoreEntity.getOrganisation(),
	    		keyStoreEntity.getLocality(), keyStoreEntity.getState(), keyStoreEntity.getCountryCode(), keyStoreEntity.getEmail(), 
	    		days, keyPair.getPublic(), keyPair.getPrivate(), signatureType);
		
		// Get the keystore
		KeyStore keyStore = keyStoreWrap.getKeyStore();

		// Get an alias for the new keystore entry
		String sAlias = X509CertUtil.getCertificateAlias(certificate).toLowerCase();

		// Store the new one
		keyStore.setKeyEntry(sAlias, keyPair.getPrivate(), cPassword, new X509Certificate[] { certificate });
		
		keyStoreWrap.setEntryPassword(sAlias, cPassword);
		keyStoreWrap.setChanged(true);

		// Save the keystore to file
		keyStoreWrap.setKeyStore(KeyStoreUtil.saveKeyStore(keyStoreWrap.getKeyStore(), keystoreFile, cPassword));

		// Update the keystore wrapper
		keyStoreWrap.setPassword(cPassword);
		keyStoreWrap.setKeyStoreFile(keystoreFile);
		keyStoreWrap.setChanged(false);
		
		System.out.println(MessageFormat.format("New KeyStrore File : {0}", keystoreFile.getAbsolutePath()));
		
	}
	
	public void loadBCProvider() throws Exception{
		
		Provider bcProv = Security.getProvider("BC");

		if (bcProv == null) {
			// Instantiate the Bouncy Castle provider
			Class<?> bcProvClass = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
			bcProv = (Provider) bcProvClass.newInstance();

			// Add BC as a security provider
			Security.addProvider(bcProv);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new KeyTool().createKeyStore(new KeyStoreEntity("d:\\server.keystore"));
	}

}
