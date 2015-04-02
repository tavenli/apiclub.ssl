package cn.apiclub.tool.ssl.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

/**
 * Provides utility methods for the creation of message digests.
 */
public final class DigestUtil
{
	/**
	 * Private to prevent construction.
	 */
	private DigestUtil()
	{
		// Nothing to do
	}

	/**
	 * Get the digest of a message as a formatted String.
	 * 
	 * @param bMessage The message to digest
	 * @param digestType The message digest algorithm
	 * @return The message digest
	 * @throws CryptoException If there was a problem generating the message digest
	 */
	public static String getMessageDigest(byte[] bMessage, DigestType digestType)
	    throws CryptoException
	{
		// Create message digest object using the supplied algorithm
		MessageDigest messageDigest;
		try
		{
			messageDigest = MessageDigest.getInstance(digestType.name());
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new CryptoException("Could not create ''"+ digestType +"'' message digest", ex);
		}

		// Create raw message digest
		byte[] bFingerPrint = messageDigest.digest(bMessage);

		// Return the formatted message digest
		StringBuilder sb = StringUtil.toHex(bFingerPrint, 2, ":");
		return sb.toString();
	}
}
