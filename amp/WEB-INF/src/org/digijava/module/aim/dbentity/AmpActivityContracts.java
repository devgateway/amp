package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class AmpActivityContracts implements Serializable, Versionable, Cloneable, Comparable<AmpActivityContracts> {
    private Long ampContractId;
    private String contractDescription;
    private AmpActivityVersion activity;
    private Date contractDate;
    private Double contractAmount;
    private static final Integer HASH_CODE_CONSTANT = 31;

    @Override
    public boolean equals(Object o) {
        return equalsForVersioning(o);
    }

    @Override
    public boolean equalsForVersioning(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AmpActivityContracts that = (AmpActivityContracts) o;

        if (contractDescription != null ? !contractDescription.equals(that.contractDescription) : that
                .contractDescription != null) {
            return false;
        }
        if (contractDate != null ? !contractDate.equals(that.contractDate) : that.contractDate != null) {
            return false;
        }
        return contractAmount != null ? contractAmount.equals(that.contractAmount) : that.contractAmount == null;
    }

    @Override
    public int hashCode() {
        int result = contractDescription != null ? contractDescription.hashCode() : 0;
        result = HASH_CODE_CONSTANT * result + (contractDate != null ? contractDate.hashCode() : 0);
        result = HASH_CODE_CONSTANT * result + (contractAmount != null ? contractAmount.hashCode() : 0);
        return result;
    }

    @Override
    public Object getValue() {
        String value = "";
        if (contractDescription != null) {
            value += contractDescription;
        }
        if (contractDate != null) {
            value += contractDate;
        }
        if (contractAmount != null) {
            value += contractAmount;
        }
        return value;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[]{"Contract description"}, new Object[]{
                        this.contractDescription != null ? this.contractDescription
                                : ""}));
        if (this.contractDate != null) {
            out.getOutputs().add(new Output(null, new String[]{"Contract Date"}, new Object[]{this.contractDate}));
        }
        if (this.contractAmount != null) {
            out.getOutputs().add(new Output(null, new String[]{"Contract Amount"}, new Object[]{this.contractAmount}));
        }
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        AmpActivityContracts aux = (AmpActivityContracts) clone();
        aux.activity = newActivity;
        aux.ampContractId = null;
        return aux;
    }

    public Long getAmpContractId() {
        return ampContractId;
    }

    public void setAmpContractId(Long ampContractId) {
        this.ampContractId = ampContractId;
    }

    public String getContractDescription() {
        return contractDescription;
    }

    public void setContractDescription(String contractDescrition) {
        this.contractDescription = contractDescrition;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    @Override
    public int compareTo(@NotNull AmpActivityContracts o) {
        return AmpActivityContractsComparator.staticCompare(this, o);
    }

    public static class AmpActivityContractsComparator implements Comparator<AmpActivityContracts> {
        @Override
        public int compare(AmpActivityContracts o1, AmpActivityContracts o2) {
            return staticCompare(o1, o2);
        }

        public static int staticCompare(AmpActivityContracts o1, AmpActivityContracts o2) {
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }
            if (o2.getContractDescription() == null) {
                return -1;
            }
            if (o1.getContractDescription() == null) {
                return 1;
            }
            int ret = o1.getContractDescription().compareTo(o2.getContractDescription());
            return ret;
        }
    }
}
