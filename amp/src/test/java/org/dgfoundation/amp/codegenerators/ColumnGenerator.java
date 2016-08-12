package org.dgfoundation.amp.codegenerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.apache.commons.lang.WordUtils;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * Class for generating NiColumns.
 * May be used to generate directly to file (.generateToFile()) or to a string (.generate()).
 
 * @author acartaleanu
 *
 */
public abstract class ColumnGenerator extends CodeGenerator {

	@SuppressWarnings("rawtypes")
	protected final Class clazz;

	/**
	 * 
	 * @param name the name of the column, like ColumnConstants.PRIMARY_SECTORS ("Primary Sectors")
	 * @param clazz The class defining cells contained (TextCell, PercentageTextCell...)
	 */
	public ColumnGenerator(String name, @SuppressWarnings("rawtypes") Class clazz) {
		super(name, clazz.getName());
		this.clazz = clazz;
	}

	
	protected String getFilePart1() {
		return 
			"import java.util.Arrays;\n" +
			"import java.util.List;\n" +
			"import java.util.Map;\n" +
			"import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;\n" + 
			"import org.dgfoundation.amp.nireports.schema.NiDimension;\n" + 
			"import org.dgfoundation.amp.testmodels.nicolumns.HardcodedCells;\n" + 
			"\n" +
			String.format("import org.dgfoundation.amp.nireports.%s;\n", clazz.getSimpleName()) +
			"\n" +
			"\n" +
			String.format("public class %s extends HardcodedCells<%s>{\n", getCanonicalNameWithCells(this.name), clazz.getSimpleName()) +
			"\n" +
			String.format("	public %s(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {\n", getCanonicalNameWithCells(this.name)) +
			"		super(activityNames, entityNames, degenerate(dim, key));\n" +
			"	}\n" +
			String.format("	public %s(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {\n", getCanonicalNameWithCells(this.name)) +
			"		super(activityNames, entityNames, lc);\n" +
			"	}\n" +
			"\n" +
			"	@Override\n" +
			String.format("	protected List<%s> populateCells() {\n", clazz.getSimpleName()) +
			"		return 	";
	}
			
	protected String getFilePart2() { 
		return "\n" +
			"	}\n" +
			"\n" +
			"}";
	}
		
	protected String getCanonicalNameWithCells(String name) {
		return WordUtils.capitalize(name).replaceAll(" ", "").replaceAll("-", "") + "Cells";
	}

}
