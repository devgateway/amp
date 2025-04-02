/**
 * 
 */
package org.digijava.module.gateperm.gates;

import java.util.Map;
import java.util.Queue;

/**
 * Implements logic for organization roles user access filtering. users are
 * assigned to organizations through the um module (verified assigned
 * organization). An user will have access to an activity if he has been
 * assigned to an Organization that has a role in the current activity and if
 * that role corresponds with the parameter of this gate. this gate has only one
 * parameter that states the type of role of the organization (implementing,
 * executing,etc...)
 * 
 * @author mihai
 */
public class OrgRoleGate extends AbstractOrgRoleGate {

    private static final String DESCRIPTION = "Implements logic for organization roles user access filtering. "
            + " Users are assigned "
            + "to organisations through the User Manager module (verified assigned organisation). "
            + " An user will have access to an activity if he has been assigned to an organisation that "
            + " has a role in the current activity and if that role corresponds with the parameter of this "
            + "gate. This gate has only one parameter that states the code of the role of the "
            + "organisation. Use as parameter the role CODE associated with the role. "
            + " Example: EA for Executing Agency...etc. "
            + "Check the Org Role Manager";

    /**
     * @param scope
     * @param parameters
     */
    public OrgRoleGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
    }

    /**
     * 
     */
    public OrgRoleGate() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    @Override
    public String description() {
        return DESCRIPTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        populateValues();
        Boolean canDo = checkIfDonorIsVerifiedOrganisation(false);
        if (canDo != null) {
            return canDo;
        } else {
            return checkVerifiedOrganisations();
        }
    }

}
