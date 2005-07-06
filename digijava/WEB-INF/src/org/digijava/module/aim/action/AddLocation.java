/** 
 * AddLocation.java
 * @author Akash Sharma
 * Created on 1/02/2005
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.form.AddLocationForm;
import javax.servlet.http.*;

public class AddLocation extends Action 
{
  private static Logger logger = Logger.getLogger(AddLocation.class);

  public ActionForward execute(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response) throws java.lang.Exception
  {
	 AddLocationForm addForm = (AddLocationForm) form;
	 
	 if (request.getParameter("edLevel") != null && request.getParameter("edAction") != null) {
	 	addForm.setEdLevel(request.getParameter("edLevel"));
	 	addForm.setEdAction(request.getParameter("edAction"));
	  	System.out.println("Inside AddLocation[edLevel] :" + addForm.getEdLevel());
	 	System.out.println("Inside AddLocation[edAction]  :" + addForm.getEdAction());
	 }
	 System.out.println("Inside AddLocation[edFlag]  :" + addForm.getEdFlag());
	 logger.debug("In addLocation");
	 
	 String level = addForm.getEdLevel();
	 String action = addForm.getEdAction();
	 boolean edFlag = false;
	 if (addForm.getEdFlag()!= null && addForm.getEdFlag().equals("on"))
	 	edFlag = true;

	if ("create".equals(action)) { 
	 if (addForm.getName() == null || edFlag) {
	 	logger.debug("Inside IF [CREATE]");
	 	if (edFlag) {
	 		addForm.setName("");
			addForm.setCode("");
			addForm.setGsLat("");
			addForm.setGsLong("");
			addForm.setGeoCode("");
			addForm.setDescription("");
	 	}
	 	return mapping.findForward("forward");
	 }
	 else {
	 	Country ctry = new Country();
	 	AmpRegion reg = new AmpRegion();
	 	AmpZone zon = new AmpZone();
	 	
	 	if (addForm.getCountryId() != null) {
	 		System.out.println("Inside ELSE Part[CountryId] :" + addForm.getCountryId());
 			ctry = DbUtil.getDgCountry(addForm.getCountryId());
 		}
	 	else
	 		System.out.println("Inside ELSE Part[CREATE] :ctry is null !");
	 	
	 	if (addForm.getRegionId() != null) {
	 		System.out.println("Inside ELSE Part[RegionId] :" + addForm.getRegionId());
 			reg = DbUtil.getAmpRegion(addForm.getRegionId());
 		}
	 	else
	 		System.out.println("Inside ELSE Part[CREATE] :reg is null !");
	
		 	if (level.equals("region")){
		 		AmpRegion c = new AmpRegion();
		 		
		 		if(ctry != null) {
		 			c.setCountry(ctry);
		 		}
		 		else {
		 			System.out.println("ctry is null!!");
		 		}
		 		
				if (addForm.getDescription() == null || 
						addForm.getDescription().trim().length() == 0) {
					c.setDescription(" 	");}
				else
					c.setDescription(addForm.getDescription());
				
		 		c.setGeoCode(addForm.getGeoCode());
		 		c.setGsLat(addForm.getGsLat());
		 		c.setGsLong(addForm.getGsLong());
		 		c.setName(addForm.getName());
		 		c.setRegionCode(addForm.getCode());
		 
		 		DbUtil.add(c);
		 	}
		 	
		 	if (level.equals("zone")){
		 		
		 		if(ctry != null) {
		 			zon.setCountry(ctry);
		 		}
		 		else {
		 			System.out.println("ctry is null!!");
		 		}
		 		
		 		if(reg != null) {
		 			zon.setRegion(reg);
		 		}
		 		else {
		 			System.out.println("reg is null!!");
		 		}
		 		
		 		//zon.getRegion().setAmpRegionId(addForm.getRegionId());
		 		//zon.getCountry().setIso(addForm.getCountryId());
		 		
				if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					zon.setDescription(" ");
				else
					zon.setDescription(addForm.getDescription());
				
		 		zon.setGeoCode(addForm.getGeoCode());
		 		zon.setGsLat(addForm.getGsLat());
		 		zon.setGsLong(addForm.getGsLong());
		 		zon.setName(addForm.getName());
		 		zon.setZoneCode(addForm.getCode());
		 		DbUtil.add(zon);
		 	}
		 
		 	if (level.equals("woreda")){
		 		AmpWoreda w = new AmpWoreda();
		 		
		 		if (addForm.getZoneId() != null) {
		 			zon = DbUtil.getAmpZone(addForm.getZoneId());
		 		}
		 	
		 		if(ctry != null) {
		 			w.setCountry(ctry);
		 		}
		 		else {
		 			System.out.println("ctry is null!!");
		 		}
		 		
		 		if(zon != null) {
		 			w.setZone(zon);
		 		}
		 		else {
		 			System.out.println("zon is null!!");
		 		}
		 		
		 		//w.getZone().setAmpZoneId(addForm.getZoneId());
		 		//w.getCountry().setIso(addForm.getCountryId());
		 		
				if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					w.setDescription(" ");
				else
					w.setDescription(addForm.getDescription());
				
		 		w.setGeoCode(addForm.getGeoCode());
		 		w.setGsLat(addForm.getGsLat());
		 		w.setGsLong(addForm.getGsLong());
		 		w.setName(addForm.getName());
		 		w.setWoredaCode(addForm.getCode());
		 		DbUtil.add(w);
		 	}
		 	
		 	addForm.setEdFlag("yes");
		 	return mapping.findForward("added");
	 	}
   }
   else if ("edit".equals(action)) {
   	if (addForm.getName() == null || edFlag) {
	 	logger.debug("Inside IF [EDIT]");
			if (level.equals("region")) {
				AmpRegion obj = DbUtil.getAmpRegion(addForm.getRegionId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getRegionCode() != null)
					addForm.setCode(obj.getRegionCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			if (level.equals("zone")) {
				AmpZone obj = DbUtil.getAmpZone(addForm.getZoneId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getZoneCode() != null)
					addForm.setCode(obj.getZoneCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			if (level.equals("woreda")) {
				AmpWoreda obj = DbUtil.getAmpWoreda(addForm.getWoredaId());
				if (obj.getName() != null)
					addForm.setName(obj.getName());
				else
					addForm.setName("");
				if (obj.getWoredaCode() != null)
					addForm.setCode(obj.getWoredaCode());
				else
					addForm.setCode("");
				if (obj.getGsLat() != null)
					addForm.setGsLat(obj.getGsLat());
				else
					addForm.setGsLat("");
				if (obj.getGsLong() != null)
					addForm.setGsLong(obj.getGsLong());
				else
					addForm.setGsLong("");
				if (obj.getGeoCode() != null)
					addForm.setGeoCode(obj.getGeoCode());
				else
					addForm.setGeoCode("");
				if (obj.getDescription() != null)
					addForm.setDescription(obj.getDescription());
				else
					addForm.setDescription("");
			}
			addForm.setEdFlag("on");
	 	return mapping.findForward("forward");
	 }
   	else {
		 	if (level.equals("region")){
		 		AmpRegion obj = null;
		 		if (addForm.getRegionId() != null) {
		 			obj = DbUtil.getAmpRegion(addForm.getRegionId());
		 		}
			 	else
			 		System.out.println("Inside ELSE [EDIT] :obj is null !");
				
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setRegionCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	if (level.equals("zone")){
		 		AmpZone obj = null;
		 		if (addForm.getZoneId() != null) {
		 			obj = DbUtil.getAmpZone(addForm.getZoneId());
		 		}
			 	else
			 		System.out.println("Inside ELSE [EDIT] :obj is null !");
				
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setZoneCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	if (level.equals("woreda")){
		 		AmpWoreda obj = null;
		 		if (addForm.getWoredaId() != null) {
		 			obj = DbUtil.getAmpWoreda(addForm.getWoredaId());
		 		}
			 	else
			 		System.out.println("Inside ELSE [EDIT] :obj is null !");
				
		 		obj.setGeoCode(addForm.getGeoCode());
		 		obj.setGsLat(addForm.getGsLat());
		 		obj.setGsLong(addForm.getGsLong());
		 		obj.setName(addForm.getName());
		 		obj.setWoredaCode(addForm.getCode());
		 		if (addForm.getDescription() == null ||
						addForm.getDescription().trim().length() == 0)
					obj.setDescription(" ");
				else
					obj.setDescription(addForm.getDescription());
		 
		 		DbUtil.update(obj);
		 	}
		 	
		 	addForm.setEdFlag("yes");
		 	return mapping.findForward("added");
   	}
   }else if ("delete".equals(action)) {
   		if (level.equals("region")){
   			AmpRegion obj = null;
   			if (addForm.getRegionId() != null) {
   				obj = DbUtil.getAmpRegion(addForm.getRegionId());
   				DbUtil.delete(obj);
   			}
   			else
   				System.out.println("Inside ELSE [DELETE] :obj is null !");
   		}
   		if (level.equals("zone")){
   			AmpZone obj = null;
   			if (addForm.getZoneId() != null) {
   				obj = DbUtil.getAmpZone(addForm.getZoneId());
   				DbUtil.delete(obj);
   			}
   			else
   				System.out.println("Inside ELSE [DELETE] :obj is null !");
   		}
   		if (level.equals("woreda")){
   			AmpWoreda obj = null;
   			if (addForm.getWoredaId() != null) {
   				obj = DbUtil.getAmpWoreda(addForm.getWoredaId());
   				DbUtil.delete(obj);
   			}
   			else
   				System.out.println("Inside ELSE [DELETE] :obj is null !");
   		}

	 	addForm.setEdFlag("yes");
   		return mapping.findForward("added");
   }
   		
	return null;
  }
 }
