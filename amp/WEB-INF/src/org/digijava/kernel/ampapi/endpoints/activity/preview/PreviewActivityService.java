package org.digijava.kernel.ampapi.endpoints.activity.preview;

import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.common.util.DateTimeUtil;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PreviewActivityService {

    protected static final int PERCENTAGE_MULTIPLIER = 100;

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
                        ApiError.toError(PreviewActivityErrors.ACTIVITY_NOT_FOUND));
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

            activityFunding.setCurrency(currencyId);
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
            List<FundingInformationItem> allFundingItems = new ArrayList<>(f.getFundingDetails());
            allFundingItems.addAll(f.getMtefProjections());

            Map<Integer, Map<AmpCategoryValue, List<FundingInformationItem>>> groupedDetails = allFundingItems.stream()
                    .collect(groupingBy(FundingInformationItem::getTransactionType,
                            groupingBy(FundingInformationItem::getAdjustmentType)));

            List<PreviewFundingDetail> fundingDetails = new ArrayList<>();
            for (Entry<Integer, Map<AmpCategoryValue, List<FundingInformationItem>>> e : groupedDetails.entrySet()) {
                Integer transactionType = e.getKey();
                Map<AmpCategoryValue, List<FundingInformationItem>> fdMap = e.getValue();
                for (AmpCategoryValue cv : fdMap.keySet()) {
                    PreviewFundingDetail fundingDetail = new PreviewFundingDetail();

                    List<PreviewFundingTransaction> transactions = fdMap.get(cv).stream()
                            .map(fd -> generateFundingTransaction(fd, currencyCode))
                            .sorted(PreviewFundingTransactionComparator.getTransactionComparator())
                            .collect(Collectors.toList());

                    fundingDetail.setTransactionType(transactionType.longValue());
                    fundingDetail.setAdjustmentType(cv.getId());
                    fundingDetail.setTransactions(transactions);
                    fundingDetail.setSubtotal(calculateSubTotal(transactions));
                    fundingDetails.add(fundingDetail);
                }
            }

            previewFunding.setFundingDetails(fundingDetails);
            previewFunding.setDonorOrganizationId(f.getAmpDonorOrgId().getAmpOrgId());
            previewFunding.setFundingId(f.getAmpFundingId());
            previewFunding.setUndisbursedBalance(calculateUndisbursedBalance(fundingDetails));
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
        List<PreviewFundingDetail> allFundingDetails = previewFundings.stream()
                .map(PreviewFunding::getFundingDetails)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Map<Long, Map<Long, List<PreviewFundingDetail>>> groupedFundingDetails = allFundingDetails.stream()
                .collect(groupingBy(PreviewFundingDetail::getTransactionType,
                        groupingBy(PreviewFundingDetail::getAdjustmentType)));

        List<PreviewFundingTotal> totals = new ArrayList<>();

        for (Entry<Long, Map<Long, List<PreviewFundingDetail>>> e : groupedFundingDetails.entrySet()) {
            Long transactionTypeId = e.getKey();
            Map<Long, List<PreviewFundingDetail>> fdMap = e.getValue();
            for (Long adjustmentTypeId : fdMap.keySet()) {
                PreviewFundingTotal total = new PreviewFundingTotal();
                total.setTransactionType(transactionTypeId);
                total.setAdjustmentType(adjustmentTypeId);
                total.setAmount(calculateTotal(fdMap.get(adjustmentTypeId)));
                totals.add(total);
            }
        }

        return totals;
    }

    /**
     * Delviery Rate = Total Actual Disbursements / Total Actual Commitments * 100;
     *
     * @param totals
     * @return delivery rate
     */
    private Double calculateDeliveryRate(List<PreviewFundingTotal> totals) {
        Long actualCategoryValueId = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();

        Double totalActualCommitments = totals.stream()
                .filter(fd -> fd.getTransactionType().equals((long) Constants.COMMITMENT)
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .collect(Collectors.summingDouble(PreviewFundingTotal::getAmount));

        Double totalActualDisbursements = totals.stream()
                .filter(fd -> fd.getTransactionType().equals((long) Constants.DISBURSEMENT)
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .collect(Collectors.summingDouble(PreviewFundingTotal::getAmount));

        Double deliveryRate = null;

        if (totalActualCommitments != 0 && totalActualDisbursements != 0) {
            deliveryRate = totalActualDisbursements / totalActualCommitments * PERCENTAGE_MULTIPLIER;
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
        Long actualCategoryValueId = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();
        Double totalActualCommitments = totals.stream()
                .filter(fd -> fd.getTransactionType().equals((long) Constants.COMMITMENT)
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .collect(Collectors.summingDouble(PreviewFundingTotal::getAmount));

        Double totalActualDisbursements = totals.stream()
                .filter(fd -> fd.getTransactionType().equals((long) Constants.DISBURSEMENT)
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .collect(Collectors.summingDouble(PreviewFundingTotal::getAmount));

        return totalActualCommitments != 0 || totalActualDisbursements != 0
                ? totalActualCommitments - totalActualDisbursements : null;
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
        transaction.setTransactionAmount(convertedAmount);
        transaction.setTransactionDate(InterchangeUtils.formatISO8601Date(fd.getTransactionDate()));
        transaction.setReportingDate(InterchangeUtils.formatISO8601Date(fd.getReportingDate()));

        return transaction;
    }

    private Double calculateSubTotal(List<PreviewFundingTransaction> transactions) {
        return transactions.stream().collect(Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount));
    }

    private Double calculateTotal(List<PreviewFundingDetail> fundingDetails) {
        return fundingDetails.stream().collect(Collectors.summingDouble(PreviewFundingDetail::getSubtotal));
    }

    /**
     * Undisbursed Balance = Total Actual Commitments - Total Actual Disbursements
     *
     * @param fundingDetails
     * @return undisbursed balance
     */
    private Double calculateUndisbursedBalance(List<PreviewFundingDetail> fundingDetails) {
        Long actualCategoryValueId = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase();
        Double totalActualCommitments = fundingDetails.stream()
                .filter(fd -> fd.getTransactionType().equals(Long.valueOf(Constants.COMMITMENT))
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .map(PreviewFundingDetail::getTransactions)
                .flatMap(List::stream)
                .collect(Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount));

        Double totalActualDisbursements = fundingDetails.stream()
                .filter(fd -> fd.getTransactionType().equals(Long.valueOf(Constants.DISBURSEMENT))
                        && fd.getAdjustmentType().equals(actualCategoryValueId))
                .map(PreviewFundingDetail::getTransactions)
                .flatMap(List::stream)
                .collect(Collectors.summingDouble(PreviewFundingTransaction::getTransactionAmount));

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

}
