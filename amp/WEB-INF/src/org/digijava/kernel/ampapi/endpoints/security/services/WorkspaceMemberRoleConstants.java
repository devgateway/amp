/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security.services;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nadejda Mandrescu
 */
public class WorkspaceMemberRoleConstants {
    /*
     * Workspace Member Roles are pretty stable. If a new role is added, then additional logic will be needed. 
     * Thus we won't be delivering the roles via API (for AMP Offline client sync) and rather hard code them there.
     * However we just need to ensure that the IDs provided are correct 
     */
    public static final Map<String, Long> ROLE_ID_MAP = new HashMap<String, Long>() {{
        put("Workspace Manager", 1l);
        put("Workspace Member", 2l);
        put("Workspace Approver", 3l);
    }};

}
