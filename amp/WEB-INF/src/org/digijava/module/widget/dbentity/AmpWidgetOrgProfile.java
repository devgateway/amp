/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.widget.dbentity;

import org.digijava.module.widget.helper.WidgetVisitor;

/**
 *
 * @author medea
 */
public class AmpWidgetOrgProfile extends AmpWidget {
    private static final long serialVersionUID = 1L;
    
    private Long type;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
    @Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
