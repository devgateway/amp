package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ViewAhSurveyFormulasForm extends ActionForm {
    private Long indId;
    private String indCode;
    private String action;

    private Long formulaId;
    private Long selectedColumnIndex;
    private String formulaText;
    private String baseLineValue;
    private String targetValue;
    private Collection columns;
    private Long columnIndex;
    private boolean formulaEnabled;
    private String constantName;

    private Collection ahsurveyIndicatorCalcFormulas;

    public ViewAhSurveyFormulasForm() {
    }

    public Collection getAhsurveyIndicatorCalcFormulas() {
        return ahsurveyIndicatorCalcFormulas;
    }

    public String getAction() {
        return action;
    }

    public Long getColumnIndex() {
        return columnIndex;
    }

    public Collection getColumns() {
        return columns;
    }

    public String getConstantName() {
        return constantName;
    }

    public Long getFormulaId() {
        return formulaId;
    }

    public String getFormulaText() {
        return formulaText;
    }

    public String getIndCode() {
        return indCode;
    }

    public Long getIndId() {
        return indId;
    }

    public Long getSelectedColumnIndex() {
        return selectedColumnIndex;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public String getBaseLineValue() {
        return baseLineValue;
    }

    public boolean isFormulaEnabled() {
        return formulaEnabled;
    }

    public void setAhsurveyIndicatorCalcFormulas(Collection ahsurveyIndicatorCalcFormulas) {
        this.ahsurveyIndicatorCalcFormulas = ahsurveyIndicatorCalcFormulas;
    }

    public void setFormulaText(String formulaText) {
        this.formulaText = formulaText;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public void setColumns(Collection columns) {
        this.columns = columns;
    }

    public void setColumnIndex(Long columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setIndCode(String indCode) {
        this.indCode = indCode;
    }

    public void setIndId(Long indId) {
        this.indId = indId;
    }

    public void setSelectedColumnIndex(Long selectedColumnIndex) {
        this.selectedColumnIndex = selectedColumnIndex;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public void setBaseLineValue(String BaseLineValue) {
        this.baseLineValue = BaseLineValue;
    }

    public void setFormulaEnabled(boolean formulaEnabled) {
        this.formulaEnabled = formulaEnabled;
    }
}
