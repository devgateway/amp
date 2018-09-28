package org.digijava.kernel.lucene;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.impl.org.LucOrganisationModule;
import org.digijava.module.help.lucene.LucHelpModule;

/**
 * Lucene Module interface.
 * Each time we want to index something we have to implement this interface
 * and register implementation with LuceneWorker. 
 * Each implementation represents one separate Index in lucene.
 * they should be stored in separate folders. Because of this each module returns
 * its own name which is used as folder names.
 * There are two types of beans that can be indexed:
 * First type is simple one, only one index for them is required and all beans are stored there.
 * Such module implementations may return null as suffix.
 * Examples of such modules could be Organizations - {@link LucOrganisationModule}, Indicators and others. 
 * When searching such indexes suffix parameters can be null or methods without this parameters can be used. 
 * Second type of indexes require same types of beans be split in different indexes by some feature.
 * For example translations should be split in separate indexes by language code.
 * Help module {@link LucHelpModule} requires language code + module instance because there are two types of
 * help content, one for user and other for admin and user should not be able to search admin help.
 * For this reason modules of second type should return suffix values other then null to let LuceneWorker distinguish them.
 * For example translatioins return language codes, like en, fr, sp so folders will look like translation_fr.
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
     * Returns name of the module.
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
    String getSuffix();
    
    Class<E> getItemClass();
    
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
    List<E> getItemsToIndex() throws DgException;
    
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
//  String getSearchFieldName();
    
    /**
     * Module provide array of field names to be searched for.
     * @return
     */
    String[] getSearchFieldNames();
    
    /**
     * Converts one hit object to one bean of type E.
     * This method knows how to read field values from hit and build from it object of E class.
     * @param scoreDoc lucene search result - one scoreDoc.
     * @return bean of E
     * @throws IOException
     */
    E luceneDocToItem(AmpLuceneDoc luceneDoc) throws IOException;
}
