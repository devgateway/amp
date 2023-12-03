package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class DonorTreeRoot {

    private List<DonorTreeNode> children;

    public DonorTreeRoot(List<DonorTreeNode> children) {
        this.children = children;
    }

    public String getTitle() {
        return "Donors";
    }

    public List<DonorTreeNode> getChildren() {
        return children;
    }
}
