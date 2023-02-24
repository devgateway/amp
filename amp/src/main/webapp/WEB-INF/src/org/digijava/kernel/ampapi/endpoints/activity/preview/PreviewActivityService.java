package org.digijava.kernel.ampapi.endpoints.activity.preview;

import static java.util.stream.Collectors.groupingBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.preview.regional.PreviewRegionalFundingItem;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.message.helper.AmpMessageWorker;
import org.digijava.module.message.helper.Team;

/**
 * @author Viorel Chihai
 */
public final class PreviewActivityService {

    protected static final int PERCENTAGE_MULTIPLIER = 100;
    private static final int TEAM_NAME_INDEX = 3;

    private static PreviewActivityService previewActivityService;

    private PreviewActivityService() {
    }

    public static PreviewActivityService getInstance() {
        if (previewActivityService == null) {
            previewActivityService = new PreviewActivityService();
        }

        return previewActivityService;
    }

    public PreviewActivityFunding getPreviewActivityFunding(Long activityId, Long currencyId) {
        PreviewActivityFunding activityFunding = new PreviewActivityFunding();
        try {
            AmpActivityVersion activity = ActivityUtil.loadActivity(activityId);

            if (activity == null) {
                throw new ApiRuntimeException(Response.Status.BAD_REQUEST,
                        ApiError.toError(ActivityErrors.ACTIVITY_NOT_FOUND));
            }
            AmpCurrency currency = null;
            if (currencyId != null) {
                currency = CurrencyUtil.getAmpcurrency(currencyId);
            }

            if (currencyId == null || currency == null) {
                throw new ApiRuntimeException(Response.Status.BAD_REQUEST,
                        ApiError.toError(PreviewActivityErrors.CURRENCY_NOT_FOUND));
            }

            String currencyCode = currency.getCurrencyCode();
            AmpFundingAmount ppcAmount = activity.getProjectCostByType(AmpFundingAmount.FundingType.PROPOSED);
            AmpFundingAmount rpcAmount = activity.getProjectCostByType(AmpFundingAmount.FundingType.REVISED);

            if (ppcAmount != null) {
                activityFunding.setPpcAmount(convertProjectCostAmount(ppcAmount, currencyCode));
            }
            if (rpcAmount != null) {
                activityFunding.setRpcAmount(convertProjectCostAmount(rpcAmount, currencyCode));
            }

            activityFunding.setFundingInformation(getFundingInformation(activity, currencyCode));

            activityFunding.setCurrency(currency.getCurrencyCode());

            this.populateRegionalFunding(activityFunding, activity, currency);

        } catch (DgException e) {
            throw new RuntimeException(e);
        }

        return activityFunding;
    }

    private PreviewFundingInformation getFundingInformation(AmpActivityVersion activity, String currencyCode) {
        PreviewFundingInformation fundingInformation = new PreviewFundingInformation();

        List<PreviewFunding> previewFundings = new ArrayList<>();
        for (AmpFunding f : activity.getFunding()) {
            PreviewFunding previewFunding = new PreviewFunding();
            Map<String, List<PreviewFundingTransaction>> fundingItemTransactions = new HashMap<>();
            List<FundingInformationItem> allFundingItems = new ArrayList<>(f.getFundingDetails());
            allFundingItems.addAll(f.getMtefProjections());

            Map<Integer, Map<AmpCategoryValue, List<FundingInformationItem>>> groupedDetails = allFundingItems.stream()
                    .collect(groupingBy(FundingInformationItem::getTransactionType,
                            groupingBy(FundingInformationItem::getAdjustmentType)));

            for (Entry<Integer, Map<AmpCategoryValue, List<FundingInformationItem>>> e : groupedDetails.entrySet()) {
                Integer transactionType = e.getKey();
                Map<AmpCategoryValue, List<FundingInformationItem>> fdMap = e.getValue();
                for (AmpCategoryValue cv : fdMap.keySet()) {
                    List<PreviewFundingTransaction> transactions = fdMap.get(cv).stream()
                            .map(fd -> generateFundingTransaction(fd, currencyCode))
                            .sorted(PreviewFundingTransactionComparator.getTransactionComparator())
                            .collect(Collectors.toList());

                    List<PreviewFundingTransaction> previewTransactions =
                            fundingItemTransactions.get(FieldMap.underscorify(ArConstants.TRANSACTION_ID_TO_TYPE_NAME.
                                    get(transactionType)));
                    if (previewTransactions == null) {
                        previewTransactions = new ArrayList<>();
                    }
                    previewTransactions.addAll(transactions);
                    fundingItemTransactions.put(FieldMap.underscorify(ArConstants.TRANSACTION_ID_TO_TYPE_NAME.
                            get(transactionType)), previewTransactions);
                }
            }
            previewFunding.setDonorOrganizationId(f.getAmpDonorOrgId().getAmpOrgId());
            previewFunding.setFundingId(f.getAmpFundingId());
            previewFunding.setUndisbursedBalance(calculateUndisbursedBalance(fundingItemTransactions));
            previewFunding.setTransactions(fundingItemTransactions);
            previewFundings.add(previewFunding);

        }
        fundingInformation.setFundings(previewFundings);
        fundingInformation.setTotals(calculateTotals(previewFundings));
        fundingInformation.setDeliveryRate(calculateDeliveryRate(fundingInformation.getTotals()));
        fundingInformation.setUndisbursedBalance(calculateTotalsUndisbursedBalance(fundingInformation.getTotals()));

        return fundingInformation;
    }

