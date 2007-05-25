package org.digijava.module.demo.data;

import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import org.digijava.module.demo.form.DemoItem;

/**
 *
 * Dummy data source class.
 * Each content item in Digi has two mandatory attributes
 * itemId (primary key) and instanceId indicating which
 * instance of the module the content belongs to
 * (kindof filter). And Each module has its own data
 * source class (hence DB table)so it's also obvious which
 * module the content belongs to.
 *
 */
public class DataSource {

    private static Collection itemsCollection;

    static {
      itemsCollection = new ArrayList();
      int i = 0;
      for (; i < 15; i++) {
        DemoItem item = new DemoItem();
        switch ( i%3 ) {
          case 0:  item.setInstanceId("default");
                   break;
          case 1:  item.setInstanceId("foo");
                   break;
          case 2:  item.setInstanceId("yadda");
        }
        switch ( i%2 ) {
          case 0:  item.setLanguage("en");
                   break;
          case 1:  item.setLanguage("fr");
        }

        item.setItemId( new Long ( (long) i) );
        item.setTitle(item.getInstanceId() + " item title " + i);
        item.setContent(item.getInstanceId() + " item content " + i);
        item.setReleaseDate( new Date() );
        itemsCollection.add(item);
      }

    };

    public static Collection getItems ( String instanceId ) {
      ArrayList result = new ArrayList();
      Iterator iter = itemsCollection.iterator();
      while (iter.hasNext()) {
        DemoItem item = (DemoItem) iter.next();
        if (instanceId.equals(item.getInstanceId())) {
          result.add(item);
        }
      }
      return result;
    }

    public static DemoItem getItem(long itemId) {
      DemoItem result = null;
      Iterator iter = itemsCollection.iterator();
      while (iter.hasNext()) {
        DemoItem item = (DemoItem) iter.next();
        if ( itemId == item.getItemId().longValue() ) {
          result = item;
          break;
        }
      }
      return result;
    }

}