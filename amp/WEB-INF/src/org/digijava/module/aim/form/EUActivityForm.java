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
	
	private List contrAmountList;
	private List contrCurrIdList;
	private List contrDonorIdList;
	private List contrDonorNameList;
	private List contrFinInstrIdList;
	private List contrFinTypeIdList;
	
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
		
		contrAmountList=new ArrayList();
		contrAmountList.add(new String(""));

		contrCurrIdList=new ArrayList();
		contrCurrIdList.add(new String("-1"));
		
		contrDonorIdList=new ArrayList();
		contrDonorIdList.add(new String("-1"));
		
		contrFinInstrIdList=new ArrayList();
		contrFinInstrIdList.add(new Long(-1));
		
		contrFinTypeIdList=new ArrayList();
		contrFinTypeIdList.add(new Long(-1));
		
		contrDonorNameList=new ArrayList();
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
		 contrAmountList = new ArrayList();
		 contrCurrIdList = new ArrayList();
		 contrDonorIdList = new ArrayList();
		 contrFinInstrIdList = new ArrayList();
		 contrFinTypeIdList = new ArrayList();
		 contrDonorNameList=new ArrayList();

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

	public List getContrAmountList() {
		return contrAmountList;
	}

	public void setContrAmountList(List contrAmountList) {
		this.contrAmountList = contrAmountList;
	}

	public List getContrCurrIdList() {
		return contrCurrIdList;
	}

	public void setContrCurrIdList(List contrCurrIdList) {
		this.contrCurrIdList = contrCurrIdList;
	}

	public List getContrDonorIdList() {
		return contrDonorIdList;
	}

	public void setContrDonorIdList(List contrDonorIdList) {
		this.contrDonorIdList = contrDonorIdList;
	}

	public List getContrFinInstrIdList() {
		return contrFinInstrIdList;
	}

	public void setContrFinInstrIdList(List contrFinInstrIdList) {
		this.contrFinInstrIdList = contrFinInstrIdList;
	}

	public List getContrFinTypeIdList() {
		return contrFinTypeIdList;
	}

	public void setContrFinTypeIdList(List contrFinTypeIdList) {
		this.contrFinTypeIdList = contrFinTypeIdList;
	}

	public Integer getEditingIndexId() {
		return editingIndexId;
	}

	public void setEditingIndexId(Integer editingIndexId) {
		this.editingIndexId = editingIndexId;
	}

	public List getContrDonorNameList() {
		return contrDonorNameList;
	}

	public void setContrDonorNameList(List contrDonorNameList) {
		this.contrDonorNameList = contrDonorNameList;
	}

	public Object[] getContrDonorName() {
		return contrDonorName;
	}

	public void setContrDonorName(Object[] contrDonorName) {
		this.contrDonorName = contrDonorName;
	}

}
