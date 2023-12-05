package org.dgfoundation.amp.nireports.testcases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IHardcodedNames {
    
    Map<String, Map<String, Long>> params = new HashMap<String, Map<String, Long>>();
    
    protected abstract void populateMaps();
    
    /**
     * String -> Long: category names are keys to their ids
     */
    public Map<String, Map<String, Long>> getParams() {
        if (params.isEmpty())
            populateMaps();
        return params;
    }
    
    protected void category(String name, List<Entry> list) {
        Map<String, Long> categ = new HashMap<String, Long>();
        for (Entry entry : list) {
            categ.put(entry.text, entry.id);
        }
        params.put(name, categ);
    }
    
    protected Entry param(String name, long id) {
        return new Entry(id, name);
    }
    
    protected static class Entry {
        final long id;
        final String text;
        public Entry(long id, String text) {
            this.id = id;
            this.text = text;
        }
    }
}
