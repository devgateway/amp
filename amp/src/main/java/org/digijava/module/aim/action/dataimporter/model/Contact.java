package org.digijava.module.aim.action.dataimporter.model;

public class Contact {
    private Long id;
    private Long contact;
    private boolean mark_as_primary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public boolean isMark_as_primary() {
        return mark_as_primary;
    }

    public void setMark_as_primary(boolean mark_as_primary) {
        this.mark_as_primary = mark_as_primary;
    }
}
