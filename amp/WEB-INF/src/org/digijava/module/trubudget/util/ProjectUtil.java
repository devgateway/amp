package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.trubudget.model.project.CreateProjectModel;
import org.digijava.module.um.model.TruLoginRequest;
import org.digijava.module.um.model.TruLoginResponse;
import org.digijava.module.um.util.GenericWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;

public class ProjectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProjectUtil.class);
    public static void createProject(AmpActivityVersion ampActivityVersion) throws URISyntaxException {
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token =(String) myCache.get("truBudgetToken");
        String user =(String) myCache.get("truBudgetUser");
       logger.info("Trubudget Cached Token:"+token);
        CreateProjectModel projectModel = new CreateProjectModel();
        CreateProjectModel.Data data = new CreateProjectModel.Data();

        projectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
        CreateProjectModel.Project project = new CreateProjectModel.Project();
        project.setAssignee(user);
        project.setId(UUID.randomUUID().toString().replaceAll("-",""));
        project.setDisplayName(ampActivityVersion.getName());
        project.setDescription(ampActivityVersion.getDescription());
        project.setStatus("open");// TODO: 9/11/23 set correct status
        project.setTags(Arrays.stream((ampActivityVersion.getName()+" "+ampActivityVersion.getDescription()).split(" ")).filter(x->x.length()<=15).collect(Collectors.toList()));
        List<Map<String, Object>> fundingDetails = new ArrayList<>();
        for(AmpFunding ampFunding: ampActivityVersion.getFunding())
        {

            for (AmpFundingDetail ampFundingDetail: ampFunding.getFundingDetails())
            {
                Double amount = ampFundingDetail.getTransactionAmount();
                String currency = ampFundingDetail.getAmpCurrencyId().getCurrencyCode();
                String organization = ampFundingDetail.getAmpFundingId().getAmpDonorOrgId().getName();
                String adjustmentType = ampFundingDetail.getAdjustmentType().getValue();
                String assistanceType = ampFundingDetail.getAmpFundingId().getTypeOfAssistance().getValue();
                String fundingStatus = ampFundingDetail.getAmpFundingId().getFundingStatus().getValue();
                CreateProjectModel.ProjectedBudget projectedBudget= new CreateProjectModel.ProjectedBudget();
                projectedBudget.setOrganization(organization);
                projectedBudget.setValue(BigDecimal.valueOf(amount).toString());
                projectedBudget.setCurrencyCode(currency);
                project.getProjectedBudgets().add(projectedBudget);
                project.setThumbnail("sampleThumbNail");
                Map<String, Object> detail = new HashMap<>();
                detail.put("organization",organization);
                detail.put("adjustmentType",adjustmentType);
                detail.put("assistanceType",assistanceType);
                detail.put("fundingStatus",fundingStatus);
                detail.put("projectedBudget", projectedBudget);
                fundingDetails.add(detail);
                Map<String, Object> additionalData =project.getAdditionalData();
                if (additionalData.containsKey("fundingDetails"))
                {
                    // TODO: 9/11/23 getdetails list and add to it
                    additionalData.get("fundingDetails");
                }
            }
        }
        data.setProject(project);
        projectModel.setData(data);
         GenericWebClient.postForSingleObjResponse(getSettingValue(settings,"baseUrl")+"/api/global.createProject",projectModel, CreateProjectModel.class, String.class, token).subscribe(
                 response->logger.info("Create project response: "+response)
         );
    }
}
