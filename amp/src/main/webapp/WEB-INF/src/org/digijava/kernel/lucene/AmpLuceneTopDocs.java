package org.digijava.kernel.lucene;

import java.util.LinkedList;

public class AmpLuceneTopDocs {
    
    private LinkedList<AmpLuceneDoc> documents = new LinkedList<>();
    
    public int size() {
        return documents.size();
    }

    public AmpLuceneDoc getDocument(int id) {
        return documents.get(id);
    }
    
    public void addDocument(AmpLuceneDoc doc) {
        documents.add(doc);
    }
}
