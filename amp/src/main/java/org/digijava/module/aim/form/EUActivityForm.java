/**
 * 
 */
package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @author mihai
 *
 */
public class EUActivityForm extends ActionForm implements Serializable {

	private Long id;
	private Integer editingIndexId;
	private String name;
	private String textId;
	private String inputs;
	private String totalCost;
	private Long totalCostCurrencyId;
	
	private Object[] contrAmount;
	private Object[] contrCurrId;
	private Object[] contrDonorId;
	private Object[] contrDonorName;
	private Long[] contrFinInstrId;
	private Long[] contrFinTypeId;
	
	private List<String> contrAmountList;
	private List<String> contrCurrIdList;
	private List<String> contrDonorIdList;
	private List<String> contrDonorNameList;
	private List<Long> contrFinInstrIdList;
	private List<Long> contrFinTypeIdList;
	
	private String assumptions;
	private String progress;
	private String dueDate;
	private Collection currencies;
	private Collection finInstrs;
	private Collection finTypes;
	private Collection donors;
	private String[] deleteContrib;
	

	
	
	public String[] getDeleteContrib() {
		return deleteContrib;
	}

	public void setDeleteContrib(String[] deleteContrib) {
		this.deleteContrib = deleteContrib;
	}

	public Collection getDonors() {
		return donors;
	}

	public void setDonors(Collection donors) {
		this.donors = donors;
	}

	public Collection getFinInstrs() {
		return finInstrs;
	}

	public void setFinInstrs(Collection finInstrs) {
		this.finInstrs = finInstrs;
	}

	public Collection getFinTypes() {
		return finTypes;
	}

	public void setFinTypes(Collection finTypes) {
		this.finTypes = finTypes;
	}

	public Object[] getContrAmount() {
		return contrAmount;
	}

	public void setContrAmount(Object[] contrAmount) {
		this.contrAmount = contrAmount;
	}

	public void clear() {
		editingIndexId=null;
		id=null;
		name=null;
		textId=null;
		inputs=null;
		totalCostCurrencyId=null;
		totalCost=null;
		contrAmount=null;
		contrCurrId=null;
		assumptions=null;
		progress=null;
		dueDate=null;
		
		contrAmountList=new ArrayList<String>();
		contrAmountList.add("");

		contrCurrIdList=new ArrayList<String>();
		contrCurrIdList.add("-1");
		
		contrDonorIdList=new ArrayList<String>();
		contrDonorIdList.add("-1");
		
		contrFinInstrIdList=new ArrayList<Long>();
		contrFinInstrIdList.add(new Long(-1));
		
		contrFinTypeIdList=new ArrayList<Long>();
		contrFinTypeIdList.add(new Long(-1));
		
		contrDonorNameList=new ArrayList<String>();
		contrDonorNameList.add("");
	}
	
	public Collection getCurrencies() {
		return currencies;
	}


	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}


	public String getAssumptions() {
		return assumptions;
	}


	public void setAssumptions(String assumptions) {
		this.assumptions = assumptions;
	}
	

	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getInputs() {
		return inputs;
	}


	public void setInputs(String inputs) {
		this.inputs = inputs;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getProgress() {
		return progress;
	}


	public void setProgress(String progress) {
		this.progress = progress;
	}


	public String getTextId() {
		return textId;
	}


	public void setTextId(String textId) {
		this.textId = textId;
	}


	public String getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}


	public Long getTotalCostCurrencyId() {
		return totalCostCurrencyId;
	}


	public void setTotalCostCurrencyId(Long totalCostCurrency) {
		this.totalCostCurrencyId = totalCostCurrency;
	}


	/**
	 * 
	 */
	public EUActivityForm() {
		super();
		 contrAmountList = new ArrayList<String>();
		 contrCurrIdList = new ArrayList<String>();
		 contrDonorIdList = new ArrayList<String>();
		 contrFinInstrIdList = new ArrayList<Long>();
		 contrFinTypeIdList = new ArrayList<Long>();
		 contrDonorNameList=new ArrayList<String>();

	}



	public Object[] getContrCurrId() {
		return contrCurrId;
	}


	public void setContrCurrId(Object[] contrCurrId) {
		this.contrCurrId = contrCurrId;
	}

	public Object[] getContrDonorId() {
		return contrDonorId;
	}

	public void setContrDonorId(Object[] contrDonorId) {
		this.contrDonorId = contrDonorId;
	}

	public Long[] getContrFinInstrId() {
		return contrFinInstrId;
	}

	public void setContrFinInstrId(Long[] contrFinInstr) {
		this.contrFinInstrId = contrFinInstr;
	}

	public Long[] getContrFinTypeId() {
		return contrFinTypeId;
	}

	public void setContrFinTypeId(Long[] contrFinType) {
		this.contrFinTypeId = contrFinType;
	}

	public List<String> getContrAmountList() {
		return contrAmountList;
	}

	public void setContrAmountList(List<String> contrAmountList) {
		this.contrAmountList = contrAmountList;
	}

	public List<String> getContrCurrIdList() {
		return contrCurrIdList;
	}

	public void setContrCurrIdList(List<String> contrCurrIdList) {
		this.contrCurrIdList = contrCurrIdList;
	}

	public List<String> getContrDonorIdList() {
		return contrDonorIdList;
	}

	public void setContrDonorIdList(List<String> contrDonorIdList) {
		this.contrDonorIdList = contrDonorIdList;
	}

	public List<Long> getContrFinInstrIdList() {
		return contrFinInstrIdList;
	}

	public void setContrFinInstrIdList(List<Long> contrFinInstrIdList) {
		this.contrFinInstrIdList = contrFinInstrIdList;
	}

	public List<Long> getContrFinTypeIdList() {
		return contrFinTypeIdList;
	}

	public void setContrFinTypeIdList(List<Long> contrFinTypeIdList) {
		this.contrFinTypeIdList = contrFinTypeIdList;
	}

	public Integer getEditingIndexId() {
		return editingIndexId;
	}

	public void setEditingIndexId(Integer editingIndexId) {
		this.editingIndexId = editingIndexId;
	}

	public List<String> getContrDonorNameList() {
		return contrDonorNameList;
	}

	public void setContrDonorNameList(List<String> contrDonorNameList) {
		this.contrDonorNameList = contrDonorNameList;
	}

	public Object[] getContrDonorName() {
		return contrDonorName;
	}

	public void setContrDonorName(Object[] contrDonorName) {
		this.contrDonorName = contrDonorName;
	}

}
