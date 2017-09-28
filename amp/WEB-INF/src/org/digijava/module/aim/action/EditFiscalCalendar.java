package org.digijava.module.aim.action ;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.dgfoundation.amp.currency.inflation.CCExchangeRate;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.AddFiscalCalendarForm;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;


public class EditFiscalCalendar extends Action {

          private static Logger logger = Logger.getLogger(EditFiscalCalendar.class);

          public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws java.lang.Exception {

                     HttpSession session = request.getSession();
                     if (session.getAttribute("ampAdmin") == null) {
                        return mapping.findForward("index");
                     } else {
                        String str = (String)session.getAttribute("ampAdmin");
                        if (str.equals("no")) {
                          return mapping.findForward("index");
                        }
                     }
                     
                     logger.debug("In edit fiscal calendar");

                     AddFiscalCalendarForm editForm = (AddFiscalCalendarForm) form;
                     String action = request.getParameter("action");
                     editForm.setAction(action);
                     
                     if ("create".equals(action)) {
                        if (editForm.getFiscalCalName() == null) {
                            logger.debug("Inside IF [CREATE]");
                            return mapping.findForward("forward");
                         }
                         else {
                            if (session.getAttribute("ampFisCal") != null) {
                                session.removeAttribute("ampFisCal");
                            }
                            //check for name duplication
                            int calCount = org.digijava.module.calendar.util.DbUtil.getFiscalCalendarCount(editForm.getFiscalCalName(), editForm.getFiscalCalId());
                            if(calCount>0){
                                ActionMessages errors= new ActionMessages();
                                errors.add("calendar not unique", new ActionMessage("admin.fiscCal.calExists",TranslatorWorker.translateText("Calendar with the given email already exists") ));
                                saveErrors(request, errors);
                                return mapping.findForward("forward");
                            }
                            
                            AmpFiscalCalendar ampFisCal = new AmpFiscalCalendar();
                            ampFisCal.setStartMonthNum(new Integer(editForm.getStartMonthNum()));
                            ampFisCal.setStartDayNum(new Integer(editForm.getStartDayNum()));
                            ampFisCal.setYearOffset(new Integer(editForm.getYearOffset()));
                            ampFisCal.setName(editForm.getFiscalCalName());
                            ampFisCal.setBaseCal(editForm.getBaseCalendar());
                            if(editForm.getIsFiscal().equals("1")){
                                ampFisCal.setIsFiscal(true);
                            }else{
                                ampFisCal.setIsFiscal(false);
                            }
                            if (editForm.getDescription() == null || editForm.getDescription().trim().equals("")) {
                                ampFisCal.setDescription(new String(" "));
                            } else {
                                 ampFisCal.setDescription(editForm.getDescription());
                            }

                            DbUtil.add(ampFisCal);
                            logger.debug("FiscalCalendar added");
                            return mapping.findForward("added");
                         }
                 
                        } else if ("edit".equals(action)){
                            editForm.setFiscalCalId(new Long(Integer.parseInt(request.getParameter("fiscalCalId"))));
                            AmpFiscalCalendar ampFisCal = FiscalCalendarUtil.getAmpFiscalCalendar(editForm.getFiscalCalId());
                            if (ampFisCal == null) {
                                if (session.getAttribute("ampFisCal") != null) {
                                    session.removeAttribute("ampFisCal");
                                }
                                
                                return mapping.findForward("added");
                            }
                            editForm.setFlag("delete");
                            
                            List<AmpCurrency> constantCurrencies = DbUtil.getFiscalCalConstantCurrencies(editForm.getFiscalCalId());
                            
                            if (constantCurrencies.size() > 0) {
                                editForm.setFlag("orgConstantCurrencies");
                            }
                            
                            if (editForm.getFiscalCalName() == null) {
                                
                                logger.debug("Inside IF [EDIT]");
                                if (ampFisCal.getName() != null)
                                    editForm.setFiscalCalName(ampFisCal.getName());
                                else
                                    editForm.setFiscalCalName("");
                                if (ampFisCal.getDescription() != null)
                                    editForm.setDescription(ampFisCal.getDescription());
                                else
                                    editForm.setDescription("");
                                if (ampFisCal.getStartDayNum() != null)
                                    editForm.setStartDayNum(ampFisCal.getStartDayNum().intValue());
                                if (ampFisCal.getStartMonthNum() != null)
                                    editForm.setStartMonthNum(ampFisCal.getStartMonthNum().intValue());
                                if (ampFisCal.getYearOffset() != null)
                                    editForm.setYearOffset(ampFisCal.getYearOffset().intValue());
                                
                                if (ampFisCal.getBaseCal()!=null){
                                    editForm.setBaseCalendar(ampFisCal.getBaseCal());
                                }else{
                                    editForm.setBaseCalendar(BaseCalendar.BASE_GREGORIAN.getValue());
                                }
                                if (ampFisCal.getIsFiscal()){
                                    editForm.setIsFiscal("1");
                                }else{
                                    editForm.setIsFiscal("0");
                                }
                                return mapping.findForward("forward");
                             }
                             else {
                                if (session.getAttribute("ampFisCal") != null) {
                                    session.removeAttribute("ampFisCal");
                                }
                                
                                //check for name duplication
                                int calCount = org.digijava.module.calendar.util.DbUtil.getFiscalCalendarCount(editForm.getFiscalCalName(), editForm.getFiscalCalId());
                                if(calCount>0){
                                    ActionMessages errors= new ActionMessages();
                                    errors.add("calendar not unique", new ActionMessage("admin.fiscCal.calExists",TranslatorWorker.translateText("Calendar with the given email already exists") ));
                                    saveErrors(request, errors);
                                    return mapping.findForward("forward");
                                }
                                
                                ampFisCal.setStartMonthNum(new Integer(editForm.getStartMonthNum()));
                                ampFisCal.setStartDayNum(new Integer(editForm.getStartDayNum()));
                                ampFisCal.setYearOffset(new Integer(editForm.getYearOffset()));
                                ampFisCal.setName(editForm.getFiscalCalName());
                                ampFisCal.setBaseCal(editForm.getBaseCalendar());
                                
                                if (editForm.getDescription() == null || editForm.getDescription().trim().equals("")) {
                                    ampFisCal.setDescription(new String(" "));
                                } else {
                                    ampFisCal.setDescription(editForm.getDescription());
                                }
                                
                                if(editForm.getIsFiscal().equals("1")){
                                    ampFisCal.setIsFiscal(true);
                                }else{
                                    ampFisCal.setIsFiscal(false);
                                }

                                DbUtil.update(ampFisCal);
                                logger.debug("FiscalCalendar updated");
                                // also regenerate constant currencies linked to this calendar
                                CCExchangeRate.regenerateConstantCurrenciesExchangeRates(false, ampFisCal);
                                return mapping.findForward("added");
                             }
                        } else if ("delete".equals(action)){
                            
                            Iterator<AmpOrganisation> itr1 = DbUtil.getFiscalCalOrgs(editForm.getFiscalCalId()).iterator();
                            Iterator<AmpApplicationSettings> itr2 = DbUtil.getFiscalCalSettings(editForm.getFiscalCalId()).iterator();
                            
                            if (itr1.hasNext() || itr2.hasNext()) {
                                editForm.setFlag("orgReferences");
                                editForm.setAction("edit");
                                editForm.setFiscalCalName(request.getParameter("fiscalCalName"));
                                editForm.setDescription(request.getParameter("description"));
                                editForm.setStartDayNum(Integer.parseInt(request.getParameter("startDayNum")));
                                editForm.setStartMonthNum(Integer.parseInt(request.getParameter("startMonthNum")));
                                editForm.setYearOffset(Integer.parseInt(request.getParameter("yearOffset")));
                                editForm.setIsFiscal(request.getParameter("isFiscal"));
                                return mapping.findForward("forward");
                            } else {
                                if (session.getAttribute("ampFisCal") != null) {
                                    session.removeAttribute("ampFisCal");
                                }
                                
                                AmpFiscalCalendar ampFisCal = DbUtil.getAmpFiscalCalendar(editForm.getFiscalCalId());
                                if (ampFisCal != null) {
                                    List<AmpCurrency> currencies = DbUtil.getFiscalCalConstantCurrencies(editForm.getFiscalCalId());
                                    
                                    for (AmpCurrency oldConstCurrency : currencies) {
                                        CurrencyInflationUtil.deleteConstantCurrencies(oldConstCurrency);
                                    }
                                    
                                    DbUtil.delete(ampFisCal);
                                    logger.debug("FIscal Calendar deleted");
                                }
                                return mapping.findForward("added");
                            }
                        }
                        
                     return mapping.findForward("index");
                }
}
