package cn.apiclub.tool.ssl.crypto;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Comparator for OID string values.
 */
public class OidComparator
    implements Comparator<String>
{
	@Override
	public int compare(String o1, String o2)
	{
		int longest = 0;

		String[] bits1 = o1.split("\\.");
		int[] lengths1 = new int[bits1.length];
		String[] bits2 = o2.split("\\.");
		int[] lengths2 = new int[bits2.length];

		for (int i = 0; i < bits1.length; i++)
		{
			lengths1[i] = bits1[i].length();
			longest = Math.max(longest, lengths1[i]);
		}
		for (int i = 0; i < bits2.length; i++)
		{
			lengths2[i] = bits2[i].length();
			longest = Math.max(longest, lengths2[i]);
		}

		for (int i = 0; i < bits1.length; i++)
		{
			if (lengths1[i] < longest)
			{
				bits1[i] = String.format("%" + (longest - lengths1[i]) + "s", bits1[i]);
			}
		}
		for (int i = 0; i < bits2.length; i++)
		{
			if (lengths2[i] < longest)
			{
				bits2[i] = String.format("%" + (longest - lengths2[i]) + "s", bits2[i]);
			}
		}

		return Arrays.toString(bits1).compareTo(Arrays.toString(bits2));
	}
}
