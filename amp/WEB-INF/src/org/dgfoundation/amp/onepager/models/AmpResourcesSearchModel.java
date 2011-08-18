/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.jcr.Node;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author aartimon@dginternational.org
 * @since Aug 18, 2011
 */
public class AmpResourcesSearchModel extends
		AbstractAmpAutoCompleteModel<NodeWrapper> {

	public AmpResourcesSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	
	@Override
	protected ArrayList<NodeWrapper> load() {
		ArrayList<NodeWrapper> ret = new ArrayList<NodeWrapper>();
		try {
			AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
			javax.jcr.Session jcrWriteSession = DocumentManagerUtil.getWriteSession(s.getHttpSession());

			Node otherHomeNode				= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession , s.getCurrentMember());
			Iterator<Node> nit = otherHomeNode.getNodes();
			while (nit.hasNext()) {
				Node n = (Node) nit.next();
				NodeWrapper nw = new NodeWrapper(n);
				ret.add(nw);
			}
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
