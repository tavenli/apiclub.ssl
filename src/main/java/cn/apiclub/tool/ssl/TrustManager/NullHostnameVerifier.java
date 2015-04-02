package cn.apiclub.tool.ssl.TrustManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 
 * 防止出现 No subject alternative names matching IP address
 * 
 * @author Taven
 *
 */
public class NullHostnameVerifier implements HostnameVerifier {
	
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
    
}