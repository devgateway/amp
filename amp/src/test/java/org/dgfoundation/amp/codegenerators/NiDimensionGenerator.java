package org.dgfoundation.amp.codegenerators;


import java.util.List;



public class NiDimensionGenerator extends CodeGenerator {

	private final List<TreeNode> roots;
	
	public NiDimensionGenerator(List<TreeNode> roots) {
		this.roots = roots;
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
		StringBuilder strb = new StringBuilder();
		strb.append("Arrays.asList(");
		strb.append(generateCode(this.roots, 0));
		strb.append(");");
		return strb.toString();
	}
	
}
