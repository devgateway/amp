package org.digijava.module.parisindicator.util;

import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class PIUtils {

	public final static boolean containSectors(Collection<AmpSector> sectors1, Collection<AmpSector> sectors2) {
		boolean ret = false;
		Iterator<AmpSector> iter1 = sectors1.iterator();
		Iterator<AmpSector> iter2 = sectors2.iterator();
		while (iter1.hasNext()) {
			AmpSector aux1 = iter1.next();
			while (iter2.hasNext()) {
				AmpSector aux2 = iter2.next();
				if (aux1.getAmpSectorId().equals(aux2.getAmpSectorId())) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	public final static boolean containStatus(Collection<AmpCategoryValue> statuses, AmpCategoryValue status) {
		boolean ret = false;
		Iterator<AmpCategoryValue> iterStatus = statuses.iterator();
		while (iterStatus.hasNext()) {
			AmpCategoryValue aux = iterStatus.next();
			if (aux.getId().equals(status.getId())) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public final static boolean containFinancingInstrument(AmpCategoryValue financing1,
			Collection<AmpCategoryValue> financing2) {
		boolean ret = false;
		Iterator<AmpCategoryValue> iter2 = financing2.iterator();
		while (iter2.hasNext()) {
			AmpCategoryValue aux1 = iter2.next();
			if (aux1.getId().equals(financing1.getId())) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
