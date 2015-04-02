package cn.apiclub.tool.ssl.crypto;

/**
 * Represents a cryptographic exception.
 */
public class CryptoException
    extends Exception
{
	/**
	 * Creates a new CryptoException.
	 */
	public CryptoException()
	{
		super();
	}

	/**
	 * Creates a new CryptoException with the specified message.
	 * 
	 * @param sMessage Exception message
	 */
	public CryptoException(String sMessage)
	{
		super(sMessage);
	}

	/**
	 * Creates a new CryptoException with the specified message and cause throwable.
	 * 
	 * @param causeThrowable The throwable that caused this exception to be thrown
	 * @param sMessage Exception message
	 */
	public CryptoException(String sMessage, Throwable causeThrowable)
	{
		super(sMessage, causeThrowable);
	}

	/**
	 * Creates a new CryptoException with the specified cause throwable.
	 * 
	 * @param causeThrowable The throwable that caused this exception to be thrown
	 */
	public CryptoException(Throwable causeThrowable)
	{
		super(causeThrowable);
	}
}
