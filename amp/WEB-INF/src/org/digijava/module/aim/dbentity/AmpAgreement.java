package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;

@TranslatableClass(displayName = "Agreement")
public class AmpAgreement implements Serializable, Versionable {
    private static final long serialVersionUID = 1L;

    @Interchangeable(fieldTitle = "Agreement ID")
    private Long id;

    @Interchangeable(fieldTitle = "Code",
            fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Code",
            importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private String code;

    @Interchangeable(fieldTitle = "Title",
            fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Title",
            importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @TranslatableField
    private String title;

    @Interchangeable(fieldTitle = "Effective Date", fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Effective Date", importable = true)
    private Date effectiveDate;

    @Interchangeable(fieldTitle = "Signature Date", fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Signature Date", importable = true)
    private Date signatureDate;

    @Interchangeable(fieldTitle = "Close Date", fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Close Date", importable = true)
    private Date closeDate;

    @Interchangeable(fieldTitle = "Parlimentary Approval Date", fmPath = "/Activity Form/Funding/Funding Group/Funding Item/Funding Classification/Agreement/Parlimentary Approval Date", importable = true)
    private Date parlimentaryApprovalDate;

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
        if (!(obj instanceof AmpAgreement))
            return false;
        AmpAgreement oth = (AmpAgreement) obj;
        if (this.id != null && oth.id != null)
            return this.id.longValue() == oth.id.longValue();
        return this.title.equals(oth.title);
    }

    @Override
    public Object getValue() {
        StringBuffer ret = new StringBuffer();
        ret.append("-Title:" + (this.title != null ? this.title : ""));
        ret.append("-Code:" + (this.code != null ? this.code : ""));
        ret.append("-Signature date:" + (this.signatureDate != null ? this.signatureDate : ""));
        ret.append("-Effective date:" + (this.effectiveDate != null ? this.effectiveDate : ""));
        ret.append("-Close date:" + (this.closeDate != null ? this.closeDate : ""));
        ret.append("-Parlimentary Approval date:"
                + (this.parlimentaryApprovalDate != null ? this.parlimentaryApprovalDate : ""));
        return ret.toString();
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] { "Title" }, new String[] { this.title }));
        out.getOutputs().add(new Output(null, new String[] { "Code" }, new String[] { this.code }));
        out.getOutputs().add(new Output(null, new String[] { "Signature Date" },
                new String[] { PersistenceManager.getString(this.signatureDate) }));
        out.getOutputs().add(new Output(null, new String[] { "Effective Date" },
                new String[] { PersistenceManager.getString(this.effectiveDate) }));
        out.getOutputs().add(new Output(null, new String[] { "Close Date" },
                new String[] { PersistenceManager.getString(this.closeDate) }));
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        return this;
    }

    public Date getParlimentaryApprovalDate() {
        return parlimentaryApprovalDate;
    }

    public void setParlimentaryApprovalDate(Date parlimentaryApprovalDate) {
        this.parlimentaryApprovalDate = parlimentaryApprovalDate;
    }
}
