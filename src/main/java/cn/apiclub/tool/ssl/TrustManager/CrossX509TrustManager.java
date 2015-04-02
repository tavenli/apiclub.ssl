package cn.apiclub.tool.ssl.TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 本类实现一个空的信任证书管理器，可以让客户端请求绕过信任证书的检查.
 * 
 * 具体使用请参考 AppDemo 中的 main 方法例子
 * 
 * @author Taven
 *
 */
public class CrossX509TrustManager implements X509TrustManager{

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		//无需任何逻辑
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		//无需任何逻辑
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		//直接返回空
		return null;
	}

}
