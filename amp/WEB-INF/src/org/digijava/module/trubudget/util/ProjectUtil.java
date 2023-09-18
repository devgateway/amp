package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.entity.trubudget.SubIntents;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.model.project.*;
import org.digijava.module.trubudget.model.subproject.EditSubProjectModel;
import org.digijava.module.trubudget.model.subproject.EditSubProjectedBudgetModel;
import org.digijava.module.trubudget.model.subproject.SubProjectGrantRevokePermModel;
import org.digijava.module.um.util.GenericWebClient;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.um.util.DbUtil.*;

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
                Integer transactionType = ampFundingDetail.getTransactionType();

                Double amount = ampFundingDetail.getTransactionAmount();
                String currency = ampFundingDetail.getAmpCurrencyId().getCurrencyCode();
                String organization = ampFundingDetail.getAmpFundingId().getAmpDonorOrgId().getName();
                String assistanceType = ampFundingDetail.getAmpFundingId().getTypeOfAssistance().getValue();
                String fundingStatus = ampFundingDetail.getAmpFundingId().getFundingStatus().getValue();
                CreateProjectModel.ProjectedBudget projectedBudget = new CreateProjectModel.ProjectedBudget();
                if (Objects.equals(adjustmentType, "Actual") && transactionType==0)//project budget is created using "actual commitment"
                {
                    projectedBudget.setOrganization(organization);
                    projectedBudget.setValue(BigDecimal.valueOf(amount).toString());
                    projectedBudget.setCurrencyCode(currency);
                    project.getProjectedBudgets().add(projectedBudget);
                }

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
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/global.createProject", projectModel, CreateProjectModel.class, String.class, token).subscribe(
                response -> logger.info("Create project response: " + response)
        );
        List<SubIntents> subIntents = getSubIntentsByMother("project");
        subIntents.forEach(subIntent -> {
            ProjectGrantRevokePermModel projectGrantRevokePermModel = new ProjectGrantRevokePermModel();
            ProjectGrantRevokePermModel.Data data1 = new ProjectGrantRevokePermModel.Data();
            data1.setProjectId(project.getId());
            data1.setIdentity(user);
            data1.setIntent(subIntent.getSubTruBudgetIntentName());
            projectGrantRevokePermModel.setData(data1);
            projectGrantRevokePermModel.setApiVersion(getSettingValue(settings, "apiVersion"));
            try {
                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.intent.grantPermission", projectGrantRevokePermModel, ProjectGrantRevokePermModel.class, String.class, token).subscribeOn(Schedulers.parallel()).subscribe(
                        response -> logger.info("Grant project permission response: " + response)
                );
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        });
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
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.update", editProjectModel, EditProjectModel.class, String.class, token).subscribe(res -> {
            logger.info("Update project response: "+res);

        });

        for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {

            for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                String adjustmentType = ampFundingDetail.getAdjustmentType().getValue();
                Integer transactionType = ampFundingDetail.getTransactionType();

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
                if (Objects.equals(adjustmentType, "Actual") && transactionType==0)//project budget is edited using "actual commitment"
                {
                    GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.budget.updateProjected", projectedBudget, EditProjectedBudgetModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                            .subscribe(res2->logger.info("Update budget response: "+res2));
                }




            }
        }
    }
    public static void createSubProjects(AmpActivityVersion ampActivityVersion, String projectId) throws URISyntaxException {
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        String user = (String) myCache.get("truBudgetUser");
        logger.info("Trubudget Cached Token:" + token);
        for (AmpComponent ampComponent: ampActivityVersion.getComponents())
        {
            if (ampComponent.getSubProjectId()==null) {//create subproject
                CreateSubProjectModel createSubProjectModel = new CreateSubProjectModel();
                CreateSubProjectModel.Data data = new CreateSubProjectModel.Data();
                CreateSubProjectModel.Subproject subproject = new CreateSubProjectModel.Subproject();
                subproject.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//            subproject.setAmount(ampComponent.);
                subproject.setDisplayName(ampComponent.getTitle());
//                BigDecimal totalFunding = BigDecimal.ZERO;
                if (!ampComponent.getFundings().isEmpty()) {
                    for (AmpComponentFunding componentFunding : ampComponent.getFundings()) {
                        if (componentFunding.getTransactionType() == 0 && Objects.equals(componentFunding.getAdjustmentType().getValue(), "Planned")) {
//                            totalFunding = totalFunding.add(BigDecimal.valueOf(componentFunding.getTransactionAmount()));
                            CreateProjectModel.ProjectedBudget projectedBudget = new CreateProjectModel.ProjectedBudget();
                            projectedBudget.setOrganization(componentFunding.getReportingOrganization().getName());
                            projectedBudget.setValue(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toString());
                            projectedBudget.setCurrencyCode(componentFunding.getCurrency().getCurrencyCode());
                        }
                    }
                    subproject.setCurrency(new ArrayList<>(ampComponent.getFundings()).get(0).getCurrency().getCurrencyCode());
                }
//                subproject.setAmount(totalFunding.toString());
                subproject.setAssignee(user);
                subproject.setValidator(user);
                subproject.setDescription(ampComponent.getDescription());

                data.setProjectId(projectId);
                data.setSubproject(subproject);
                createSubProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                createSubProjectModel.setData(data);
                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.createSubproject", createSubProjectModel, CreateSubProjectModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                        .subscribe(res2 -> logger.info("Create subproject response: " + res2));
                List<SubIntents> subIntents = getSubIntentsByMother("subproject");
                subIntents.forEach(subIntent -> {
                    SubProjectGrantRevokePermModel subProjectGrantRevokePermModel = new SubProjectGrantRevokePermModel();
                    SubProjectGrantRevokePermModel.Data data2 = new SubProjectGrantRevokePermModel.Data();
                    data2.setProjectId(projectId);
                    data2.setIdentity(user);
                    data2.setIntent(subIntent.getSubTruBudgetIntentName());
                    subProjectGrantRevokePermModel.setData(data2);
                    subProjectGrantRevokePermModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                    try {
                        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.intent.grantPermission", subProjectGrantRevokePermModel, SubProjectGrantRevokePermModel.class, String.class, token).subscribeOn(Schedulers.parallel()).subscribe(
                                response -> logger.info("Grant subproject permission response: " + response)
                        );
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                });
                ampComponent.setSubProjectId(subproject.getId());
                Session session = PersistenceManager.getRequestDBSession();
                session.merge(ampComponent);
                session.flush();
            }
            else {//update subProject
                EditSubProjectModel editSubProjectModel = new EditSubProjectModel();
                EditSubProjectModel.Data data = new EditSubProjectModel.Data();
                data.setProjectId(projectId);
                data.setSubprojectId(ampComponent.getSubProjectId());
                data.setDescription(ampComponent.getDescription());
                data.setDisplayName(ampComponent.getTitle());
                editSubProjectModel.setData(data);
                editSubProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                //call edit
                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.update", editSubProjectModel, EditSubProjectModel.class, String.class, token)
                        .subscribe(res2->logger.info("Update subproject response: "+res2));
                if (!ampComponent.getFundings().isEmpty()) {
                    for (AmpComponentFunding componentFunding : ampComponent.getFundings()) {
                        EditSubProjectedBudgetModel editSubProjectedBudgetModel = new EditSubProjectedBudgetModel();
                        EditSubProjectedBudgetModel.Data data1 = new EditSubProjectedBudgetModel.Data();
                        data1.setProjectId(projectId);
                        data1.setSubprojectId(ampComponent.getSubProjectId());
                        data1.setValue(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toString());
                        data1.setOrganization(componentFunding.getReportingOrganization().getName());
                        editSubProjectedBudgetModel.setData(data1);
                        editSubProjectedBudgetModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.budget.updateProjected", editSubProjectedBudgetModel, EditSubProjectedBudgetModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                                .subscribe(res2->logger.info("Update subproject budget response: "+res2));

                    }
                }
            }
        }
    }
}
