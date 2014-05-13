package org.dgfoundation.amp.ar.moldovamigration;

public class HardCodedStuff
{
	public static String[] checkHardcodedCases(String text, char separator)
	{
		if (text.startsWith("Lucrarea include o caracterizare de ansamblu") || text.contains("organic persistenți (POP)") || text.contains("23.11.2011"))
		{
			int pos = text.indexOf("The paper includes") - 1;
			return new String[] {text.substring(0, pos).trim(), text.substring(pos + 1).trim()};
		}
		
		if (text.contains("Joint United Nations Programme on HIV/AIDS (UNAIDS)"))
			return new String[] {text, text};
		return null;
	}
}
