package org.dgfoundation.amp.algo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * various algorithmical utils
 * @author Dolghier Constantin
 *
 */
public class AlgoUtils {
	
	/**
	 * recursively get all ancestors (parents) OR descendants of a set by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static<K> Set<K> runWave(Collection<K> inIds, Waver<K> waver)
	{
		Set<K> result = new HashSet<K>();
		if (inIds == null)
			return result;
		Set<K> currentWave = new HashSet<K>();
		currentWave.addAll(inIds);
		while (currentWave.size() > 0)
		{
			result.addAll(currentWave);
			currentWave = waver.wave(currentWave);
			currentWave.removeAll(result); // in case there is a cycle somewhere in the DB, do not cycle forever
		}
		return result;
	}
	 
}
