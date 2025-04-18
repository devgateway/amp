package org.digijava.kernel.lucene;

import org.apache.lucene.document.Document;

public class AmpLuceneDoc {
    
    private Document document;
    private float score;
    
    public AmpLuceneDoc(Document document, float score) {
        super();
        this.document = document;
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

}
