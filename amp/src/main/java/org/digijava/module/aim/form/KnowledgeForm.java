package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class KnowledgeForm extends ActionForm
{
    private Long id ;
    private Long ampKmId;
    private Long ampNotesId;
    private String name ;
    private String description ;
    private Collection documents ;
    private Collection managedDocuments;
    private Collection notes ;
    private int flag;
    private int dflag;
    private String display;
    private String ndisplay;
    private boolean validLogin;

    public Long getId()
    {
        return id ;
    }

    public String getDisplay()
    {
        return display;
    }

    public String getNdisplay()
    {
        return ndisplay;
    }

    public Long getAmpKmId()
    {
        return ampKmId ;
    }

    public Long getAmpNotesId()
    {
        return ampNotesId ;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public int getFlag()
    {
        return flag;
    }

    public int getDflag()
    {
        return dflag;
    }

    public boolean getValidLogin()
    {
        return validLogin;
    }

    public void setDescription(String description)
    {
        this.description = description ;
    }

    public void setName(String name)
    {
        this.name = name ;
    }

    public void setId(Long id)
    {
        this.id = id ;
    }

    public void setAmpKmId(Long id)
    {
        this.ampKmId = id ;
    }

    public void setAmpNotesId(Long id)
    {
        this.ampNotesId = id ;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public void setDflag(int dflag)
    {
        this.dflag = dflag;
    }

    public void setValidLogin(boolean bool)
    {
        this.validLogin = bool ;
    }


    /**
     * @return
     */
    public Collection getDocuments()
    {
        return documents;
    }

    /**
     * @return
     */
    public Collection getNotes()
    {
        return notes;
    }

    public Collection getManagedDocuments() {
        return managedDocuments;
    }

    /**
     * @param collection
     */
    public void setNotes(Collection notes)
    {
        this.notes = notes;
    }

    /**
     * @param collection
     */
    public void setDocuments(Collection documents)
    {
        this.documents=documents;
    }
    public void setDisplay(String display)
    {
        this.display = display;
    }
    public void setNdisplay(String ndisplay)
    {
        this.ndisplay = ndisplay;
    }

    public void setManagedDocuments(Collection managedDocuments) {
        this.managedDocuments = managedDocuments;
    }
}
