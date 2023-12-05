package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generates code for HardcodedFundingNames.  
 * 
 * @author acartaleanu
 *
 */
public class FundingTypesGenerator extends CodeGenerator {

    Map<String, Map<String, Long>> params;
    
    public FundingTypesGenerator(Map<String, Map<String, Long>> params) {
        super("HardcodedFundingNames", "hardcodedFundingNames");
        this.params = params;
    }
    
    @Override
    public String generate() {
        StringBuilder bld = new StringBuilder();
        for (Map.Entry<String, Map<String, Long>> entry : params.entrySet()) {
            bld.append(String.format("\t\tcategory(%s, ", escape(entry.getKey())));
            bld.append("Arrays.asList(");

            List<Map.Entry<String, Long>> list =  new ArrayList<Map.Entry<String, Long>>(entry.getValue().entrySet());
            for (int i = 0; i < list.size(); i++) {
                bld.append("\n\t\t\tparam(");
                bld.append(String.format("%s, %d", escape(list.get(i).getKey()), list.get(i).getValue()));
                bld.append(")");
                if (i < list.size() - 1)
                    bld.append(",");
            }
            bld.append("));\n");
        }
        return bld.toString();
    }

    @Override
    protected String getFilePart1() {
        return "";
    }

    @Override
    protected String getFilePart2() {
        return "";
    }

    @Override
    protected String getCanonicalNameWithCells(String name) {
        return "HardcodedFundingNames";
    }

}
