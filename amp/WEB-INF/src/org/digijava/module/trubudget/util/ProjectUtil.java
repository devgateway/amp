package org.digijava.module.trubudget.util;

import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.TemporaryComponentFundingDocument;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.entity.trubudget.SubIntents;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.trubudget.dbentity.AmpComponentFundingTruWF;
import org.digijava.module.trubudget.dbentity.AmpComponentTruSubProject;
import org.digijava.module.trubudget.dbentity.TruBudgetActivity;
import org.digijava.module.trubudget.model.project.*;
import org.digijava.module.trubudget.model.subproject.*;
import org.digijava.module.trubudget.model.workflowitem.*;
import org.digijava.module.um.util.GenericWebClient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.um.util.DbUtil.*;

public class ProjectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProjectUtil.class);
    private static Session session;
    private static Transaction transaction;

    public static void init()
    {
        session= PersistenceManager.openNewSession();
        transaction= session.beginTransaction();

    }

    public static void  end()
    {
        Transaction transaction = session.getTransaction();
        while (true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!transaction.isActive()){
                session.close();
                break;
            }
        }
    }
    public static void refreshSession()
    {
        if (!session.isOpen()){
            session = PersistenceManager.openNewSession();
            transaction= session.beginTransaction();
        }
        if (!transaction.isActive()){
            transaction=session.beginTransaction();
        }
    }

    public static void createProject(AmpActivityVersion ampActivityVersion, List<AmpComponent> ampComponents) throws URISyntaxException {
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
        logger.info("Project Name is: "+ampActivityVersion.getName());
        project.setDisplayName(ampActivityVersion.getName());
        project.setDescription(ampActivityVersion.getDescription());
        project.setStatus("open");// TODO: 9/11/23 set correct status
        project.setTags(Arrays.stream((ampActivityVersion.getName() + " " + ampActivityVersion.getDescription()).split(" ")).filter(x -> x.length() <= 15).collect(Collectors.toList()));
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
        for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {
            if (ampFunding.getFundingDetails()!=null) {

                for (AmpFundingDetail ampFundingDetail : ampFunding.getFundingDetails()) {
                    String adjustmentType = ampFundingDetail.getAdjustmentType().getValue();
                    Integer transactionType = ampFundingDetail.getTransactionType();

                    Double amount = ampFundingDetail.getTransactionAmount();
                    String currency = ampFundingDetail.getAmpCurrencyId().getCurrencyCode();
                    String organization = ampFundingDetail.getAmpFundingId().getAmpDonorOrgId().getName();
                    CreateProjectModel.ProjectedBudget projectedBudget = new CreateProjectModel.ProjectedBudget();
                    if (Objects.equals(adjustmentType, "Actual") && transactionType == 0)//project budget is created using "actual commitment"
                    {
                        projectedBudget.setOrganization(organization);
                        projectedBudget.setValue(BigDecimal.valueOf(amount).toPlainString());
                        projectedBudget.setCurrencyCode(currency);
                        project.getProjectedBudgets().add(projectedBudget);
                    }


                }
            }
        }
        project.setThumbnail("sampleThumbNail");
        data.setProject(project);
        projectModel.setData(data);
        List<SubIntents> subIntents = getSubIntentsByMother("project");
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/global.createProject", projectModel, CreateProjectModel.class, String.class, token).subscribe(
                response ->{
                    logger.info("Create project response: " + response);
                    try {
                        refreshSession();
                        TruBudgetActivity truBudgetActivity = new TruBudgetActivity();
                        truBudgetActivity.setAmpActivityId(ampActivityVersion.getAmpActivityId());
                        truBudgetActivity.setTruBudgetId(project.getId());
                        session.save(truBudgetActivity);
                        session.flush();
                        transaction.commit();
                        createUpdateSubProjects(ampComponents, project.getId(),settings,s);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
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
                                    response1 -> logger.info("Grant project permission response: " + response1)
                            );
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }




                    });

                }
        );



