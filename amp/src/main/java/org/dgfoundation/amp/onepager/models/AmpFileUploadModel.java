package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AmpFileUploadModel implements IModel<List<FileUpload>> {
    private static final long serialVersionUID = 1L;
    private IModel<FileUpload> model;

    
    public AmpFileUploadModel(IModel<FileUpload> model) {
        this.model=model;
    }
    
    @Override
    public void detach() {
    }

    @Override
    public List<FileUpload> getObject() {
        ArrayList<FileUpload> al = new ArrayList<FileUpload>();
        FileUpload item = model.getObject();
        if (item != null)
            al.add(item);
        return al;
    }

    @Override
    public void setObject(List<FileUpload> list) {
        if (list == null)
            model.setObject(null);
        else{
            Iterator<FileUpload> it = list.iterator();
            if (it.hasNext()){
                FileUpload tmp = it.next();
                model.setObject(tmp);
            }
            else
                model.setObject(null);
        }
    }
}
