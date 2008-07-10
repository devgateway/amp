/**
 * 
 */
package org.digijava.module.gateperm.action;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.TranslatorManager;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.gateperm.core.ClusterIdentifiable;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permissible;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.feed.schema.AssignedObjIdType;
import org.digijava.module.gateperm.feed.schema.CompositePermType;
import org.digijava.module.gateperm.feed.schema.GatePermType;
import org.digijava.module.gateperm.feed.schema.ObjectFactory;
import org.digijava.module.gateperm.feed.schema.Permissions;
import org.digijava.module.gateperm.form.ExchangePermissionForm;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;

/**
 * @author mihai
 * 
 */
public class ExchangePermission extends MultiAction {
	private static Logger logger = Logger.getLogger(ExchangePermission.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return modeSelect(mapping, form, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getParameter("import") != null)
			return modeImportForm(mapping, form, request, response);
		if (request.getParameter("export") != null)
			return modeExportForm(mapping, form, request, response);
		if (request.getParameter("exportPerform") != null)
			return modeExportPerform(mapping, form, request, response);
		if (request.getParameter("importPerform") != null)
			return modeImportPerform(mapping, form, request, response);

		return modeOption(mapping, form, request, response);
	}

	private ActionForward modeImportPerform(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ExchangePermissionForm epf = (ExchangePermissionForm) form;
		FormFile xmlFile = epf.getFileUploaded();
	    byte[] fileData;
		try {
			fileData = xmlFile.getFileData();
			InputStream inputStream= new ByteArrayInputStream(fileData);
		
			JAXBContext jc = JAXBContext.newInstance("org.digijava.module.gateperm.feed.schema");
			Unmarshaller m = jc.createUnmarshaller();
			Permissions xmlPermissions = (Permissions) m.unmarshal(inputStream);
			List gatePerm = xmlPermissions.getGatePerm();
			Iterator i=gatePerm.iterator();
			while (i.hasNext()) {
				GatePermType gp = (GatePermType) i.next();
				xmlToDbGatePermission(gp);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
        
    
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void xmlToDbGatePermission(GatePermType xmlGp) throws DgException, HibernateException {
		//try to fetch an existing gate permission or create a fresh one
		Session requestDBSession = PersistenceManager.getRequestDBSession();
		Permission dbp=PermissionUtil.findPermissionByName(xmlGp.getName());
		if(dbp!=null) {
			if(dbp instanceof CompositePermission) {requestDBSession.delete(dbp);dbp=null;}
		}
		
		if(dbp==null) dbp=new GatePermission();
		GatePermission dbGp=(GatePermission) dbp;
		
		dbGp.setName(xmlGp.getName());
		dbGp.getActions().clear();
		dbGp.getActions().addAll(xmlGp.getAction());
		dbGp.setDedicated(xmlGp.isDedicated());
		dbGp.setDescription(xmlGp.getDescription());
		dbGp.getGateParameters().clear();
		dbGp.getGateParameters().addAll(xmlGp.getParam());
		dbGp.setGateTypeName(GatePermConst.availableGatesBySimpleNames.get(xmlGp.getGateClass()).getName());
		
		dbGp.getPermissibleObjects().clear();
		dbGp.getPermissibleObjects().addAll(getAssignedLocalIds(xmlGp.getAssignedObjId()));
		
		requestDBSession.saveOrUpdate(dbGp);
	}

	private ActionForward modeImportForm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("importForm");
	}

	private ActionForward modeOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return mapping.findForward("option");
	}

	private List<PermissionMap> getAssignedLocalIds(List<AssignedObjIdType> localIds) throws DgException {
		List<PermissionMap> ret=new ArrayList<PermissionMap>();
		for (AssignedObjIdType assignedObjIdType : localIds) {
			PermissionMap pm=new PermissionMap();
			pm.setPermissibleCategory(assignedObjIdType.getPermissibleClass());
			String clusterId=assignedObjIdType.getValue();
			if(clusterId!=null) {
				Identifiable ident = PermissionUtil.getIdentifiableByClusterIdentifier(clusterId, GatePermConst.availablePermissiblesBySimpleNames
						.get(pm.getPermissibleCategory()));
				pm.setObjectIdentifier((Long) ident.getIdentifier());
			}
		}
		return ret;
		
	}
	
	private List<AssignedObjIdType> getAssignedClusterIds(
			ObjectFactory objFactory, Permission p) throws HibernateException,
			JAXBException, DgException {
		List<AssignedObjIdType> l = new ArrayList<AssignedObjIdType>();
		Session requestDBSession = PersistenceManager.getRequestDBSession();
		// assign cluster identifiable and permissible class, this will require
		// the loading of the permissible object itself!:
		Iterator<PermissionMap> iterator = p.getPermissibleObjects().iterator();
		while (iterator.hasNext()) {
			PermissionMap permissionMap = (PermissionMap) iterator.next();

			// try to load the obj as a cluster identifiable to get harmonized
			// id:
			ClusterIdentifiable ci = null;
			if (permissionMap.getObjectIdentifier() != null)
				try {
					ci = (ClusterIdentifiable) requestDBSession
							.get(
									GatePermConst.availablePermissiblesBySimpleNames
											.get(permissionMap
													.getPermissibleCategory()),
									permissionMap.getObjectIdentifier());
					if (ci == null) {
						requestDBSession.delete(permissionMap);
						continue;
					}
				} catch (net.sf.hibernate.ObjectNotFoundException e) {
					// delete garbage data
					requestDBSession.delete(permissionMap);
					continue;
				}
			AssignedObjIdType clusterId = objFactory.createAssignedObjIdType();
			clusterId.setPermissibleClass(permissionMap
					.getPermissibleCategory());
			clusterId.setValue(ci == null ? "" : ci.getClusterIdentifier());
			l.add(clusterId);
		}
		return l;
	}

	@SuppressWarnings("unchecked")
	private ActionForward modeExportPerform(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ExchangePermissionForm epf = (ExchangePermissionForm) form;
		JAXBContext jc = JAXBContext
				.newInstance("org.digijava.module.gateperm.feed.schema");
		Marshaller m = jc.createMarshaller();
		response.setContentType("text/xml");
		response.setHeader("content-disposition",
				"attachment; filename=permissions.xml");
		ObjectFactory objFactory = new ObjectFactory();
		Permissions xmlPermissions = objFactory.createPermissions();

		Session requestDBSession = PersistenceManager.getRequestDBSession();
		Set<Long> exportedIds = new TreeSet<Long>();

		for (int i = 0; i < epf.getPermissions().length; i++) {
			Permission p = (Permission) requestDBSession.get(Permission.class,
					epf.getPermissions()[i]);
			if (exportedIds.contains(p.getId()))
				continue;
			exportedIds.add(p.getId());
			GatePermType gpt = null;
			CompositePermType cpt = null;

			if (p instanceof GatePermission) {
				gpt = dbToXmlGatePermission(objFactory, (GatePermission) p);
				xmlPermissions.getGatePerm().add(gpt);
			}

			if (p instanceof CompositePermission) {
				cpt = dbToXmlCompositePermission(objFactory,
						(CompositePermission) p);
				xmlPermissions.getCompositePerm().add(cpt);
			}

		}
		m.marshal(xmlPermissions, response.getOutputStream());

		return null;
	}

	@SuppressWarnings("unchecked")
	private GatePermType dbToXmlGatePermission(ObjectFactory of,
			GatePermission gp) throws JAXBException, HibernateException,
			DgException {
		GatePermType xmlPerm = of.createGatePermType();
		xmlPerm.setName(gp.getName());
		if (gp.getDescription() != null)
			xmlPerm.setDescription(gp.getDescription());
		xmlPerm.getAction().addAll(gp.getActions());
		xmlPerm.getParam().addAll(gp.getGateParameters());
		xmlPerm.setGateClass(gp.getGateSimpleName());
		xmlPerm.getAssignedObjId().addAll(getAssignedClusterIds(of, gp));
		if(gp.isDedicated()) xmlPerm.setDedicated(true);
		return xmlPerm;
	}

//	private GatePermType xmlToDbGatePermission(GatePermType xml) throws DgException  {
//		List<PermissionMap> pmList=getAssignedLocalIds(xml.getAssignedObjId());
		//try to load an object with that identifier
//		GatePermType xmlPerm = gp.createGatePermType();
//		xmlPerm.setName(gp.getName());
//		if (gp.getDescription() != null)
//			xmlPerm.setDescription(gp.getDescription());
//		xmlPerm.getAction().addAll(gp.getActions());
//		xmlPerm.getParam().addAll(gp.getGateParameters());
//		xmlPerm.setGateClass(gp.getGateSimpleName());
//		xmlPerm.getAssignedObjId().addAll(getAssignedClusterIds(of, gp));
//		if(gp.isDedicated()) xmlPerm.setDedicated(true);
//		return xmlPerm;
//	}
	
	
	@SuppressWarnings("unchecked")
	private CompositePermType dbToXmlCompositePermission(ObjectFactory of,
			CompositePermission cp) throws JAXBException, HibernateException,
			DgException {
		CompositePermType xmlPerm = of.createCompositePermType();
		xmlPerm.setName(cp.getName());
		if (cp.getDescription() != null)
			xmlPerm.setDescription(cp.getDescription());
		if (cp.getIntersection() != null)
			xmlPerm.setIntersection(cp.getIntersection());

		Iterator<Permission> i = cp.getPermissions().iterator();
		while (i.hasNext()) {
			Permission p = (Permission) i.next();
			if (p instanceof GatePermission)
				xmlPerm.getGatePerm().add(
						dbToXmlGatePermission(of, (GatePermission) p));
			if (p instanceof CompositePermission)
				xmlPerm.getCompositePerm()
						.add(
								dbToXmlCompositePermission(of,
										(CompositePermission) p));
		}

		xmlPerm.getAssignedObjId().addAll(getAssignedClusterIds(of, cp));
		if(cp.isDedicated()) xmlPerm.setDedicated(true);
		return xmlPerm;
	}

	public ActionForward modeExportForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List<Permission> allUnDedicatedPermissions = PermissionUtil
				.getAllUnDedicatedPermissions();
		List<Permission> allDedicatedCompositePermissions = PermissionUtil
				.getAllDedicatedCompositePermissions();
		List<Permission> perms = new ArrayList<Permission>();
		perms.addAll(allDedicatedCompositePermissions);
		perms.addAll(allUnDedicatedPermissions);

		request.setAttribute("allPermissions", perms);
		return mapping.getInputForward();
	}

}
