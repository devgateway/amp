package org.dgfoundation.amp.codegenerators;


import java.util.List;


public class NiDimensionGenerator extends CodeGenerator {

    private final List<TreeNode> roots;
    protected final String className;
    protected final String dimensionName;
    protected final int depth;
    
    public NiDimensionGenerator(List<TreeNode> roots, String className, String dimensionName, int depth) {
        super(className, "");
        this.roots = roots;
        this.className = className;
        this.dimensionName = dimensionName;
        this.depth = depth;
    }

    private String generateCode(List<TreeNode> nodes, int depth) {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            TreeNode node = nodes.get(i);
            bld.append("\n");
            for (int j = 0; j < depth + 2; j++)
                bld.append("\t");
            bld.append(String.format("element(%d, %s", node.id, escape(node.name)));
            if (node.children.size() > 0){
                bld.append(", ");
                bld.append(generateCode(node.children, depth + 1));
            } 
            bld.append(" )");
            if (i < nodes.size() - 1) // don't need a ',' if it's the last element in a sequence of children
                bld.append(", ");
        }
        return bld.toString();
    }
    
    @Override
    public String generate() {
        StringBuilder bld = new StringBuilder();

        bld.append("import java.util.Arrays;\n");
        bld.append("import java.util.List;\n");
        bld.append("import org.dgfoundation.amp.testmodels.dimensions.HardcodedNiDimension;\n");
        bld.append("import org.dgfoundation.amp.testmodels.dimensions.HNDNode;\n");
        bld.append("import static org.dgfoundation.amp.testmodels.dimensions.HNDNode.element;\n\n");
        bld.append(String.format("public class %s extends HardcodedNiDimension {\n\n", className));
        bld.append(String.format("\tpublic %s(String name, int depth) {\n", className));
        bld.append(String.format("\t\tsuper(name, depth);\n"));
        bld.append(String.format("\t}\n\n"));
        bld.append(String.format("\tpublic final static %s instance = new %s(\"%s\", %d);\n\n", className, className, dimensionName, depth));
        bld.append(String.format("\t@Override\n"));
        bld.append(String.format("\tprotected List<HNDNode> buildHardcodedElements() {\n"));
        bld.append(String.format("\t\treturn "));
                        
        bld.append("Arrays.asList(");
        bld.append(generateCode(this.roots, 0));
        bld.append(");");
        
        bld.append("\n\t}\n");
        bld.append("}\n");

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
        return name;
    }
    
}
