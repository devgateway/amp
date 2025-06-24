/**
 * 
 */
package org.dgfoundation.amp.menu.dynamic;

import org.dgfoundation.amp.menu.MenuItem;

/**
 * Interface for menu entries that need a dynamic adjustment of their structure and properties
 * @author Nadejda Mandrescu
 */
public interface DynamicMenu {
    void process(MenuItem menuItem);
}
