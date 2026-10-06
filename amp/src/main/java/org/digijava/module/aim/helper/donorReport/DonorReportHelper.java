package org.digijava.module.aim.helper.donorReport;

import java.lang.reflect.Method;
import java.util.List;


public class DonorReportHelper {
    private Method method;
    private PropertyType type;
    private List<String> subHeaders;

    public List<String> getSubHeaders() {
        return subHeaders;
    }

    public void setSubHeaders(List<String> subHeaders) {
        this.subHeaders = subHeaders;
    }

    public DonorReportHelper(Method method, PropertyType type, List<String> subHeaders) {
        this.method = method;
        this.type = type;
        this.subHeaders = subHeaders;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }
}