//        session.flush();

    }

    public static TruBudgetActivity activityAlreadyInTrubudget(Long activityId) {
        Session session = PersistenceManager.getRequestDBSession();
        Query<TruBudgetActivity> query = session.createQuery("FROM " + TruBudgetActivity.class.getName() + " ta WHERE ta.ampActivityId=:ampActivityId", TruBudgetActivity.class);
        query.setParameter("ampActivityId", activityId, LongType.INSTANCE);
//        query.setParameter("truBudgetId",truBudgetId, StringType.INSTANCE);
        return query.stream().findAny().orElse(null);
    }



    public static void updateProject(String projectId, AmpActivityVersion ampActivityVersion, List<AmpComponent> ampComponents) throws URISyntaxException {

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
        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.update", editProjectModel, EditProjectModel.class, String.class, token).subscribe(res -> {
            logger.info("Update project response: "+res);
            try {
                createUpdateSubProjects(ampComponents, projectId,settings,s);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }


        });

        for (AmpFunding ampFunding : ampActivityVersion.getFunding()) {
            if (ampFunding.getFundingDetails()!=null) {
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
                    data1.setValue(BigDecimal.valueOf(amount).toPlainString());
                    data1.setCurrencyCode(currency);
                    data1.setProjectId(projectId);
                    projectedBudget.setData(data1);
                    if (Objects.equals(adjustmentType, "Actual") && transactionType == 0)//project budget is edited using "actual commitment"
                    {
                        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.budget.updateProjected", projectedBudget, EditProjectedBudgetModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                                .subscribe(res2 -> logger.info("Update budget response: " + res2));
                    }


                }
            }
        }

    }

    public static void closeProject(String projectId) throws URISyntaxException {
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        CloseProjectModel closeProjectModel = new CloseProjectModel();
        closeProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
        CloseProjectModel.Data data = new CloseProjectModel.Data();
        data.setProjectId(projectId);
        closeProjectModel.setData(data);
       session.createQuery("FROM " + AmpComponentTruSubProject.class.getName() + " act WHERE act.truProjectId= '" + projectId + "'", AmpComponentTruSubProject.class).list().forEach(
                subProject->{
                    try {
                        // TODO: 10/16/23 add functionality to close wf
//                        AmpComponentFundingTruWF ampComponentFundingTruWF = session.createQuery("FROM " + AmpComponentFundingTruWF.class.getName() + " act WHERE act.truSubprojectId= '" + subProject.getTruSubProjectId() + "'", AmpComponentFundingTruWF.class).stream().findAny().orElse(null);
//                        WorkflowItemDetailsModel workflowItemDetailsModel = getWFItemDetails(ampComponentFundingTruWF);
//                        if(workflowItemDetailsModel!=null) {
//                            if (workflowItemDetailsModel.getData().getWorkflowitem().getData().getStatus().equalsIgnoreCase("open")) {
////                                closeWorkFlowItem();
//                            }
//                        }
                        String res = closeSubProject(settings,projectId,subProject.getTruSubProjectId(), token);
                        logger.info("Subproject close response: Item "+subProject.getTruSubProjectId()+":Res : "+res);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
        );


        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.close", closeProjectModel, CloseProjectModel.class, String.class, token)
                    .subscribe(res -> logger.info("Project close response: "+res));

    }

    public static String closeSubProject(List<AmpGlobalSettings>settings, String projectId,String subProjectId, String token) throws URISyntaxException {
        CloseSubProjectModel closeSubProjectModel = new CloseSubProjectModel();
        closeSubProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
        CloseSubProjectModel.Data data = new CloseSubProjectModel.Data();
        data.setProjectId(projectId);
        data.setSubprojectId(subProjectId);
        closeSubProjectModel.setData(data);

        return GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.close", closeSubProjectModel, CloseSubProjectModel.class, String.class, token)
                .block();

    }
    public static void createUpdateSubProjects(List<AmpComponent> components, String projectId, List<AmpGlobalSettings> settings, AmpAuthWebSession ampAuthWebSession) throws URISyntaxException {

        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        String user = (String) myCache.get("truBudgetUser");
        logger.info("Trubudget Cached Token:" + token);
        for (AmpComponent ampComponent:components)
        {
            refreshSession();

            final AmpComponentTruSubProject[] ampComponentTruSubProject = {session.createQuery("FROM " + AmpComponentTruSubProject.class.getName() + " act WHERE act.ampComponentId= " + ampComponent.getAmpComponentId() + " AND act.ampComponentId IS NOT NULL", AmpComponentTruSubProject.class).stream().findAny().orElse(null)};

            if (ampComponentTruSubProject[0] ==null) {//create subproject
                CreateSubProjectModel createSubProjectModel = new CreateSubProjectModel();
                CreateSubProjectModel.Data data = new CreateSubProjectModel.Data();
                CreateSubProjectModel.Subproject subproject = new CreateSubProjectModel.Subproject();
                subproject.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                subproject.setDisplayName(ampComponent.getTitle());
                subproject.setCurrency(getSettingValue(settings, "defaultSubProjectCurrency")!=null?getSettingValue(settings, "defaultSubProjectCurrency"):"USD");
                if (ampComponent.getFundings()!=null) {
                    if (!ampComponent.getFundings().isEmpty()) {
                        for (AmpComponentFunding componentFunding : ampComponent.getFundings()) {
                            if (componentFunding.getTransactionType() == 0 && Objects.equals(componentFunding.getAdjustmentType().getValue(), "Planned")) {
                                CreateProjectModel.ProjectedBudget projectedBudget = new CreateProjectModel.ProjectedBudget();
                                projectedBudget.setOrganization(componentFunding.getReportingOrganization() != null ? componentFunding.getReportingOrganization().getName() : "Funding Org");
                                projectedBudget.setValue(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toPlainString());
                                projectedBudget.setCurrencyCode(componentFunding.getCurrency().getCurrencyCode());
                                subproject.getProjectedBudgets().add(projectedBudget);
                            }
                        }
                        subproject.setCurrency(new ArrayList<>(ampComponent.getFundings()).get(0).getCurrency().getCurrencyCode());
                    }
                }
                subproject.setAssignee(user);
                subproject.setValidator(user);
                subproject.setDescription(ampComponent.getDescription());

                data.setProjectId(projectId);
                data.setSubproject(subproject);
                createSubProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                createSubProjectModel.setData(data);
                List<SubIntents> subIntents = getSubIntentsByMother("subproject");
                try {
                    GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/project.createSubproject", createSubProjectModel, CreateSubProjectModel.class, String.class, token)
                            .subscribe(res-> {
                                logger.info("Create subproject response: "+res);
                                refreshSession();

                                ampComponentTruSubProject[0] =new AmpComponentTruSubProject();


                                ampComponentTruSubProject[0].setAmpComponentId(ampComponent.getAmpComponentId());
                                ampComponentTruSubProject[0].setTruSubProjectId(subproject.getId());
                                ampComponentTruSubProject[0].setTruProjectId(projectId);
                                session.save(ampComponentTruSubProject[0]);
                                try {
                                    session.flush();

                                    transaction.commit();
                                    createUpdateWorkflowItems(projectId, subproject.getId(),ampComponent, settings, ampAuthWebSession);
                                } catch (URISyntaxException e) {
                                    throw new RuntimeException(e);
                                }
                                        subIntents.forEach(subIntent -> {
                                            SubProjectGrantRevokePermModel subProjectGrantRevokePermModel = new SubProjectGrantRevokePermModel();
                                            SubProjectGrantRevokePermModel.Data data2 = new SubProjectGrantRevokePermModel.Data();
                                            data2.setProjectId(projectId);
                                            data2.setSubprojectId(subproject.getId());
                                            data2.setIdentity(user);
                                            data2.setIntent(subIntent.getSubTruBudgetIntentName());
                                            subProjectGrantRevokePermModel.setData(data2);

                                            subProjectGrantRevokePermModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                                            try {
                                                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.intent.grantPermission", subProjectGrantRevokePermModel, SubProjectGrantRevokePermModel.class, String.class, token).subscribeOn(Schedulers.parallel()).subscribe(
                                                        response ->{
                                                            logger.info("Grant subproject permission response: " + response);

                                                        } );
                                            } catch (URISyntaxException e) {
                                                throw new RuntimeException(e);
                                            }

                                        });

                                    });

                }catch (Exception e)
                {
                    logger.info("Error during subproject creation");
                    e.printStackTrace();
                }

//                ampComponent.setAmpComponentTruBudgetSubProjectId(subproject.getId());

            }
            else {//update subProject
                EditSubProjectModel editSubProjectModel = new EditSubProjectModel();
                EditSubProjectModel.Data data = new EditSubProjectModel.Data();
                data.setProjectId(projectId);
                data.setSubprojectId(ampComponentTruSubProject[0].getTruSubProjectId());
                data.setDescription(ampComponent.getDescription());
                data.setDisplayName(ampComponent.getTitle());

                editSubProjectModel.setData(data);

                editSubProjectModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                //call edit
                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.update", editSubProjectModel, EditSubProjectModel.class, String.class, token)
                        .subscribe(res2->logger.info("Update subproject response: "+res2));
                if (ampComponent.getFundings()!=null) {
                    if (!ampComponent.getFundings().isEmpty()) {
                        for (AmpComponentFunding componentFunding : ampComponent.getFundings()) {
                            EditSubProjectedBudgetModel editSubProjectedBudgetModel = new EditSubProjectedBudgetModel();
                            EditSubProjectedBudgetModel.Data data1 = new EditSubProjectedBudgetModel.Data();
                            data1.setProjectId(projectId);
                            data1.setSubprojectId(ampComponentTruSubProject[0].getTruSubProjectId());
                            data1.setCurrencyCode(componentFunding.getCurrency().getCurrencyCode());
                            data1.setValue(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toPlainString());
                            data1.setOrganization(componentFunding.getReportingOrganization() != null ? componentFunding.getReportingOrganization().getName() : "Funding Org");
                            editSubProjectedBudgetModel.setData(data1);
                            editSubProjectedBudgetModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                            GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.budget.updateProjected", editSubProjectedBudgetModel, EditSubProjectedBudgetModel.class, String.class, token).subscribeOn(Schedulers.parallel())
                                    .subscribe(res2 ->{
                                        logger.info("Update subproject budget response: " + res2) ;
                                        try {
                                            createUpdateWorkflowItems(projectId, ampComponentTruSubProject[0].getTruSubProjectId(),ampComponent, settings,ampAuthWebSession);
                                        } catch (URISyntaxException e) {
                                            throw new RuntimeException(e);
                                        }

                                    } );

                        }
                    }
                }

            }
        }
    }
    public static void createUpdateWorkflowItems(String projectId, String subProjectId, AmpComponent ampComponent, List<AmpGlobalSettings> settings, AmpAuthWebSession ampAuthWebSession) throws URISyntaxException {
        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        String user = (String) myCache.get("truBudgetUser");
        logger.info("Trubudget Cached Token:" + token);
        if (ampComponent.getFundings()!=null) {
                if (!ampComponent.getFundings().isEmpty()) {
                    for (AmpComponentFunding componentFunding : ampComponent.getFundings()) {
                        if (componentFunding.getTransactionType() == 1 && (Objects.equals(componentFunding.getAdjustmentType().getValue(), "Planned") || Objects.equals(componentFunding.getAdjustmentType().getValue(), "Actual"))) {
                            AmpComponentFundingTruWF ampComponentFundingTruWF = session.createQuery("FROM " + AmpComponentFundingTruWF.class.getName() + " act WHERE act.ampComponentFundingId= '" + componentFunding.getJustAnId() + "' AND act.ampComponentFundingId IS NOT NULL", AmpComponentFundingTruWF.class).stream().findAny().orElse(null);
                            if (ampComponentFundingTruWF == null) {//create new wfItem
                                CreateWorkFlowItemModel createWorkFlowItemModel = new CreateWorkFlowItemModel();
                                createWorkFlowItemModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                                CreateWorkFlowItemModel.Data data = new CreateWorkFlowItemModel.Data();
                                data.setProjectId(projectId);
                                data.setSubprojectId(subProjectId);
                                data.setAssignee(user);
                                List<CreateWorkFlowItemModel.Document> docs = new ArrayList<>();
//                                AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
                                HashSet<TemporaryComponentFundingDocument> newResources = ampAuthWebSession.getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(componentFunding.getJustAnId());
//                                newResources.addAll(s.getMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES).get(componentFunding.getJustAnId()));
                                if (newResources != null) {
                                    for (TemporaryComponentFundingDocument temporaryComponentFundingDocument : newResources) {
                                        try {
                                            CreateWorkFlowItemModel.Document doc = new CreateWorkFlowItemModel.Document();
                                            doc.setFileName(temporaryComponentFundingDocument.getFileName());
                                            doc.setId(UUID.randomUUID().toString());
                                            File file = temporaryComponentFundingDocument.getFile().writeToTempFile();

                                            byte[] fileContent = Files.readAllBytes(file.toPath());
                                            doc.setBase64(Base64.getEncoder().encodeToString(fileContent));
                                            docs.add(doc);
                                        } catch (Exception e) {
                                            logger.error("Error during workflow creation ", e);
                                        }


                                    }
                                    data.setDocuments(docs);
                                    newResources.clear();
                                }
                                data.setDescription(componentFunding.getDescription());
                                data.setDisplayName(componentFunding.getComponent().getTitle());
                                data.setAmount(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toPlainString());
                                data.setCurrency(componentFunding.getCurrency().getCurrencyCode());
                                data.setAmountType(Objects.equals(componentFunding.getAdjustmentType().getValue(), "Planned") ? "allocated" : "disbursed");
                                data.setBillingDate(convertToISO8601(componentFunding.getTransactionDate()));
                                data.setDueDate(convertToISO8601AndAddDays(componentFunding.getTransactionDate(), Integer.parseInt(getSettingValue(settings, "workFlowItemDueDays"))));//set approprite date
                                createWorkFlowItemModel.setData(data);
                                AmpComponentFundingTruWF ampComponentFundingTruWF1 = new AmpComponentFundingTruWF();
                                List<SubIntents> subIntents = getSubIntentsByMother("workflowitem");
                                try {
                                    GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/subproject.createWorkflowitem", createWorkFlowItemModel, CreateWorkFlowItemModel.class, CreateWFResponseModel.class, token)
                                            .subscribe(res -> {
                                                logger.info("Create WorkflowItem response: " + res);
                                                ampComponentFundingTruWF1.setAmpComponentFundingId(componentFunding.getJustAnId());
                                                ampComponentFundingTruWF1.setTruProjectId(projectId);
                                                ampComponentFundingTruWF1.setTruSubprojectId(subProjectId);
                                                ampComponentFundingTruWF1.setTruWFId(res.getData().getWorkflowitem().getId());
                                                try {
                                                    refreshSession();

                                                    // Perform database operations here
                                                    session.save(ampComponentFundingTruWF1);

                                                    session.flush();
                                                    transaction.commit();

                                                } catch (Exception e) {

                                                    e.printStackTrace();
                                                }
                                                subIntents.forEach(subIntent -> {
                                                    WFItemGrantRevokePermModel wfItemGrantRevokePermModel = new WFItemGrantRevokePermModel();
                                                    WFItemGrantRevokePermModel.Data data2 = new WFItemGrantRevokePermModel.Data();
                                                    data2.setProjectId(projectId);
                                                    data2.setSubprojectId(subProjectId);
                                                    data2.setIdentity(user);
                                                    data2.setWorkflowitemId(res.getData().getWorkflowitem().getId());
                                                    data2.setIntent(subIntent.getSubTruBudgetIntentName());
                                                    wfItemGrantRevokePermModel.setData(data2);
                                                    wfItemGrantRevokePermModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                                                    try {
                                                        GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/workflowitem.intent.grantPermission", wfItemGrantRevokePermModel, WFItemGrantRevokePermModel.class, String.class, token).subscribeOn(Schedulers.parallel()).subscribe(
                                                                response -> logger.info("Grant workflowitem permission response: " + response)
                                                        );
                                                    } catch (URISyntaxException e) {
                                                        throw new RuntimeException(e);
                                                    }

                                                });
                                                //workflow close
                                                try {
                                                    closeWorkFlowItem(componentFunding, settings, projectId, subProjectId, res.getData().getWorkflowitem().getId(), "Closed", token);
                                                } catch (URISyntaxException e) {
                                                    logger.info("Error when closing wf item ", e);
                                                    throw new RuntimeException(e);
                                                }

                                            });

                                } catch (Exception e) {
                                    logger.info("Error during subproject creation");
                                    e.printStackTrace();
                                }
                            } else {//update wfItem
                                //a WF item can only be edited if it's status is 'open'
                                EditWFItemModel editWFItemModel = new EditWFItemModel();
                                editWFItemModel.setApiVersion(getSettingValue(settings, "apiVersion"));
                                EditWFItemModel.Data data = new EditWFItemModel.Data();
                                data.setProjectId(projectId);
                                data.setSubprojectId(subProjectId);
                                data.setDescription(componentFunding.getDescription());
                                data.setWorkflowitemId(ampComponentFundingTruWF.getTruWFId());
                                data.setDisplayName(componentFunding.getComponent().getTitle());
                                data.setAmount(BigDecimal.valueOf(componentFunding.getTransactionAmount()).toPlainString());
                                data.setCurrency(componentFunding.getCurrency().getCurrencyCode());
                                data.setAmountType(Objects.equals(componentFunding.getAdjustmentType().getValue(), "Planned") ? "allocated" : "disbursed");
                                data.setBillingDate(convertToISO8601(componentFunding.getTransactionDate()));
                                data.setDueDate(convertToISO8601AndAddDays(componentFunding.getTransactionDate(), Integer.parseInt(getSettingValue(settings, "workFlowItemDueDays"))));//set approprite date
                                List<CreateWorkFlowItemModel.Document> docs = new ArrayList<>();
//                                AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
                                HashSet<TemporaryComponentFundingDocument> newResources = ampAuthWebSession.getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS).get(componentFunding.getJustAnId());
//                                newResources.addAll(s.getMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES).get(componentFunding.getJustAnId()));
                                if (newResources != null) {
                                    for (TemporaryComponentFundingDocument temporaryComponentFundingDocument : newResources) {
                                        try {
                                            CreateWorkFlowItemModel.Document doc = new CreateWorkFlowItemModel.Document();
                                            doc.setFileName(temporaryComponentFundingDocument.getFileName());
                                            File file = temporaryComponentFundingDocument.getFile().writeToTempFile();
                                            doc.setId(UUID.randomUUID().toString());

                                            byte[] fileContent = Files.readAllBytes(file.toPath());
                                            doc.setBase64(Base64.getEncoder().encodeToString(fileContent));
                                            docs.add(doc);
                                        } catch (Exception e) {
                                            logger.error("Error during workflow creation ", e);
                                        }


                                    }
                                    newResources.clear();
                                    data.setDocuments(docs);
                                }
                                editWFItemModel.setData(data);
                                GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/workflowitem.update", editWFItemModel, EditWFItemModel.class, String.class, token)
                                        .subscribe(res -> logger.info("Edit WFItem response: " + res));

                                try {
                                    closeWorkFlowItem(componentFunding, settings, projectId, subProjectId, ampComponentFundingTruWF.getTruWFId(), componentFunding.getComponentFundingStatus().getValue(), token);
                                } catch (URISyntaxException e) {
                                    logger.info("Error when closing wf item ", e);
                                    throw new RuntimeException(e);
                                }
                            }
                        }


                    }
                }
        }

    }
    public static void closeWorkFlowItem(AmpComponentFunding ampComponentFunding, List<AmpGlobalSettings> settings, String projectId, String subProjectId, String workFlowItemId, String rejectReason, String token) throws URISyntaxException {
        if (Objects.equals(ampComponentFunding.getComponentFundingStatusFormatted(), "closed") || Objects.equals(ampComponentFunding.getComponentFundingStatusFormatted(), "rejected")){
            //workflow close
            CloseWFItemModel closeWFItemModel = new CloseWFItemModel();
            closeWFItemModel.setApiVersion(getSettingValue(settings, "apiVersion"));
            if (ampComponentFunding.getComponentFundingStatus().getValue().equalsIgnoreCase("closed")) {
                CloseWFItemModel.Data data = new CloseWFItemModel.Data();
                data.setProjectId(projectId);
                data.setSubprojectId(subProjectId);
                data.setWorkflowitemId(workFlowItemId);
                closeWFItemModel.setData(data);
            }
            else {
                CloseWFItemModel.RejectData data = new CloseWFItemModel.RejectData();
                data.setProjectId(projectId);
                data.setSubprojectId(subProjectId);
                data.setWorkflowitemId(workFlowItemId);
                data.setRejectReason(ampComponentFunding.getComponentRejectReason());
                closeWFItemModel.setData(data);
            }

            GenericWebClient.postForSingleObjResponse(getSettingValue(settings, "baseUrl") + "api/workflowitem.close", closeWFItemModel, CloseWFItemModel.class, String.class, token)
                    .subscribe(res -> logger.info("WF close response: "+res));


        }
    }

    private static String convertToISO8601(Date date) {
        Instant instant = date.toInstant();
        return instant.toString();
    }

    private static String convertToISO8601AndAddDays(Date date, int daysToAdd) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newLocalDate = localDate.plusDays(daysToAdd);
         date = Date.from(newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Instant instant = date.toInstant();
        return instant.toString();
    }

    public static WorkflowItemDetailsModel getWFItemDetails(AmpComponentFundingTruWF ampComponentFundingTruWF) throws URISyntaxException {
        if (getSettingValue(getGlobalSettingsBySection("trubudget"),"isEnabled").equalsIgnoreCase("true")&& TeamUtil.getCurrentUser().getTruBudgetEnabled()) {
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        AbstractCache myCache = new EhCacheWrapper("trubudget");
        String token = (String) myCache.get("truBudgetToken");
        if (ampComponentFundingTruWF!=null) {
            return GenericWebClient.getForSingleObjResponse(getSettingValue(settings, "baseUrl") + String.format("api/workflowitem.viewDetails?projectId=%s&subprojectId=%s&workflowitemId=%s", ampComponentFundingTruWF.getTruProjectId(), ampComponentFundingTruWF.getTruSubprojectId(), ampComponentFundingTruWF.getTruWFId()), WorkflowItemDetailsModel.class, token)
                    .onErrorReturn(new WorkflowItemDetailsModel()).block();
        }
        }
        return null;
    }


}
