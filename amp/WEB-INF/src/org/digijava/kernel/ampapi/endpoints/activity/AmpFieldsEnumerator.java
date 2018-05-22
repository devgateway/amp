package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.fm.FMService;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author Octavian Ciubotaru
 */
public final class AmpFieldsEnumerator {

    public static final FieldsEnumerator PUBLIC_ENUMERATOR;
    public static final FieldsEnumerator PRIVATE_ENUMERATOR;

    static {
        AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);
        AMPFeatureManagerService fmService = new AMPFeatureManagerService();
        PUBLIC_ENUMERATOR = new FieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE, false);
        PRIVATE_ENUMERATOR = new FieldsEnumerator(provider, fmService, AMPTranslatorService.INSTANCE, true);
    }

    private AmpFieldsEnumerator() {
    }

    /**
     * Group the fields by workspace member
     * 
     * @param fieldsEnumarator
     * @param wsMemberIds
     * @return
     */
    public static List<APIWorkspaceMemberFieldList> getAvailableFieldsBasedOnWs(List<Long> wsMemberIds) {
        List<APIWorkspaceMemberFieldList> wsList = new ArrayList<>();
        
        Map<Long, List<Long>> fmTreesWsMap = FMService.getFMTreeWsMap();
        
        fmTreesWsMap.entrySet().forEach(t -> {
            Long templateId = t.getKey();
            List<Long> wsIds = t.getValue();
            
            if (!wsMemberIds.isEmpty()) {
                wsIds.retainAll(wsMemberIds);
            }
            
            if (!wsIds.isEmpty()) {
                AmpFieldInfoProvider provider = new AmpFieldInfoProvider(AmpActivityVersion.class);
                FMVisibility fmVisbility = new FMVisibility(templateId);
                AMPFeatureManagerService fmService = new AMPFeatureManagerService(fmVisbility);
                FieldsEnumerator fieldsEnumarator = new FieldsEnumerator(provider, fmService, 
                        AMPTranslatorService.INSTANCE, false);
                APIWorkspaceMemberFieldList fieldList = new APIWorkspaceMemberFieldList(wsIds, 
                        fieldsEnumarator.getAllAvailableFields());
                wsList.add(fieldList);
            }
        });
        
        return wsList;
    }
}
