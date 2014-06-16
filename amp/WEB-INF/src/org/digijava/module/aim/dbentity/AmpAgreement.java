package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;
import org.springframework.transaction.annotation.Transactional;

@TranslatableClass (displayName = "Agreement")
public class AmpAgreement implements Serializable, Versionable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	@TranslatableField
	private String title;
	private Date effectiveDate;
	private Date signatureDate;
	private Date closeDate;
	
	public AmpAgreement() {
		super();
	}
	
	public AmpAgreement(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getSignatureDate() {
		return signatureDate;
	}
	public void setSignatureDate(Date signatureDate) {
		this.signatureDate = signatureDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		if (!(obj instanceof AmpAgreement)) return false;
		AmpAgreement oth = (AmpAgreement) obj;
		if (this.id != null && oth.id != null)
			return this.id.longValue() == oth.id.longValue();
		return this.title.equals(oth.title);
	}

	@Override
	public Object getValue(){
		StringBuffer ret = new StringBuffer();
		ret.append("-Title:" + (this.title != null ? this.title : ""));
		ret.append("-Code:" + (this.code != null ? this.code : ""));
		ret.append("-Signature date:" + (this.signatureDate != null ? this.signatureDate : ""));
		ret.append("-Effective date:" + (this.effectiveDate != null ? this.effectiveDate : ""));
		ret.append("-Close date:" + (this.closeDate != null ? this.closeDate : ""));
		return ret.toString();
	}

	@Override
	public Output getOutput(){
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[]{"Title"}, new String[] {this.title}));
		out.getOutputs().add(new Output(null, new String[]{"Code"}, new String[] {this.code}));
		out.getOutputs().add(new Output(null, new String[]{"Signature Date"}, new String[] {PersistenceManager.getString(this.signatureDate)}));
		out.getOutputs().add(new Output(null, new String[]{"Effective Date"}, new String[] {PersistenceManager.getString(this.effectiveDate)}));
		out.getOutputs().add(new Output(null, new String[]{"Close Date"}, new String[] {PersistenceManager.getString(this.closeDate)}));
		return out;
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws Exception{
		return this;
	}
}
