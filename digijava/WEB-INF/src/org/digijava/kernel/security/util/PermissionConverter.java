package org.digijava.kernel.security.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Permission;
import java.util.Iterator;

import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.entity.PrincipalPermission;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ModuleInstancePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.DummyServletContext;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.digijava.kernel.security.DigiPolicy;
import org.digijava.kernel.security.principal.GroupPrincipal;

public class PermissionConverter {

    public static void main(String[] args) throws Exception {
        ViewConfigFactory.initialize(new DummyServletContext("."));
        DigiConfigManager.initialize("./repository");
        PersistenceManager.initialize(false);
        DigiPolicy digiPolicy = new DigiPolicy();
        try {
            Session session = PersistenceManager.getSession();
            Iterator iter = session.iterate("from " + GroupPermission.class.getName());
            while (iter.hasNext()) {
                GroupPermission item = (GroupPermission)iter.next();
                Permission permission = createPermission(session, item);

                GroupPrincipal gp = new GroupPrincipal(item.getGroup().getId().longValue());

                digiPolicy.grant(gp, permission);
            }
            PersistenceManager.releaseSession(session);
        }
        finally {
            PersistenceManager.cleanup();
        }
    }

    private static Permission createPermission(Session session,
                                        GroupPermission groupPermission) throws
        Exception {
        Permission permission = null;
        switch (groupPermission.getPermissionType()) {
            case GroupPermission.SITE_PERMISSION:

                // Find target site
                Site targetSite = groupPermission.getGroup().getSite();
                permission = new SitePermission(targetSite,
                                                groupPermission.getActions());
                break;
            case GroupPermission.MODULE_INSTANCE_PERMISSION:
                Long moduleInstanceId = new Long(groupPermission.
                                                 getTargetName());
                ModuleInstance moduleInstance = (ModuleInstance)
                    session.load(ModuleInstance.class, moduleInstanceId);
                permission = new ModuleInstancePermission(moduleInstance,
                    groupPermission.getActions());

                break;
        }

        return permission;
    }

    private static byte[] serializePermission(Permission permission) throws IOException {
        byte[] serializedForm;
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(str);
        ous.writeObject(permission);
        ous.flush();
        serializedForm = str.toByteArray();
        ous.close();
        return serializedForm;
    }

}