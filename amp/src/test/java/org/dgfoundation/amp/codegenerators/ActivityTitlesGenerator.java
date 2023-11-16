package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ActivityTitlesGenerator extends CodeGenerator {

    public ActivityTitlesGenerator() {
        super("HardcodedActivities", "dummy");
        activities = getActivityNames();
        entries = populateEntries();
    }

    final private Map<Long, String> activities;
    private List<Entry> entries;
    
    List<Entry> populateEntries() {
        List<Entry> entries = new ArrayList<Entry>();
        for (Map.Entry<Long, String> entry : activities.entrySet()) {
            entries.add(new Entry(entry.getKey(), entry.getValue()));
        }
        entries.sort(null);
        return Collections.unmodifiableList(entries);
    }
    
    
    
    class Entry implements Comparable<Entry>{
        public final long id;
        public final String text;
        public Entry(long id, String text) {
            this.id = id;
            this.text = anon(text);
        }
        @Override
        public int compareTo(Entry o) {
            return this.text.toLowerCase().compareTo(o.text.toLowerCase());
        }
    }
    
    private String generateCode() {
        StringBuilder strb = new StringBuilder();
        strb.append("\n");
        for (int i = 0; i < entries.size(); i++) {
            Entry ent = entries.get(i);
            strb.append("\t\tactivity(");
            strb.append(String.format("%s, %d", escape(ent.text), ent.id));
            strb.append(");\n");
        }
        return strb.toString();
    }
    
    
    @Override
    public String generate() {
        StringBuilder strb = new StringBuilder();
        strb.append(generateCode());
        return strb.toString();
    }


    @Override
    protected String getFilePart1() {
        return 
        "import java.util.ArrayList;\n" +
        "import java.util.Collections;\n" +
        "import java.util.HashMap;\n" +
        "import java.util.List;\n" +
        "import java.util.Map;\n" +
        "\n" +
        "public class HardcodedActivities {\n" +
        "\n" +
        "   private final Map<Long, String> actNames;\n" +
        "   private final Map<String, Long> actIds;\n" +
        "   \n" +
        "   private Map<Long, String> hiddenActNames = new HashMap<Long, String>();\n" +
        "   private Map<String, Long> hiddenActIds = new HashMap<String, Long>();\n" +
        "   \n" +
        "\n" +
        "   public HardcodedActivities() {\n" +
        "       populateMaps();\n" +
        "       actNames = Collections.unmodifiableMap(hiddenActNames);\n" +
        "       actIds = Collections.unmodifiableMap(hiddenActIds);\n" +
        "   }\n" +
        "   \n" +
        "   private void activity(String title, long id) {\n" +
        "       hiddenActNames.put(id, title);\n" +
        "       hiddenActIds.put(title, id);\n" +
        "   }\n" +
        "   \n" + 
        "   private void populateMaps() {\n";
    }

    @Override
    protected String getFilePart2() {
        return
        "}\n" + 
        "public Map<Long, String> getActNamesMap() {\n" + 
        "       return actNames;\n" +
        "   }\n" +
        "\n" + 
        "public List<String> getActNamesList() {\n" +
        "   return new ArrayList<String>(actNames.values());\n" +
        "}\n" +
        "\n" +
        "\n" +
        "public Map<String, Long> getActIdsMap() {\n" +
        "   return actIds;\n" +
        "}\n" + 
        "}\n";
    }


    @Override
    protected String getCanonicalNameWithCells(String name) {
        return "HardcodedActivities";
    }

}
