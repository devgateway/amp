package org.digijava.module.translation.form;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.entity.Message;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.util.ListChangesBuffer.ChangedItem;

import java.util.List;

/**
 * New Advanced Mode form bean.
 * @author Irakli Kobiashvili
 *
 */
public class NewAdvancedTrnForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private String searchTerm;
    private List<MessageGroup> resultList;
    private Integer pageNumber;
    private Integer totalPages;
    private String[] key;
    private String[] locale;
    private String[] message;
    private List<ChangedItem<String, Message>> changesList;
    private Boolean changesOpened;
    private String[] possibleLocales;
    private String addKey;
    private String addLocale;
    private String addMessage;
    private String[] undoChanges;
    private String updateKey;
    private String updateLocale;
    private String updateMessage;
    private String selectedLocale;
    private List<org.digijava.kernel.entity.Locale> languages;
    private String action;
    private int itemsPerPage;
    private boolean showOnlyEnglish;

    public boolean isShowOnlyEnglish() {
        return showOnlyEnglish;
    }

    public void setShowOnlyEnglish(boolean showOnlyEnglish) {
        this.showOnlyEnglish = showOnlyEnglish;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<org.digijava.kernel.entity.Locale> getLanguages() {
        return languages;
    }

    public void setLanguages(List<org.digijava.kernel.entity.Locale> languages) {
        this.languages = languages;
    }

    public String getSelectedLocale() {
        return selectedLocale;
    }

    public void setSelectedLocale(String selectedLocale) {
        this.selectedLocale = selectedLocale;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setResultList(List<MessageGroup> resultList) {
        this.resultList = resultList;
    }
    
    public List<MessageGroup> getResultList() {
        return resultList;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public String[] getKey() {
        return key;
    }

    public void setLocale(String locale[]) {
        this.locale = locale;
    }

    public String[] getLocale() {
        return locale;
    }

    public void setMessage(String message[]) {
        this.message = message;
    }

    public String[] getMessage() {
        return message;
    }

    public void setChangesList(List<ChangedItem<String, Message>> changesList) {
        this.changesList = changesList;
    }

    public List<ChangedItem<String, Message>> getChangesList() {
        return changesList;
    }

    public void setChangesOpened(Boolean changesOpened) {
        this.changesOpened = changesOpened;
    }

    public Boolean getChangesOpened() {
        return changesOpened;
    }

    public void setPossibleLocales(String[] possibleLocales) {
        this.possibleLocales = possibleLocales;
    }

    public String[] getPossibleLocales() {
        return possibleLocales;
    }

    public void setAddKey(String addKey) {
        this.addKey = addKey;
    }

    public String getAddKey() {
        return addKey;
    }

    public void setAddLocale(String addLocale) {
        this.addLocale = addLocale;
    }

    public String getAddLocale() {
        return addLocale;
    }

    public void setAddMessage(String addmessage) {
        this.addMessage = addmessage;
    }

    public String getAddMessage() {
        return addMessage;
    }

    public void setUndoChanges(String[] undoChanges) {
        this.undoChanges = undoChanges;
    }

    public String[] getUndoChanges() {
        return undoChanges;
    }

    public void setUpdateKey(String updateKey) {
        this.updateKey = updateKey;
    }

    public String getUpdateKey() {
        return updateKey;
    }

    public void setUpdateLocale(String updateLocale) {
        this.updateLocale = updateLocale;
    }

    public String getUpdateLocale() {
        return updateLocale;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }
    
}
