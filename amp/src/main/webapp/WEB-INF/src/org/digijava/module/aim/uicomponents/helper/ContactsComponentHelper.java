package org.digijava.module.aim.uicomponents.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;

public class ContactsComponentHelper {
    
    public static <E> Collection<E> insertItemIntoCollection(Collection<E> col, E item, Comparator<E> comparator){
        if(col==null){
            col=new ArrayList<E>();         
        }else{
            Iterator<E> iterator=col.iterator();
            while(iterator.hasNext()){
                E element=iterator.next();
                if(comparator.compare(element, item) ==0){
//                  if(element instanceof AmpActivityContact){                      
//                      iterator.remove();
//                  }                   
                    iterator.remove();
                }
            }
        }
        //add item to collection
//      if(item instanceof AmpActivityContact){
//          col.add(item);
//      } 
        col.add(item);
        return col;
    }
    
    
    public static class AmpContactCompare implements Comparator<AmpContact> {
        public int compare(AmpContact cont1, AmpContact cont2) {
            if(cont1.getId()!=null&&cont2.getId()!=null){
                return cont1.getId().compareTo(cont2.getId());
            }
            else{
                 if(cont1.getId()==null&&cont2.getId()==null){
                       return cont1.getTemporaryId().compareTo(cont2.getTemporaryId());
                 }
                 else{
                     if(cont1.getId()==null){
                         return -1;
                     }
                     else{
                         return 1;
                     }
                 }
            }            
        }
    }
    
    public static class AmpActivityContactCompareByContact implements Comparator<AmpActivityContact>{
        @Override
        public int compare(AmpActivityContact cont1, AmpActivityContact cont2) {
            return new AmpContactCompare().compare(cont1.getContact(), cont2.getContact());
        }
    }
    
    public static class AmpOrganisationContactCompareByContact implements Comparator<AmpOrganisationContact>{
        @Override
        public int compare(AmpOrganisationContact cont1, AmpOrganisationContact cont2) {
            return new AmpContactCompare().compare(cont1.getContact(), cont2.getContact());
        }       
    }

}
