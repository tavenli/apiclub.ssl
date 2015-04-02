package cn.apiclub.tool.ssl;

import java.io.FileOutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLExtractor {

	/**
	 * 将一个https的公钥导出到本地
	 * 
	 * SSLExtractor.extractor("github.com", 443, "d:\\");
	 * 
	 * @param host
	 * @param port
	 * @param outPath
	 * @throws Exception
	 */
	public static void extractor(String host, int port, String outPath) throws Exception{
		
		final List certs = new ArrayList();

        X509TrustManager trust = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                System.out.println(s);
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                for (int i = 0; i < x509Certificates.length; i++) {
                    X509Certificate cert = x509Certificates[i];
                    System.out.println("Loading certificate " + cert.getSubjectDN() + " issued by: " + cert.getIssuerDN());
                    certs.add(x509Certificates[i]);
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trust}, null);
        SSLSocket socket = (SSLSocket) sslContext.getSocketFactory().createSocket(host, port);

        socket.getInputStream();
        socket.getSession().getPeerCertificates();
        socket.close();

        Iterator iterator = certs.iterator();
        while (iterator.hasNext()) {
            X509Certificate cert = (X509Certificate) iterator.next();
            String outputFile = "";
            //outputFile = cert.getSubjectDN().getName().replaceAll("[^a-zA-Z0-9-=_\\.]", "_") + ".cer";
            outputFile = cert.getSubjectDN().getName().hashCode() + ".cer";
            System.out.println("Serializing certificate to: " + outputFile);
            FileOutputStream certfos = new FileOutputStream(outPath + outputFile);
            certfos.write(cert.getEncoded());
            certfos.close();
        }
        
	}
	
}
