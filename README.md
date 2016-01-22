# apiclub.ssl

具体使用例子请参考 apiclub.ssl\src\test\java\cn\apiclub\tool\ssl\AppDemo.java


	/**
	 * Class used to add the server's certificate to the KeyStore with your trusted
	 * certificates.
	 * 
	 * Usage: java InstallCert <host>[:port] [passphrase]
	 * 
	 * java InstallCert www.test.com 或 java -jar JavaInstallCert.jar github.com
	 * 
	 * Enter certificate to add to trusted keystore or 'q' to quit: [1]
	 * 
	 * 输入1，回车，然后会在当前的目录下产生一个名为"jssecacerts"的证书。 将证书拷贝到$JAVA_HOME/jre/lib/security目录下
	 * 或者通过以下方式： System.setProperty("javax.net.ssl.trustStore","你的jssecacerts证书路径");
	 * 
	 * 注意：因为是静态加载，所以要重新启动你的Web Server，证书才能生效。
	 * 
	 * 生成的 jssecacerts 证书是将原JDK下的文件加入新的证书后生成的，所以之前的jssecacerts内容并不丢失
	 * 
	 */


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


