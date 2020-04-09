package org.digijava.kernel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumeratorFactory;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.services.sync.SyncDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public final class AmpFieldsEnumerator implements InitializingBean {

    private static CachingFieldsEnumeratorFactory enumeratorFactory;

    @Autowired
    private SyncDAO syncDAO;

    public static CachingFieldsEnumerator getEnumerator() {
        return enumeratorFactory.getDefaultEnumerator();
    }

    @Override
    public void afterPropertiesSet() {
       enumeratorFactory = new CachingFieldsEnumeratorFactory(syncDAO);
       enumeratorFactory.buildDefaultEnumerator();
    }

    /**
     * Group the fields by workspace member
     *
     * @param wsMemberIds
     * @return
     */
    public static List<APIWorkspaceMemberFieldList> getAvailableActivityFieldsBasedOnWs(List<Long> wsMemberIds) {
        List<APIWorkspaceMemberFieldList> wsList = new ArrayList<>();

        Map<Long, List<Long>> fmTreesWsMap = FMService.getFMTreeWsMap();

        for (Map.Entry<Long, List<Long>> t : fmTreesWsMap.entrySet()) {
            Long templateId = t.getKey();
            List<Long> wsIds = t.getValue();

            if (!wsMemberIds.isEmpty()) {
                wsIds.retainAll(wsMemberIds);
            }

            if (!wsIds.isEmpty()) {
                CachingFieldsEnumerator cachingFieldsEnumerator = enumeratorFactory.getEnumerator(templateId);
                APIWorkspaceMemberFieldList fieldList = new APIWorkspaceMemberFieldList(wsIds,
                        cachingFieldsEnumerator.getActivityFields());
                wsList.add(fieldList);
            }
        }

        return wsList;
    }
}
