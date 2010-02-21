package org.digijava.kernel.lucene;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hit;

/**
 * Lucene Module interface.
 * this was named module to make easy understand that 
 * each module in AMP (e.g. Help, translations) 
 * should have one implementations of this interface.
 * Otherwise it represents one separate Index in lucene.
 * @author Irakli Kobiashvili
 *
 */
public interface LucModule<E> extends Serializable {
	
	/**
	 * Returns value of serialVersionUID static variable of implementing class.
	 * NOTE This value should be changed each time the implementation class is changed!!! 
	 * @return
	 */
	long getSerialVersionUID();
	
	/**
	 * Returns unique name of the module.
	 * e.g. Translation, Help, Activity
	 * @return name of the module.
	 */
	String getName();
	
	/**
	 * Returns sub directory name where indexes will be stored.
	 * usually same as name above but in lower case to not 
	 * have problems with different FS.
	 * @return name of the sub-folder for indexes.
	 */
	String getDirSuffix();
	
	/**
	 * Each module should return its own type of analyzer object.
	 * This is used to analyze texts for indexing.
	 * Usually this is instance of {@link StandardAnalyzer} 
	 * @return
	 */
	Analyzer getAnalyzer();
	
	/**
	 * Returns all items for the module that should be indexed when rebuilding it.
	 * Examples can be all organizations, or all contacts.
	 * @return list of items of type E
	 */
	List<E> getItemsToIndex();
	
	/**
	 * If for some reason module needs to rebuild indexes then this method will return true. 
	 * @return
	 */
	boolean needIndexRebuild();
	
	/**
	 * Converts one item to lucene {@link Document}.
	 * So each module "knows" how to convert their items to lucene document.
	 * @param item
	 * @return
	 */
	Document convertToDocument(E item);
	
	/**
	 * Get term for ID field to search one single doc.
	 * Module should create term by using field name as first parameter in term constructor 
	 * and value of that field retrieved from item as second parameters 
	 * TODO need refactoring: probably it is better to get ID value from module and build Term object in LuceneWorker? 
	 * @return 
	 */
	Term getIdFieldTerm(E item);
	
	/**
	 * Module provides main search field name for index search.
	 * this field should be created in {@link #convertToDocument(Object)} method and added to doc method produces.
	 * @return search field name.
	 */
	String getSearchFieldName();
	
	E hitToItem(Hit hit) throws IOException;
}
