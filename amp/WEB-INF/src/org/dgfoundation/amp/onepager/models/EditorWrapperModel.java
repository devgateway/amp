/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;

/**
 * @author aartimon@dginternational.org 
 * @since Jun 16, 2011
 */
public class EditorWrapperModel extends Model<String> {
	private static final Logger logger = Logger.getLogger(EditorWrapperModel.class);

	private static final String KEY_PREFIX = "aim-wckt-";
	
	private IModel<String> keyModel;

	public EditorWrapperModel(IModel<String> m, String id) {
		super();
		this.keyModel = m;
		
		boolean valueStoredInActivity = false;
		String valueStoredInActivityVal = "";

		AmpAuthWebSession session = ((AmpAuthWebSession)Session.get()); 
		AmpAuthWebSession wicketSession = ((AmpAuthWebSession)org.apache.wicket.Session.get());
		
		if(m.getObject() != null && !m.getObject().startsWith("aim-")){
			//all editor keys start with "aim-" so it should be fine to 
			//assume value was stored inside AmpActivity instead of the key
			valueStoredInActivityVal = m.getObject();
			valueStoredInActivity = true;
			m.setObject(null); // to generate a proper editor ket for it
		}
		
		//if (m.getObject() == null || m.getObject().trim().compareTo("") == 0 || !m.getObject().startsWith(KEY_PREFIX)){
		if (m.getObject() == null || m.getObject().trim().compareTo("") == 0 ){
			//no editor key
			String eKey = generateEditorKey(session, id);
			m.setObject(eKey);
		}
		else{
			try {
				Editor editor = DbUtil.getEditor(session.getSite().getSiteId(), keyModel.getObject(), wicketSession.getLocale().getLanguage());
				if (editor != null){
					this.setObject(editor.getBody());
				}
				else
					this.setObject("");
			} catch (EditorException e) {
				logger.error("Can't get editor:", e);
				this.setObject("");
			}
		}
		
		if (valueStoredInActivity)
			setObject(valueStoredInActivityVal);

		if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
			Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new HashMap());

		if (ActivityVersionUtil.isVersioningEnabled()){
			m.setObject(generateEditorKey(session, id));
			setObject(getObject()); //seems pointless, but EDITOR_ITEMS have to be updated with the 
									//value for the new editor key so that versioning works
		}
	}
	
	private String generateEditorKey(AmpAuthWebSession session, String id) {
		String eKey = KEY_PREFIX;
		eKey = eKey + session.getCurrentMember().getMemberId() + "-";
		eKey = eKey + id + "-";
		eKey = eKey + System.currentTimeMillis();
		return eKey;
	}

	@Override
	public void setObject(String object) {
		super.setObject(object);
		if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
			Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new HashMap());
		
		Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS).put(keyModel.getObject(), object);
	}
	
	@Override
	public String getObject() {
		return super.getObject();
	}
}
