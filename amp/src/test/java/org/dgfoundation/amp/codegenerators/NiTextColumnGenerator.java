package org.dgfoundation.amp.codegenerators;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Code generator for NiTextColumn cells. 
 * @author acartaleanu
 *
 */
public class NiTextColumnGenerator extends ColumnGenerator {

    final List<Entry> entries;
    
    class Entry {
        public final String aavname;
        public final String text;
        public final long id;
        public final Map<NiDimensionUsage, Coordinate> coos;
        
        public Entry(String aavname, String name, long id, Map<NiDimensionUsage, Coordinate> coos) {
            this.aavname = anon(aavname);
            this.text = cleanupOrAnon(name);
            this.id = id;
            this.coos = coos;
        }
        
        @Override
        public String toString() {
            return String.format("%s: %s (%d)", aavname, name, id);
        }
    }
    
    public String cleanupOrAnon(String str) {
        return name.equals(ColumnConstants.PROJECT_TITLE) ? anon(str) : cleanup(str);
    }
    
    public NiTextColumnGenerator(String columnName) {
        super(columnName, TextCell.class);
        this.entries = populateList();
    }
        
    private List<Entry> populateList() {
        final List<Entry> entries = new ArrayList<>();
        runInEngineContext( 
                new ArrayList<>(getActivityNames().values()),
                eng -> {
                    Map<Long, String> activityNames = getActivityNames();
                    @SuppressWarnings("unchecked")
                    List<TextCell> cells = (List<TextCell>) eng.schema.getColumns().get(name).fetch(eng);
                    for (TextCell cell : cells) {
                        entries.add(new Entry(activityNames.get(cell.activityId), cell.text, cell.entityId, cell.coordinates));
                    }
                    entries.sort((Entry e1, Entry e2) -> {
                        if (e1.aavname.equalsIgnoreCase(e2.aavname))
                            return e1.text.compareToIgnoreCase(e2.text);
                        return e1.aavname.compareToIgnoreCase(e2.aavname);
                    });
                    
                });
        return entries;
    }
    
    @Override
    public String generate() {
        StringJoiner cellsCode = new StringJoiner(",");
        entries.forEach(ent -> cellsCode.add(String.format("\n\t\t\tcell(%s, %s, %s,\n\t\t\t\tcoos(%s))",
                escape(ent.aavname), escape(ent.text), ent.id, coosCode(ent))));
        return "Arrays.asList(" + cellsCode.toString() + ");";
    }

    private String coosCode(Entry ent) {
        StringJoiner coosCode = new StringJoiner(",");
        ent.coos.forEach((k, v) -> coosCode.add(String.format("\n\t\t\t\t\tentry(%s, %s, %d, %d)",
                escape(k.dimension.name), escape(k.instanceName), v.level, v.id)));
        return coosCode.toString();
    }
}
