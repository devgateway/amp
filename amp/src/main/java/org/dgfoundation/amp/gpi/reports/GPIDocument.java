package org.dgfoundation.amp.gpi.reports;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class GPIDocument {

    private String title;
    private String question;
    private String description;
    private String type;
    private String url;
    
    public GPIDocument() {}

    public GPIDocument(String title, String question, String description, String type, String url) {
        super();
        this.title = title;
        this.question = question;
        this.description = description;
        this.type = type;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
