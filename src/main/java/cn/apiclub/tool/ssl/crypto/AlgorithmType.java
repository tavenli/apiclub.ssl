package cn.apiclub.tool.ssl.crypto;

import java.util.HashMap;

/**
 * Algorithm type. Enum constant names are compatible with JCA standard names.
 * 
 * @see <a href="http://download.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html">JCA
 *      Standard Names</a>
 */
public enum AlgorithmType
{
	DSA("1.2.840.10040.4.1"),
	RSA("1.2.840.113549.1.1.1");

	/** OID-to-type map */
	private static final HashMap<String, AlgorithmType> OID_MAP = new HashMap<String, AlgorithmType>();
	static
	{
		for (AlgorithmType at : values())
		{
			OID_MAP.put(at.oid, at);
		}
	}

	private final String oid;

	private AlgorithmType(String oid)
	{
		this.oid = oid;
	}

	/**
	 * Gets an AlgorithmType corresponding to the given object identifier.
	 * 
	 * @param oid the object identifier
	 * @return the corresponding AlgorithmType, <code>null</code> if unknown
	 */
	public static AlgorithmType valueOfOid(String oid)
	{
		return OID_MAP.get(oid);
	}

	/**
	 * Gets a string representation of algorithm type corresponding to the given object identifier.
	 * 
	 * @param oid the object identifier
	 * @return the corresponding algorithm type as string, <code>oid</code> itself if unknown
	 */
	public static String toString(String oid)
	{
		AlgorithmType type = valueOfOid(oid);
		if (type != null)
		{
			return type.toString();
		}
		return oid;
	}
}
