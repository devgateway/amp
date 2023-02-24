/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpUniqueCollectionValidator;

/**
 * @author mihai
 *
 */
public abstract class AmpUniqueCollectionValidatorField<T> extends
        AmpCollectionValidatorField<T, String> {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param collectionModel
     * @param fmName
     */
    public AmpUniqueCollectionValidatorField(String id,
            IModel<? extends Collection<T>> collectionModel, String fmName) {
        super(id, collectionModel, fmName,new AmpUniqueCollectionValidator());
        hiddenContainer.setType(String.class);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.model.IModel)
     */
    @Override
    public IModel<String> getHiddenContainerModel(
            final IModel<? extends Collection<T>> collectionModel) {
        Model<String> model=new Model<String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public String getObject() {
                Set<Object> res=new TreeSet<Object>();
                TreeSet<Object> s=new TreeSet<Object>();
                Iterator<T> iterator = collectionModel.getObject().iterator();
                while (iterator.hasNext()) {
                    T t = (T) iterator.next();
                    if(s.contains(getIdentifier(t))) res.add(getIdentifier(t)); 
                    s.add(getIdentifier(t));
                }
                if(res.size()>0) 
                        return res.toString();
                return "";
            }
        };
        return model;
    }
    
    public abstract Object getIdentifier(T t);

}
