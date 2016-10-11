package org.digijava.kernel.lucene.impl.org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hit;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;

/**
 * Organization module for lucene.
 * NOTE: This is reference implementation, not used anywhere yet. Please move to correct package if you decide to use and change this.
 * @author Irakli Kobiashvili
 *
 */
public class LucOrganisationModule implements LucModule<AmpOrganisation> {
	/**
	 * PLEASE INCREMENT VALUE ECH TIME CLASS IS CHANGED. 
	 */
	private static final long serialVersionUID = 4L;
	
	private static final String NAME = "Organisations";
	private static final String FIELD_ID = "orgId";
	private static final String FIELD_NAME = "orgName";

	@Override
	public Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}

	@Override
	public String getSuffix() {
		return null;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<AmpOrganisation> getItemsToIndex() {
		return new ArrayList<>(DbUtil.getAll(AmpOrganisation.class));
	}

	@Override
	public boolean needIndexRebuild() {
		return false;
	}

	@Override
	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public Document convertToDocument(AmpOrganisation item) {
		Document doc = new Document();
		Field orgId = new Field(FIELD_ID,item.getAmpOrgId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field orgName = new Field(FIELD_NAME,item.getName(), Field.Store.YES, Field.Index.TOKENIZED);
		doc.add(orgId);
		doc.add(orgName);
		return doc;
	}

	@Override
	public Term getIdFieldTerm(AmpOrganisation item) {
		Term term = new Term(FIELD_ID, item.getAmpOrgId().toString());
		return term;
	}

	@Override
	public String[] getSearchFieldNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmpOrganisation hitToItem(Hit hit) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<AmpOrganisation> getItemClass() {
		return AmpOrganisation.class;
	}

}
