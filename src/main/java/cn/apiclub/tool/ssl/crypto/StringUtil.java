package cn.apiclub.tool.ssl.crypto;

import java.math.BigInteger;
import java.util.Locale;

import org.bouncycastle.asn1.DERInteger;

/**
 * String utilities.
 * 
 * @author Ville Skyttä
 */
public class StringUtil
{
	/**
	 * Convert the supplied object to hex characters sub-divided by spaces every given number of characters,
	 * and left-padded with zeros to fill group size.
	 * 
	 * @param obj Object (byte array, BigInteger, DERInteger)
	 * @param groupSize number of characters to group hex characters by
	 * @param separator grouping separator
	 * @return Hex string
	 * @throws IllegalArgumentException if obj is not a BigInteger, byte array, or a DERInteger, or groupSize
	 *             &lt; 0
	 */
	public static StringBuilder toHex(Object obj, int groupSize, String separator)
	{
		if (groupSize < 0)
		{
			throw new IllegalArgumentException("Group size must be >= 0");
		}
		BigInteger bigInt;
		if (obj instanceof BigInteger)
		{
			bigInt = (BigInteger) obj;
		}
		else if (obj instanceof byte[])
		{
			bigInt = new BigInteger(1, (byte[]) obj);
		}
		else if (obj instanceof DERInteger)
		{
			bigInt = ((DERInteger) obj).getValue();
		}
		else
		{
			throw new IllegalArgumentException("Don't know how to convert " + obj.getClass().getName() +
			    " to a hex string");
		}

		// Convert to hex

		StringBuilder sb = new StringBuilder(bigInt.toString(16).toUpperCase(Locale.ENGLISH));

		// Left-pad if asked and necessary

		if (groupSize != 0)
		{
			int len = groupSize - (sb.length() % groupSize);
			if (len != groupSize)
			{
				for (int i = 0; i < len; i++)
				{
					sb.insert(0, '0');
				}
			}
		}

		// Place separator at every groupSize characters

		if (sb.length() > groupSize && !separator.isEmpty())
		{
			for (int i = groupSize; i < sb.length(); i += groupSize + separator.length())
			{
				sb.insert(i, separator);
			}
		}

		return sb;
	}
}
