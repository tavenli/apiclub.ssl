package cn.apiclub.tool.ssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class InstallCert {
	

	
	/**
	 * 把证书加入到当前的 java.home 中
	 * 
	 * @param host
	 * @param port
	 * @throws Exception
	 */
	public static void installCertToJdk(String host, int port) throws Exception {
		
		downloadCert(host, port, Constant.JDK_CERTS_PASSWORD, Constant.JDK_SECURITY_DIR);
		
	}
	
	/**
	 * 
	 * InstallCert.downloadCert("github.com", 443, "changeit", "d:\\");
	 * 
	 * 
	 * @param host
	 * @param port
	 * @param password
	 * @throws Exception
	 */
	public static void downloadCert(String host, int port, String password, String outPath) throws Exception {
		char[] passphrase = password.toCharArray();
		
		File file = new File("jssecacerts");
		if (file.isFile() == false) {
			char SEP = File.separatorChar;
			File dir = new File(Constant.JDK_SECURITY_DIR);
			file = new File(Constant.JSSE_CACERTS);
			//System.out.println(file.getCanonicalPath());
			System.out.println(file.getAbsolutePath());
			if (file.isFile() == false) {
				file = new File(Constant.CACERTS);
				System.out.println(file.getAbsolutePath());
			}
		}
		System.out.println("Loading KeyStore " + file);
		InputStream in = new FileInputStream(file);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(in, passphrase);
		in.close();

		SSLContext context = SSLContext.getInstance("TLS");

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);

		X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();

		//System.out.println("Opening connection to " + host + ":" + port + "...");
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
			//System.out.println("Starting SSL handshake...");
			socket.startHandshake();
			socket.close();
			//System.out.println();
			//System.out.println("No errors, certificate is already trusted");
		} catch (SSLException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace(System.out);
		}

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
			System.out.println("Could not obtain server certificate chain");
			return;
		}

		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		//System.out.println();
		//System.out.println("Server sent " + chain.length + " certificate(s):");
		//System.out.println();
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = chain[i];
			System.out.println(" " + (i + 1) + " Subject "	+ cert.getSubjectDN());
			System.out.println("   Issuer  " + cert.getIssuerDN());
			sha1.update(cert.getEncoded());
			System.out.println("   sha1    " + toHexString(sha1.digest()));
			md5.update(cert.getEncoded());
			System.out.println("   md5     " + toHexString(md5.digest()));
			System.out.println();
		}

		
		//默认把第一个证书加入到 jssecacerts
		X509Certificate cert = chain[0];
		String alias = host + "-1";
		ks.setCertificateEntry(alias, cert);

		OutputStream out = new FileOutputStream(outPath + Constant.FILE_SEPARATOR + "jssecacerts");
		ks.store(out, passphrase);
		out.close();

		System.out.println();
		System.out.println(cert);
		System.out.println();
		System.out.println("Added certificate to keystore 'jssecacerts' using alias '" + alias + "'");
	}


	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SavingTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}
	
	
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
	public static void main(String[] args) throws Exception {
		String host;
		int port;
		String passphrase;
		if ((args.length == 1) || (args.length == 2)) {
			String[] c = args[0].split(":");
			host = c[0];
			port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
			passphrase = (args.length == 1) ? Constant.JDK_CERTS_PASSWORD : args[1];
			
		} else {
			System.out.println("Usage: java InstallCert <host>[:port] [passphrase]");
			return;
		}

		InstallCert.downloadCert(host, port, passphrase, "");

	}

}
