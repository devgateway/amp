package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.util.Output;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

/**
 * 
 * @author Alex Gartner
 *
 */
public class AmpActivityDocument extends ObjectReferringDocument implements Serializable, Versionable, Cloneable {
	private Long id;
	private AmpActivityVersion ampActivity;
	private String documentType;
	
	
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public AmpActivityVersion getAmpActivity() {
		return ampActivity;
	}
	public void setAmpActivity(AmpActivityVersion ampActivity) {
		this.ampActivity = ampActivity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	protected void detach() {
		ampActivity.getActivityDocuments().remove(this);
		this.ampActivity		= null;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivityDocument aux = (AmpActivityDocument) obj;
		String original = this.documentType != null ? this.documentType : "";
		String copy = aux.documentType != null ? aux.documentType : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		if (this.documentType != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "&nbsp;Document Type:&nbsp;" },
									new Object[] { this.documentType }));
		}

		return out;
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpActivityDocument aux = (AmpActivityDocument) clone();
		aux.id = null;
		aux.ampActivity = newActivity;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}