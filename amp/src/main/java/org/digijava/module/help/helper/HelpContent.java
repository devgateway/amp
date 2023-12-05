package org.digijava.module.help.helper;

import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.sdm.dbentity.Sdm;

/**
 * Used for saving tree state
 * @author Dare
 *
 */
public class HelpContent {
    private Editor editor;
    private Sdm document;
    
    public Editor getEditor() {
        return editor;
    }
    public void setEditor(Editor editor) {
        this.editor = editor;
    }
    public Sdm getDocument() {
        return document;
    }
    public void setDocument(Sdm document) {
        this.document = document;
    }
    
}
