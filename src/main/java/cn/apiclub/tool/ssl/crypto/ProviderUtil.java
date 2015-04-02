package cn.apiclub.tool.ssl.crypto;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides security provider utility methods.
 */
public final class ProviderUtil
{
	/**
	 * Private to prevent construction.
	 */
	private ProviderUtil()
	{
		// Nothing to do
	}

	/**
	 * Get the PKCS #11 <code>Provider</code>s available on the system.
	 * 
	 * @return the (possibly empty) collection of available PKCS #11 <code>Provider</code>s
	 */
	public static Collection<Provider> getPkcs11Providers()
	{
		ArrayList<Provider> p11s = new ArrayList<Provider>();
		for (Provider prov : Security.getProviders())
		{
			String pName = prov.getName();
			// Is it a PKCS #11 provider?
			/*
			 * TODO: is there a better way to find out? Could try instanceof sun.security.pkcs11.SunPKCS11 but
			 * that would require the class to be available?
			 */
			if (pName.startsWith("SunPKCS11-"))
			{
				p11s.add(prov);
			}
		}
		return p11s;
	}
}
