package org.digijava.module.um.form;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.UserBean;

public class ViewAllUsersForm
    extends ActionForm {
    private String keyword;
    private int type=-1;
    private Collection<UserBean> users;
    //users list for selected page
    private Collection<UserBean> pagedUsers;
    //users list for selected alpha
    private Collection<UserBean> alphaUsers;
    //its true when no letter is selected
    private boolean selectedNoLetter;
    private Integer currentPage;
    //selected letter
    private String currentAlpha;
    //amount of results per page
    private int tempNumResults;
    private int numResults;
    private Collection pages = null;
    //this field is used to sort user by name,email or workspace
    private String sortBy;
    private String reset;
   
    private int pagesToShow;
    private int offset;
    private int pagesSize;

    private String[] digitPages;
    private String[] alphaPages = null;
    
    private boolean showBanned  = false;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
          //pages = null;
          // currentAlpha = null;        
          //pagedUsers = null;
        if(reset!=null && reset.equals("true")){
            keyword = null;       
            type = -1;
            numResults = tempNumResults = 10;
            setReset("false");
           
        }   
         //setTempNumResults(10);
      }
    
    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public Collection getPages() {
        return pages;
    }

    public void setPages(Collection pages) {
        this.pages = pages;
        if(pages!=null)
        {    
            this.pagesSize=pages.size();
        }
    }

    public int getTempNumResults() {
        return tempNumResults;
    }

    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }

    public String[] getAlphaPages() {
        return alphaPages;
    }

    public void setAlphaPages(String[] alphaPages) {
        this.alphaPages = alphaPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentAlpha() {
        return currentAlpha;
    }

    public void setCurrentAlpha(String currentAlpha) {
        this.currentAlpha = currentAlpha;
    }

    public ViewAllUsersForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<UserBean> getUsers() {
        return users;
    }

    public int getType() {
        return type;
    }

    public void setUsers(Collection<UserBean> users) {
        this.users = users;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public Collection<UserBean> getPagedUsers() {
        return pagedUsers;
    }

    public void setPagedUsers(Collection<UserBean> pagedUsers) {
        this.pagedUsers = pagedUsers;
    }

    public Collection<UserBean> getAlphaUsers() {
        return alphaUsers;
    }

    public void setAlphaUsers(Collection<UserBean> alphaUsers) {
        this.alphaUsers = alphaUsers;
    }

    public boolean isSelectedNoLetter() {
        return selectedNoLetter;
    }

    public void setSelectedNoLetter(boolean selectedNoLetter) {
        this.selectedNoLetter = selectedNoLetter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public int getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public int getOffset() {
        int value;
        if (getCurrentPage()> (this.getPagesToShow()/2)){
            value = (this.getCurrentPage() - (this.getPagesToShow()/2))-1;
        }
        else {
            value = 0;
        }
        setOffset(value);
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getPagesSize() {
        return pagesSize;
    }

    public void setPagesSize(int pagesSize) {
        this.pagesSize = pagesSize;
    }

    public boolean getShowBanned() {
        return showBanned;
    }

    public void setShowBanned(boolean showBanned) {
        this.showBanned = showBanned;
    }

    public void setDigitPages(String[] digitPages) {
        this.digitPages = digitPages;
    }

    public String[] getDigitPages() {
        return digitPages;
    }

    
    
}
