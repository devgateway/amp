/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.digijava.kernel.request.TLSUtils;
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
		
		String valueStoredInActivityVal = "";

		AmpAuthWebSession session = ((AmpAuthWebSession)Session.get());

        if(keyModel.getObject() != null && !keyModel.getObject().startsWith("aim-")){
			//all editor keys start with "aim-" so it should be fine to 
			//assume value was stored inside AmpActivity instead of the key
			valueStoredInActivityVal = keyModel.getObject();
			keyModel.setObject(null); // to generate a proper editor ket for it
		}
		
		//if (m.getObject() == null || m.getObject().trim().compareTo("") == 0 || !m.getObject().startsWith(KEY_PREFIX)){
		if (keyModel.getObject() == null || keyModel.getObject().trim().compareTo("") == 0 ){
			//no editor key
			String eKey = generateEditorKey(session, id);
			keyModel.setObject(eKey);
			setObject(valueStoredInActivityVal);
		}
		else{
			try {
				Editor editor = DbUtil.getEditor(session.getSite(), keyModel.getObject(), TLSUtils.getLangCode());
				if (editor != null){
					super.setObject(editor.getBody());
				}
				else
					super.setObject("");
			} catch (EditorException e) {
				logger.error("Can't get editor:", e);
				super.setObject("");
			}
		}
		
		if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
			Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new EditorStore());

		if (ActivityVersionUtil.isVersioningEnabled()){
			String oldKey = keyModel.getObject();
			String newKey = generateEditorKey(session, id);
			Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS).getOldKey().put(newKey, oldKey);
			keyModel.setObject(newKey);
		}
		//update the EDITOR_ITEMS object after we've possibly changed the key
		setObject(getObject());
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
			Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new EditorStore());
		
		Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS).getValues().put(keyModel.getObject(), object);
	}
	
	@Override
	public String getObject() {
		return super.getObject();
	}
}
