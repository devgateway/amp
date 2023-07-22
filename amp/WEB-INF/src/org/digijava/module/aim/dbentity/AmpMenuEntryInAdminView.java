/**
 * 
 */
package org.digijava.module.aim.dbentity;

import javax.persistence.*;

/**
 * @author Nadejda Mandrescu
 */
@Entity
@DiscriminatorValue("1")
public class AmpMenuEntryInAdminView extends AmpMenuEntryInView {
}
