package org.digijava.module.demo.form;

import java.util.Collection;
import org.apache.struts.action.ActionForm;

public class DemoItemList extends ActionForm {

   Collection items;
   long activeItemId;

   public Collection getItems() {
      return items;
   }

   public void setItems(Collection items) {
       this.items = items;
   }

   public long getActiveItemId() {
      return activeItemId;
   }

   public void setActiveItemId(long activeItemId) {
      this.activeItemId = activeItemId;
   }

}