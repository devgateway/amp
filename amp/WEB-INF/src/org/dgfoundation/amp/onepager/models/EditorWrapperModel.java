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

	public EditorWrapperModel(IModel<String> m) {
		super();
		this.keyModel = m;

		AmpAuthWebSession session = ((AmpAuthWebSession)Session.get()); 
		
		if (m.getObject() == null || m.getObject().trim().compareTo("") == 0 || !m.getObject().startsWith(KEY_PREFIX)){
			//no editor key
			String eKey = KEY_PREFIX;
			eKey = eKey + session.getCurrentMember().getMemberId() + "-";
			eKey = eKey + System.currentTimeMillis();
			m.setObject(eKey);
		}
		else{
			try {
				Editor editor = DbUtil.getEditor(session.getSite().getSiteId(), keyModel.getObject(), session.getLocale().getLanguage());
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
		
		if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
			Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new HashMap());
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
