package org.digijava.module.um.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.digijava.kernel.util.UserUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.um.form.ViewEditUserForm;
import org.digijava.kernel.user.User;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.Locale;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.kernel.dbentity.Country;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class ViewEditUser
    extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    ViewEditUserForm uForm = (ViewEditUserForm) form;
    User user = null;
    HttpSession session = request.getSession();
    String isAmpAdmin = (String) session.getAttribute("ampAdmin");
    ActionErrors errors = new ActionErrors();

    if (uForm.getId() != null) {
      user = UserUtils.getUser(uForm.getId());
    }
    else if (uForm.getEmail() != null) {
      user = UserUtils.getUserByEmail(uForm.getEmail());
    }
    Boolean isBanned = uForm.getBan();
    if (isBanned != null) {
      if (isAmpAdmin.equals("yes")) {
        user.setBanned(isBanned);
        DbUtil.updateUser(user);
      }
      else {
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
            "error.um.banUserInvalidPermission"));
        saveErrors(request, errors);
      }
      resetViewEditUserForm(uForm);
      return mapping.findForward("saved");

    }
    if (uForm.getEvent() == null) {

      Locale navLang = RequestUtils.getNavigationLanguage(request);
      if (navLang != null) {
        Collection countrieCol = TrnUtil.getCountries(navLang.getCode());
        if (countrieCol != null) {
          List sortedCountrieList = new ArrayList(countrieCol);
          Collections.sort(sortedCountrieList, TrnUtil.countryNameComparator);
          uForm.setCountries(sortedCountrieList);
        }

        Collection langCol = TrnUtil.getLanguages(navLang.getCode());
        if (langCol != null) {
          List sortedLangList = new ArrayList(langCol);
          Collections.sort(sortedLangList, TrnUtil.localeNameComparator);
          uForm.setLanguages(sortedLangList);
        }
      }

      Collection orgTypeCol = DbUtil.getAllOrgTypes();
      if (orgTypeCol != null) {
        uForm.setOrgTypes(orgTypeCol);
      }

      uForm.setMailingAddress(null);
      uForm.setFirstNames(null);
      uForm.setLastName(null);
      uForm.setName(null);
      uForm.setUrl(null);
      uForm.setSelectedCountryIso(null);
      uForm.setSelectedLanguageCode(null);
      uForm.setSelectedOrgName(null);

      uForm.setSelectedOrgGroupId(null);
      uForm.setSelectedOrgTypeId(null);
      uForm.setSelectedCountryIso(null);

      uForm.setId(null);
      uForm.setEmail(null);

      if (user != null) {
        uForm.setMailingAddress(user.getAddress());

        if (user.getCountry() != null) {
          uForm.setSelectedCountryIso(user.getCountry().getIso());
        }

        uForm.setId(user.getId());
        uForm.setEmail(user.getEmail());
        uForm.setFirstNames(user.getFirstNames());
        uForm.setId(user.getId());
        uForm.setLastName(user.getLastName());
        uForm.setName(user.getName());
        uForm.setUrl(user.getUrl());

        Locale language = user.getRegisterLanguage();
        uForm.setSelectedLanguageCode(language.getCode());
        uForm.setSelectedOrgName(user.getOrganizationName());

        if (user.getOrganizationName() != null ||
            !user.getOrganizationName().equals("")) {
          Collection orgCol = org.digijava.module.aim.util.DbUtil.
              getAllOrganisation();

          AmpOrganisation orgnisation = null;
          if (orgCol != null) {
            for (Iterator iter = orgCol.iterator(); iter.hasNext(); ) {
              orgnisation = (AmpOrganisation) iter.next();
              if (orgnisation.getName().equals(uForm.getSelectedOrgName())) {
                break;
              }
            }
          }

          Collection orgGrpCol = DbUtil.getAllOrgGroup();
          AmpOrgGroup orgGroup = null;
          if (orgGrpCol != null && orgnisation != null) {
            for (Iterator orgGroupIter = orgGrpCol.iterator();
                 orgGroupIter.hasNext(); ) {
              orgGroup = (AmpOrgGroup) orgGroupIter.next();
              if (orgGroup != null &&
                  orgGroup.getOrgGrpName().equals(orgnisation.getOrgGroup())) {
                uForm.setSelectedOrgGroupId(orgGroup.getAmpOrgGrpId());
                if (orgGroup.getOrgType() != null) {
                  uForm.setSelectedOrgTypeId(orgGroup.getOrgType().
                                             getAmpOrgTypeId().toString());
                }
                break;
              }
            }
          }

          uForm.setOrgTypes(DbUtil.getAllOrgTypes());
          if (uForm.getSelectedOrgTypeId() != null) {
            uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.
                getSelectedOrgTypeId())));
          }
          uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
        }
      }
    }
    else {

      if (uForm.getEvent().equalsIgnoreCase("save")) {
        if (user != null) {
          user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(
              uForm.
              getSelectedCountryIso()));
          user.setEmail(uForm.getEmail());
          user.setFirstNames(uForm.getFirstNames());
          user.setLastName(uForm.getLastName());
          user.setAddress(uForm.getMailingAddress());
          user.setOrganizationName(uForm.getSelectedOrgName());
          user.setUrl(uForm.getUrl());

          Locale language = DbUtil.getLanguageByCode(uForm.
              getSelectedLanguageCode());
          user.setRegisterLanguage(language);
          DbUtil.updateUser(user);
          resetViewEditUserForm(uForm);
          return mapping.findForward("saved");

        }

        else if (uForm.getEvent().equalsIgnoreCase("typeSelected")) {
          uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.
              getSelectedOrgTypeId())));
          if (uForm.getOrgs() != null && uForm.getOrgs().size() != 0) {
            uForm.getOrgs().clear();
          }
        }
        else if (uForm.getEvent().equalsIgnoreCase("groupSelected")) {
          uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
        }
      }

    }

    resetViewEditUserForm(uForm);
    return mapping.findForward("forward");
  }

  public ViewEditUser() {
  }

  public void resetViewEditUserForm(ViewEditUserForm uForm) {
    uForm.setBan(null);
    uForm.setEvent(null);
  }
}
