package org.dgfoundation.amp.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.util.Identifiable;


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
	
	/**
	 * collects all the ids in a set
	 * @param set
	 * @param input
	 * @return
	 */
	public static Set<Long> collectIds(Set<Long> set, Collection<? extends Identifiable> input)
	{
		for(Identifiable elem:input)
			set.add((Long) elem.getIdentifier());
		return set;
	}
	
	public static<K extends Identifiable> List<K> sortByIds(Collection<K> objs){
		List<K> res = new ArrayList<>(objs);
		Collections.sort(res, new Comparator<Identifiable>(){
			@Override public int compare(Identifiable arg0, Identifiable arg1) {
				Long id0 = (Long) arg0.getIdentifier();
				Long id1 = (Long) arg1.getIdentifier();
				return id0.compareTo(id1);
			}
		});
		return res;
	}
	
	public static Long getIdFrom(Identifiable id) {
		return id == null ? null : (Long)id.getIdentifier();
	}
	
	public static Long getLongFrom(String z) {
		if (z == null) return null;
		try {
			return Long.parseLong(z);
		} catch (Exception e) {
			return null;
		}
	}

	 
}
