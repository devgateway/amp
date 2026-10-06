package org.digijava.kernel.services;

import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumeratorFactory;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.kernel.services.sync.SyncDAO;
import org.digijava.module.aim.util.ActivityUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
@Component
public final class AmpFieldsEnumerator implements InitializingBean {

    private static CachingFieldsEnumeratorFactory enumeratorFactory;

    public static final String TYPE_ACTIVITY = "TYPE_ACTIVITY";
    public static final String TYPE_CONTACT = "TYPE_CONTACT";
    public static final String TYPE_RESOURCE = "TYPE_RESOURCE";

    @Autowired
    private SyncDAO syncDAO;

    public static CachingFieldsEnumerator getEnumerator() {
        return enumeratorFactory.getDefaultEnumerator();
    }

    public static CachingFieldsEnumerator getEnumerator(Long id) {
        return enumeratorFactory.getEnumerator(id);
    }

    public static Map<Long, CachingFieldsEnumerator> getAllEnumerators() {
        return enumeratorFactory.getAllEnumerators();
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
     * @param type
     * @return
     */
    public static List<APIWorkspaceMemberFieldList> getAvailableFieldsBasedOnWs(List<Long> wsMemberIds, String type) {
        List<APIWorkspaceMemberFieldList> wsList = new ArrayList<>();

        Map<Long, List<Long>> fmTreesWsMap = FMService.getFMTreeWsMap();

        for (Map.Entry<Long, List<Long>> t : fmTreesWsMap.entrySet()) {
            Long templateId = t.getKey();
            List<Long> wsIds = t.getValue();

            if (!wsMemberIds.isEmpty()) {
                wsIds.retainAll(wsMemberIds);
            }

            ActivityUtil.loadWorkspacePrefixesIntoRequest();

            if (!wsIds.isEmpty()) {
                CachingFieldsEnumerator cachingFieldsEnumerator = enumeratorFactory.getEnumerator(templateId);
                if (type.equals(TYPE_ACTIVITY)) {
                    APIWorkspaceMemberFieldList fieldList = new APIWorkspaceMemberFieldList(wsIds,
                            cachingFieldsEnumerator.getActivityFields());
                    wsList.add(fieldList);
                } else if (type.equals(TYPE_CONTACT)) {
                    APIWorkspaceMemberFieldList fieldList = new APIWorkspaceMemberFieldList(wsIds,
                            cachingFieldsEnumerator.getContactFields());
                    wsList.add(fieldList);
                } else if (type.equals(TYPE_RESOURCE)) {
                    APIWorkspaceMemberFieldList fieldList = new APIWorkspaceMemberFieldList(wsIds,
                            cachingFieldsEnumerator.getResourceFields());
                    wsList.add(fieldList);
                }
            }
        }

        return wsList;
    }
}
