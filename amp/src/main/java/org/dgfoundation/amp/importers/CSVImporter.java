/**
 * 
 */
package org.dgfoundation.amp.importers;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author mihai
 *
 */
public abstract class CSVImporter extends AmpImporter {

    protected String token;


    public CSVImporter(String importFileName, String[] columnNames,Properties extraProperties) {
        super(importFileName, columnNames);
        this.token=extraProperties.getProperty("token");
    }



    /* (non-Javadoc)
     * @see org.dgfoundation.amp.importers.AmpImporter#getFileType()
     */
    @Override
    protected String getFileType() {
        return "CSV";
    }



    /* (non-Javadoc)
     * @see org.dgfoundation.amp.importers.AmpImporter#parseNextObject()
     */
    @Override
    protected Map<String,String> parseNextLine() throws IOException {
        LineNumberReader lnr= (LineNumberReader) reader;
        String line=lnr.readLine();
        if(line==null) return null;
        int k=0;
        Map<String,String> ret=new HashMap<String,String>(); 
        String [] parts = tokenizeLine (line);
        for (String token:parts) {
            if(token.trim().equals("")) ret.put(columnNames[k], null); else ret.put(columnNames[k], token);
            k++;
        }
        return ret;
    }


    @Override
    protected void initializeReader(Reader source) {
        LineNumberReader lnr=new LineNumberReader(source);
        reader=lnr;
    }
    
    protected String [] tokenizeLine (String line) {
        return line.split(token);
        
    }


}
