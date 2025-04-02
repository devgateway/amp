/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.user.User;
import org.digijava.module.um.exception.UMException;

/**
 * 
 * @author dan
 */
public class AmpPMUserModel implements IModel<Set<User>> {

    private IModel<Set<User>> model;

    public AmpPMUserModel(IModel<Set<User>> model) {
        this.model=model;
    }

    public AmpPMUserModel() {
        super();
    }
    
    @Override
    public void detach() {
        if(model !=null ) model.detach();
    }

    @Override
    public Set<User> getObject() {
        Set<User> s = new HashSet<User>();
        List<User> users = new ArrayList<User>();
        try {
            users = org.digijava.module.um.util.DbUtil.getList(User.class.getName(),"firstNames");
        } catch (UMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        s.addAll(users);
        return s;
    }

    @Override
    public void setObject(Set<User> object) {
//      for (AmpFundingDetail ampFundingDetail : model.getObject())
//          if (ampFundingDetail.getTransactionType().equals(transactionType))
//              model.getObject().remove(ampFundingDetail);
        model.getObject().clear();
        model.getObject().addAll(object);
    }

}
