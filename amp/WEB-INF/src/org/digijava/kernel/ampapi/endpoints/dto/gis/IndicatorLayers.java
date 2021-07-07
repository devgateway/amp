package org.digijava.kernel.ampapi.endpoints.dto.gis;

public class IndicatorLayers {
    private Long id;
    private String title;
    private String type;
    private String link;
    private String layer;
    private String legendNotes;

    public IndicatorLayers() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLegendNotes(String legendNotes) {
        this.legendNotes = legendNotes;
    }

    public String getLegendNotes() {
        return legendNotes;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }
}
