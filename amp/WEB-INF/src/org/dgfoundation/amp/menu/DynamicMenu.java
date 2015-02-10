/**
 * 
 */
package org.dgfoundation.amp.menu;

/**
 * Interface for menu entries that need a dynamic adjustment of their structure and properties
 * @author Nadejda Mandrescu
 */
public interface DynamicMenu {
	void process(MenuItem menuItem);
}
