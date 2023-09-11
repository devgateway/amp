package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.trubudget.model.project.CreateProjectModel;

import java.util.Arrays;
import java.util.UUID;

public class ProjectUtil {
    public static void createProject(AmpActivityVersion ampActivityVersion)
    {
        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token =(String) myCache.get("truBudgetToken");
        String user =(String) myCache.get("truBudgetUser");
        System.out.println("Token:"+token);
        CreateProjectModel projectModel = new CreateProjectModel();
        CreateProjectModel.Data data = new CreateProjectModel.Data();
        CreateProjectModel.Project project = new CreateProjectModel.Project();
        project.setAssignee(user);
        project.setId(UUID.randomUUID().toString().replaceAll("-",""));
        project.setDisplayName(ampActivityVersion.getName());
        project.setDescription(ampActivityVersion.getDescription());
        project.setTags(Arrays.asList((ampActivityVersion.getName()+" "+ampActivityVersion.getDescription()).split(" ")));

    }
}
