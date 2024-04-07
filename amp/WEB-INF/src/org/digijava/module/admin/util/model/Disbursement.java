package org.digijava.module.admin.util.model;

public class Disbursement {
    private Long transaction_id;
    private Long adjustment_type;
    private String transaction_date;
    private String reporting_date;
    private String updated_date;
    private double transaction_amount;
    private Long currency;
    private Object fixed_exchange_rate;

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Long getAdjustment_type() {
        return adjustment_type;
    }

    public void setAdjustment_type(Long adjustment_type) {
        this.adjustment_type = adjustment_type;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getReporting_date() {
        return reporting_date;
    }

    public void setReporting_date(String reporting_date) {
        this.reporting_date = reporting_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public Object getFixed_exchange_rate() {
        return fixed_exchange_rate;
    }

    public void setFixed_exchange_rate(Object fixed_exchange_rate) {
        this.fixed_exchange_rate = fixed_exchange_rate;
    }

    // Getters and setters
}
