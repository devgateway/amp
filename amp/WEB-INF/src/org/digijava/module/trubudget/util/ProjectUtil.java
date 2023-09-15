package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.model.project.CreateProjectModel;
import org.digijava.module.trubudget.model.project.EditProjectModel;
import org.digijava.module.trubudget.model.project.EditProjectedBudgetModel;
import org.digijava.module.um.model.TruLoginRequest;
import org.digijava.module.um.model.TruLoginResponse;
import org.digijava.module.um.util.GenericWebClient;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
        String token = (String) myCache.get("truBudgetToken");
        String user = (String) myCache.get("truBudgetUser");
        logger.info("Trubudget Cached Token:" + token);
        CreateProjectModel projectModel = new CreateProjectModel();
        CreateProjectModel.Data data = new CreateProjectModel.Data();

        projectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
        CreateProjectModel.Project project = new CreateProjectModel.Project();
        project.setAssignee(user);
        project.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        project.setDisplayName(ampActivityVersion.getName());
        project.setDescription(ampActivityVersion.getDescription());
        project.setStatus("open");// TODO: 9/11/23 set correct status
        project.setTags(Arrays.stream((ampActivityVersion.getName() + " " + ampActivityVersion.getDescription()).split(" ")).filter(x -> x.length() <= 15).collect(Collectors.toList()));
        List<Map<String, Object>> fundingDetails = new ArrayList<>();
        for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {

            for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                String adjustmentType = ampFundingDetail.getAdjustmentType().getValue();
                // TODO: 9/15/23 use adjustmentType to determine actual/planned  
                Double amount = ampFundingDetail.getTransactionAmount();
                String currency = ampFundingDetail.getAmpCurrencyId().getCurrencyCode();
                String organization = ampFundingDetail.getAmpFundingId().getAmpDonorOrgId().getName();
                String assistanceType = ampFundingDetail.getAmpFundingId().getTypeOfAssistance().getValue();
                String fundingStatus = ampFundingDetail.getAmpFundingId().getFundingStatus().getValue();
                CreateProjectModel.ProjectedBudget projectedBudget = new CreateProjectModel.ProjectedBudget();
                projectedBudget.setOrganization(organization);
                projectedBudget.setValue(BigDecimal.valueOf(amount).toString());
                projectedBudget.setCurrencyCode(currency);
                project.getProjectedBudgets().add(projectedBudget);
                Map<String, Object> detail = new HashMap<>();
                detail.put("organization", organization);
                detail.put("adjustmentType", adjustmentType);
                detail.put("assistanceType", assistanceType);
                detail.put("fundingStatus", fundingStatus);
                detail.put("projectedBudget", projectedBudget);
                fundingDetails.add(detail);
                Map<String, Object> additionalData = project.getAdditionalData();
                if (additionalData.containsKey("fundingDetails")) {
                    // TODO: 9/11/23 getdetails list and add to it
                    additionalData.get("fundingDetails");
                }
            }
        }
        project.setThumbnail("sampleThumbNail");
        data.setProject(project);
        projectModel.setData(data);
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "/api/global.createProject", projectModel, CreateProjectModel.class, String.class, token).subscribe(
                response -> logger.info("Create project response: " + response)
        );
        Session session = PersistenceManager.getRequestDBSession();
        TruBudgetActivity truBudgetActivity = new TruBudgetActivity();
        truBudgetActivity.setAmpActivityId(ampActivityVersion.getAmpActivityId());
        truBudgetActivity.setTruBudgetId(project.getId());
        session.saveOrUpdate(truBudgetActivity);
        session.flush();
    }

    public static TruBudgetActivity isActivityAlreadyInTrubudget(Long activityId) {
        Session session = PersistenceManager.getRequestDBSession();
        Query<TruBudgetActivity> query = session.createQuery("FROM " + TruBudgetActivity.class.getName() + " ta WHERE ta.ampActivityId=:ampActivityId", TruBudgetActivity.class);
        query.setParameter("ampActivityId", activityId, LongType.INSTANCE);
//        query.setParameter("truBudgetId",truBudgetId, StringType.INSTANCE);
        return query.stream().findAny().orElse(null);
    }

    public static void updateProject(String projectId, AmpActivityVersion ampActivityVersion) throws URISyntaxException {

        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        logger.info("Trubudget Cached Token:" + token);

        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        EditProjectModel editProjectModel = new EditProjectModel();
        editProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
        EditProjectModel.Data data = new EditProjectModel.Data();
        data.setProjectId(projectId);
        data.setDescription(ampActivityVersion.getDescription());
        data.setDisplayName(ampActivityVersion.getName());
        editProjectModel.setData(data);
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "/api/project.update", editProjectModel, EditProjectModel.class, String.class, token).subscribe(res -> {
            logger.info("Update project response: "+res);

        });

        for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {

            for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                Double amount = ampFundingDetail.getTransactionAmount();
                String currency = ampFundingDetail.getAmpCurrencyId().getCurrencyCode();
                String organization = ampFundingDetail.getAmpFundingId().getAmpDonorOrgId().getName();
                EditProjectedBudgetModel projectedBudget = new EditProjectedBudgetModel();
                projectedBudget.setApiVersion(getSettingValue(settings, "apiVersion"));
                EditProjectedBudgetModel.Data data1 = new EditProjectedBudgetModel.Data();
                data1.setOrganization(organization);
                data1.setValue(BigDecimal.valueOf(amount).toString());
                data1.setCurrencyCode(currency);
                data1.setProjectId(projectId);
                projectedBudget.setData(data1);

                    GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "/api/project.budget.updateProjected", projectedBudget, EditProjectedBudgetModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                            .subscribe(res2->logger.info("Update budget response: "+res2));

            }
        }
    }
}
