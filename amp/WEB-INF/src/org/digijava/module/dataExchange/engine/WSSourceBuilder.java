/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.io.InputStream;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

/**
 * @author dan
 *
 */
public class WSSourceBuilder  extends SourceBuilder {
    
    public WSSourceBuilder(DESourceSetting ampSourceSetting) {
        super(ampSourceSetting);
        // TODO Auto-generated constructor stub
    }

    public WSSourceBuilder(DESourceSetting ampSourceSetting, String inputStream) {
        super(ampSourceSetting, inputStream);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * This will initialize the private property inputStream.
     * This must be implemented by all implementers (URLSourceBuilder and WSSourceBuilder).
     * For FileSourceBuilder the InputStream will be given as a parameter in the constructor.
     */
    @Override
    public void process() {
        // TODO Auto-generated method stub
        
    }

}