    /**
     * Calculate totals for transactions grouped by transaction type and adjustment type
     *
     * @param previewFundings
     * @return
     */

    private List<PreviewFundingTotal> calculateTotals(List<PreviewFunding> previewFundings) {

        Optional<Map<String, List<PreviewFundingTransaction>>> allTransactionsByTypeAndAdjustment =
                previewFundings.stream().map(PreviewFunding::getTransactions).
                        collect(Collectors.toList()).stream().reduce((firstMap, secondMap)
                        -> Stream.concat(firstMap.entrySet().stream(), secondMap.entrySet().stream())
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                                (countInFirstMap, countInSecondMap) -> {
                                    countInFirstMap.addAll(countInSecondMap);
                                    return countInFirstMap;
                                }
                        )));
        List<PreviewFundingTotal> totals = new ArrayList<>();
        if (allTransactionsByTypeAndAdjustment.isPresent()) {
            allTransactionsByTypeAndAdjustment.get().forEach((transactionType, previewFundingTransactions) -> {
                Map<Long, Double> transactionTotalByAdjustmentType =
                        previewFundingTransactions.stream().collect(Collectors.
                                groupingBy(PreviewFundingTransaction::getAdjustmentType,
                                        Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount)
                                ));

                transactionTotalByAdjustmentType.forEach((adjustmentType, totalAmount) -> {

                    PreviewFundingTotal total = new PreviewFundingTotal();
                    total.setTransactionType(transactionType);
                    total.setAdjustmentType(adjustmentType);
                    total.setAmount(totalAmount);
                    totals.add(total);
                });
            });
        }
        return totals;
    }

    private ImmutablePair<Double, Double> getTotalActualCommitmentDisbursement(List totals) {
        Long actualCategoryValueId = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();

        Double totalActualCommitments = calculateTotal(totals, actualCategoryValueId,
                ArConstants.COMMITMENT.toLowerCase());

        Double totalActualDisbursements = calculateTotal(totals, actualCategoryValueId,
                ArConstants.DISBURSEMENT.toLowerCase());
        return new ImmutablePair(totalActualCommitments, totalActualDisbursements);
    }

    /**
     * Delviery Rate = Total Actual Disbursements / Total Actual Commitments * 100;
     *
     * @param totals
     * @return delivery rate
     */
    private Double calculateDeliveryRate(List<PreviewFundingTotal> totals) {

        ImmutablePair<Double, Double> totalActualCommitmentsDisbursements =
                getTotalActualCommitmentDisbursement(totals);
        Double deliveryRate = null;

        if (totalActualCommitmentsDisbursements.k != 0 && totalActualCommitmentsDisbursements.v != 0) {
            deliveryRate = totalActualCommitmentsDisbursements.v / totalActualCommitmentsDisbursements.k
                    * PERCENTAGE_MULTIPLIER;
        }

        return deliveryRate;
    }

    /**
     * Undisbursed Balance = Total Actual Commitments - Total Actual Disbursements
     *
     * @param totals
     * @return undisbursed balance
     */
    private Double calculateTotalsUndisbursedBalance(List<PreviewFundingTotal> totals) {
        ImmutablePair<Double, Double> totalActualCommitmentsDisbursements =
                getTotalActualCommitmentDisbursement(totals);

        return totalActualCommitmentsDisbursements.k != 0 || totalActualCommitmentsDisbursements.v != 0
                ? totalActualCommitmentsDisbursements.k - totalActualCommitmentsDisbursements.v : null;
    }

    private Double calculateTotal(List<PreviewFundingTotal> totals, Long actualCategoryValueId, String commitment) {
        return totals.stream()
                .filter(fd -> fd.getTransactionType().equals(commitment)
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .collect(Collectors.summingDouble(PreviewFundingTotal::getAmount));
    }


    private PreviewFundingTransaction generateFundingTransaction(FundingInformationItem fd, String currencyCode) {
        Double amount = fd.getTransactionAmount();
        String fromCurrencyCode = fd.getAmpCurrencyId().getCurrencyCode();
        LocalDate transactionDate = DateTimeUtil.getLocalDate(fd.getTransactionDate());
        Double fixedRate = fd.getFixedExchangeRate();

        Double convertedAmount = AmpCurrencyConvertor.getInstance()
                .convertAmount(amount, fromCurrencyCode, currencyCode, fixedRate, transactionDate);

        PreviewFundingTransaction transaction = new PreviewFundingTransaction();
        transaction.setTransactionId(fd.getDbId());
        transaction.setAdjustmentType(fd.getAdjustmentType().getId());
        transaction.setTransactionType(fd.getTransactionType().longValue());
        transaction.setTransactionAmount(convertedAmount);
        transaction.setTransactionDate(fd.getTransactionDate());
        transaction.setReportingDate(fd.getReportingDate());

        return transaction;
    }

    /**
     * Undisbursed Balance = Total Actual Commitments - Total Actual Disbursements
     *
     * @param transactions
     * @return undisbursed balance
     */
    private Double calculateUndisbursedBalance(Map<String, List<PreviewFundingTransaction>> transactions) {

        Long actualCategoryValueId = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();

        Double totalActualCommitments = transactions.getOrDefault(ArConstants.COMMITMENT.toLowerCase(),
                Collections.emptyList()).stream().filter(t -> t.getAdjustmentType().equals(actualCategoryValueId)).
                collect(Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount));

        Double totalActualDisbursements = transactions.getOrDefault(ArConstants.DISBURSEMENT.toLowerCase(),
                Collections.emptyList()).stream().filter(t -> t.getAdjustmentType().equals(actualCategoryValueId)).
                collect(Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount));

        return totalActualCommitments != 0 || totalActualDisbursements != 0
                ? totalActualCommitments - totalActualDisbursements : null;
    }

    private Double convertProjectCostAmount(AmpFundingAmount projectCost, String currencyCode) {
        //as from the Activity form settings you can end up with nulls either in dates
        //or in amounts.
        if (projectCost.getFunDate() == null && projectCost.getFunAmount() != null) {
            return currencyCode.equals(projectCost.getCurrencyCode()) ? projectCost.getFunAmount() : 0D;
        } else {
            if (projectCost.getFunDate() != null && projectCost.getFunAmount() != null) {
                Double costAmount = projectCost.getFunAmount();
                String costCurrencyCode = projectCost.getCurrencyCode();
                LocalDate costDate = DateTimeUtil.getLocalDate(projectCost.getFunDate());

                return AmpCurrencyConvertor.getInstance().
                        convertAmount(costAmount, costCurrencyCode, currencyCode, costDate);
            } else {
                return 0D;
            }
        }
    }

    public List<PreviewWorkspace> getWorkspaces(Long activityId) {
        List<PreviewWorkspace> previewWorkspaces = new ArrayList<>();

        try {
            AmpActivityVersion activity = ActivityUtil.loadActivity(activityId);
            AmpTeam ownerTeam = activity.getTeam();
            String ownerInfo = TranslatorWorker.translateText("Workspace where the activity was created");
            previewWorkspaces.add(new PreviewWorkspace(ownerTeam.getName(), PreviewWorkspace.Type.TEAM, ownerInfo));

            AmpTeam parentTeam = ownerTeam.getParentTeamId();
            String path = ownerTeam.getName();

            Set<Long> idStack = new HashSet<>();
            while (parentTeam != null && !idStack.contains(parentTeam.getAmpTeamId())) {
                path = String.format("%s -> %s", path, parentTeam.getName());
                previewWorkspaces.add(new PreviewWorkspace(parentTeam.getName(),
                        PreviewWorkspace.Type.MANAGEMENT, path));

                idStack.add(parentTeam.getAmpTeamId());
                parentTeam = parentTeam.getParentTeamId();
            }

            List<AmpTeam> computedTeams = TeamUtil.getAllTeams().stream()
                    .filter(t -> Boolean.TRUE.equals(t.getComputation()) || Boolean.TRUE.equals(t.getUseFilter()))
                    .filter(t -> t.getAmpTeamId() != ownerTeam.getAmpTeamId())
                    .collect(Collectors.toList());

            final StringBuffer wsQueries = new StringBuffer();
            for (AmpTeam team : computedTeams) {
                String wsQuery = WorkspaceFilter.generateWorkspaceFilterQueryForTeam(team.getAmpTeamId());

                if (wsQueries.length() > 0) {
                    wsQueries.append(" UNION ");
                }

                wsQueries.append(AmpMessageWorker.addTeamIdToQuery(wsQuery, team.getAmpTeamId(), team.getName()));
            }

            final Map<Long, List<Team>> activityTeams = new HashMap<>();

            PersistenceManager.getSession().doWork(conn -> {
                RsInfo teamsInActivityQuery = SQLUtils.rawRunQuery(conn, wsQueries.toString(), null);
                while (teamsInActivityQuery.rs.next()) {
                    // activityTeams
                    Long ampActivityId = teamsInActivityQuery.rs.getLong(1);
                    if (activityTeams.get(ampActivityId) == null) {
                        activityTeams.put(ampActivityId, new ArrayList<>());
                    }
                    activityTeams.get(ampActivityId).add(
                            new Team(teamsInActivityQuery.rs.getLong(2),
                                    teamsInActivityQuery.rs.getString(TEAM_NAME_INDEX)));
                }
                teamsInActivityQuery.close();
            });

            if (activityTeams.containsKey(activityId)) {
                for (Team team : activityTeams.get(activityId)) {
                    previewWorkspaces.add(new PreviewWorkspace(team.getTeamName(), PreviewWorkspace.Type.COMPUTED));
                }
            }

        } catch (DgException e) {
            throw new RuntimeException(e);
        }

        return previewWorkspaces;
    }

    private void populateRegionalFunding(PreviewActivityFunding activityFunding, AmpActivityVersion activity,
                                         AmpCurrency currencyTo) {
        Set<AmpRegionalFunding> rg = activity.getRegionalFundings();

        Map<Integer, List<AmpRegionalFunding>> regionalFundingPerTransaction = rg.stream()
                .collect(groupingBy(AmpRegionalFunding::getTransactionType));
        //Populate

        activityFunding.getRegionalCommitments().addAll(
                getAmpRegionalFundings(regionalFundingPerTransaction, Constants.COMMITMENT)
                        .stream().map(ampRegionalFunding ->
                        generateRegionalFundingTransaction(ampRegionalFunding, currencyTo)).
                        collect(Collectors.toList()));
        activityFunding.getRegionalDisbursements().addAll(
                getAmpRegionalFundings(regionalFundingPerTransaction, Constants.DISBURSEMENT)
                        .stream().map(ampRegionalFunding ->
                        generateRegionalFundingTransaction(ampRegionalFunding, currencyTo)).
                        collect(Collectors.toList()));
        activityFunding.getRegionalExpenditures().addAll(
                getAmpRegionalFundings(regionalFundingPerTransaction, Constants.EXPENDITURE).
                        stream().map(ampRegionalFunding ->
                        generateRegionalFundingTransaction(ampRegionalFunding, currencyTo)).
                        collect(Collectors.toList()));

    }

    private List<AmpRegionalFunding> getAmpRegionalFundings(Map<Integer,
            List<AmpRegionalFunding>> regionalFundingPerTransaction, Integer transactionType) {
        return Optional.ofNullable(regionalFundingPerTransaction.get(transactionType)).orElse(Collections.emptyList());
    }

    private PreviewRegionalFundingItem generateRegionalFundingTransaction(AmpRegionalFunding ampRegionalFunding,
                                                                          AmpCurrency currencyTo) {
        PreviewRegionalFundingItem pfr = new PreviewRegionalFundingItem();
        LocalDate transactionDate = DateTimeUtil.getLocalDate(ampRegionalFunding.getTransactionDate());
        pfr.setCurrency(currencyTo);
        pfr.setId(ampRegionalFunding.getAmpRegionalFundingId());
        pfr.setAdjustmentType(ampRegionalFunding.getAdjustmentType().getId());
        pfr.setRegionLocation(ampRegionalFunding.getRegionLocation().getId());
        pfr.setTransactionDate(ampRegionalFunding.getTransactionDate());
        pfr.setTransactionAmount(AmpCurrencyConvertor.getInstance().convertAmount(ampRegionalFunding.
                        getTransactionAmount(), ampRegionalFunding.getCurrency().getCurrencyCode(),
                        currencyTo.getCurrencyCode(), transactionDate));
        return pfr;
    }
}
