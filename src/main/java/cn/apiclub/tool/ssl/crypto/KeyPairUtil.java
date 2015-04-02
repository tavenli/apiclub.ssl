package cn.apiclub.tool.ssl.crypto;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.DSAKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.crypto.interfaces.DHKey;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyParameters;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

/**
 * Provides utility methods for the generation of keys.
 */
public final class KeyPairUtil
{
	/** Logger */
	private static final Logger LOG = Logger.getLogger(KeyPairUtil.class.getName());

	/** Constant representing unknown key size */
	public static final int UNKNOWN_KEY_SIZE = -1;

	/**
	 * Private to prevent construction.
	 */
	private KeyPairUtil()
	{
		// Nothing to do
	}

	/**
	 * Generate a key pair.
	 * 
	 * @param keyPairType Key pair type to generate
	 * @param iKeySize Key size of key pair
	 * @return A key pair
	 * @throws CryptoException If there was a problem generating the key pair
	 */
	public static KeyPair generateKeyPair(KeyPairType keyPairType, int iKeySize)
	    throws CryptoException
	{
		try
		{
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(keyPairType.name());

			// Create a SecureRandom
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

			// Initialize key pair generator with key strength and a randomness
			keyPairGen.initialize(iKeySize, rand);

			// Generate and return the key pair
			return keyPairGen.generateKeyPair();
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new CryptoException(MessageFormat.format("Could not generate ''{0}'' key pair.", keyPairType), ex);
		}
		catch (InvalidParameterException ex)
		{
			throw new CryptoException(MessageFormat.format("Invalid parameter for a ''{0}'' key pair.", keyPairType), ex);
		}
	}

	/**
	 * Get the key size of a public key.
	 * 
	 * @param pubKey The public key
	 * @return The key size, {@link #UNKNOWN_KEY_SIZE} if not known
	 */
	public static int getKeyLength(PublicKey pubKey)
	{
		if (pubKey instanceof RSAKey)
		{
			return ((RSAKey) pubKey).getModulus().bitLength();
		}
		else if (pubKey instanceof DSAKey)
		{
			return ((DSAKey) pubKey).getParams().getP().bitLength();
		}
		else if (pubKey instanceof DHKey)
		{
			return ((DHKey) pubKey).getParams().getP().bitLength();
		}
		else if (pubKey instanceof ECKey)
		{
			// TODO: how to get key size from these?
			return UNKNOWN_KEY_SIZE;
		}

		LOG.warning("Don't know how to get key size from key " + pubKey);
		return UNKNOWN_KEY_SIZE;
	}

	/**
	 * Get the key size of a key represented by key parameters.
	 * 
	 * @param keyParams The key parameters
	 * @return The key size, {@link #UNKNOWN_KEY_SIZE} if not known
	 */
	public static int getKeyLength(AsymmetricKeyParameter keyParams)
	{
		if (keyParams instanceof RSAKeyParameters)
		{
			return ((RSAKeyParameters) keyParams).getModulus().bitLength();
		}
		else if (keyParams instanceof DSAKeyParameters)
		{
			return ((DSAKeyParameters) keyParams).getParameters().getP().bitLength();
		}
		else if (keyParams instanceof DHKeyParameters)
		{
			return ((DHKeyParameters) keyParams).getParameters().getP().bitLength();
		}
		else if (keyParams instanceof ECKeyParameters)
		{
			// TODO: how to get key length from these?
			return UNKNOWN_KEY_SIZE;
		}

		LOG.warning("Don't know how to get key size from parameters " + keyParams);
		return UNKNOWN_KEY_SIZE;
	}
}
