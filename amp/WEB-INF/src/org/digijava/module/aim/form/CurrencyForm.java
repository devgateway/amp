package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class CurrencyForm extends ActionForm {

	private Collection currency;
	private Collection pages;
	private Collection allCurrencies;
	private int numRecords;
	private Integer currentPage;
	private int page;
	
	private String filterByCurrency;
	
	private Long id;
	private String currencyCode;
	private String currencyName;
	private String countryIso;
	private Double exchangeRate;
	private String exchangeRateDate;
	private Collection countries;
	private String countryName;
	private boolean cantDelete = false;
	
	private String doAction;
	private String closeFlag;

	/**
	 * @return Returns the allCurrencies.
	 */
	public Collection getAllCurrencies() {
		return allCurrencies;
	}
	/**
	 * @param allCurrencies The allCurrencies to set.
	 */
	public void setAllCurrencies(Collection allCurrencies) {
		this.allCurrencies = allCurrencies;
	}
	/**
	 * @return Returns the currency.
	 */
	public Collection getCurrency() {
		return currency;
	}
	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(Collection currency) {
		this.currency = currency;
	}
	/**
	 * @return Returns the pages.
	 */
	public Collection getPages() {
		return pages;
	}
	/**
	 * @param pages The pages to set.
	 */
	public void setPages(Collection pages) {
		this.pages = pages;
	}
	/**
	 * @return Returns the numRecords.
	 */
	public int getNumRecords() {
		return numRecords;
	}
	/**
	 * @param numRecords The numRecords to set.
	 */
	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
	/**
	 * @return Returns the currentPage.
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage The currentPage to set.
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return Returns the filterByCurrency.
	 */
	public String getFilterByCurrency() {
		return filterByCurrency;
	}
	/**
	 * @param filterByCurrency The filterByCurrency to set.
	 */
	public void setFilterByCurrency(String filterByCurrency) {
		this.filterByCurrency = filterByCurrency;
	}
	/**
	 * @return Returns the page.
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page The page to set.
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return Returns the countries.
	 */
	public Collection getCountries() {
		return countries;
	}
	/**
	 * @param countries The countries to set.
	 */
	public void setCountries(Collection countries) {
		this.countries = countries;
	}
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return Returns the currencyName.
	 */
	public String getCurrencyName() {
		return currencyName;
	}
	/**
	 * @param currencyName The currencyName to set.
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	/**
	 * @return Returns the doAction.
	 */
	public String getDoAction() {
		return doAction;
	}
	/**
	 * @param doAction The doAction to set.
	 */
	public void setDoAction(String doAction) {
		this.doAction = doAction;
	}
	/**
	 * @return Returns the exchangeRate.
	 */
	public Double getExchangeRate() {
		return exchangeRate;
	}
	/**
	 * @param exchangeRate The exchangeRate to set.
	 */
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	/**
	 * @return Returns the exchangeRateDate.
	 */
	public String getExchangeRateDate() {
		return exchangeRateDate;
	}
	/**
	 * @param exchangeRateDate The exchangeRateDate to set.
	 */
	public void setExchangeRateDate(String exchangeRateDate) {
		this.exchangeRateDate = exchangeRateDate;
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the closeFlag.
	 */
	public String getCloseFlag() {
		return closeFlag;
}
	/**
	 * @param closeFlag The closeFlag to set.
	 */
	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}
	/**
	 * @return Returns the countryIso.
	 */
	public String getCountryIso() {
		return countryIso;
	}
    public String getCountryName() {
        return countryName;
    }

    /**
	 * @param countryIso The countryIso to set.
	 */
	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
	public boolean getCantDelete() {
		return cantDelete;
	}
	public void setCantDelete(boolean cantDelete) {
		this.cantDelete = cantDelete;
	}

}
