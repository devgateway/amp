package org.digijava.module.search.helper;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;

public class Resource implements LoggerIdentifiable {

    private String name;
    private String uuid;
    private String webLink;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String getObjectName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getObjectType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
    public String getWebLink(){
        return this.webLink;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Resource))
                return false;
        Resource r = (Resource) obj;
        if(this.name != null && !this.name.equals(r.name) || r.name != null && !r.name.equals(this.name))
            return false;
        if(this.uuid != null && !this.uuid.equals(r.uuid) || r.uuid != null && !r.uuid.equals(this.uuid))
            return false;
        if(this.webLink != null && !this.webLink.equals(r.webLink) || r.webLink != null && !r.webLink.equals(this.webLink))
            return false;
        
        return true;
    }
         @Override
        public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }


}
