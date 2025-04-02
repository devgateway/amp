package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class DonorTreeNode {

    @ApiModelProperty(value = "the key of the donor", example = "1")
    private final Long key;


    @ApiModelProperty(value = "the title of the donors", example = "Ministry of Health")
    private final String title;

    @ApiModelProperty(example = "false")
    private final Boolean folder;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final Boolean selected;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private final List<DonorTreeNode> children;

    public DonorTreeNode(Long key, String title, Boolean folder, Boolean selected) {
        this(key, title, folder, selected, ImmutableList.of());
    }

    public DonorTreeNode(Long key, String title, Boolean folder, List<DonorTreeNode> children) {
        this(key, title, folder, null, children);
    }

    private DonorTreeNode(Long key, String title, Boolean folder, Boolean selected,
            List<DonorTreeNode> children) {
        this.key = key;
        this.title = title;
        this.folder = folder;
        this.selected = selected;
        this.children = children;
    }

    public Long getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getFolder() {
        return folder;
    }

    public Boolean getSelected() {
        return selected;
    }

    public List<DonorTreeNode> getChildren() {
        return children;
    }
}
