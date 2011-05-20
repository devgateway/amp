package org.dgfoundation.amp.onepager.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.apache.wicket.Application;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;

public class JspResolver2 extends AbstractMarkupFilter implements IComponentResolver {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(JspResolver.class);
	private static final String WICKET_MESSAGE_CONTAINER_ID = "_jspresolver_";
	private static final String WICKET_MESSAGE_ATTRIBUTE_NAME = "_jsp_resolver_";
	
	static {
		// Register the jsp tag within the wicket tag namespace
		WicketTagIdentifier.registerWellKnownTagName("jsp");
	}

	public boolean resolve(final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag) {
		if (tag instanceof WicketTag) {
			
			WicketTag wtag = (WicketTag)tag;
			
			if ("jsp".equalsIgnoreCase(wtag.getName())) {
				String file = wtag.getAttributes().getString("file");
				if (file == null || file.trim().length() == 0) {
					throw new MarkupException("Wrong format of <wicket:jsp file='foo.jsp'>: attribute 'file' is missing");
				}

				String layout = wtag.getAttributes().getString("layouts");
				if (layout == null || layout.trim().compareTo("") == 0){
					layout = null;
				}
				
				// Add the jsp component to the component hierarchy
				container.autoAdd(new JspFileContainer(file, layout), markupStream);
				
				// We did process the tag
				return true;
			}
		}

		// We did not process the tag
		return false;
	}

	private static class JspFileContainer extends MarkupContainer {
		private static final long serialVersionUID = 1L;

		private String _file;
		private String _layout;
		
		public JspFileContainer(String file, String layout) {
			super(file);
			
			_file = file;
			_layout = layout;
		}
		
		protected void onRender(MarkupStream markupStream) {
		    markupStream.next();
			
		    WebRequestCycle cycle    = (WebRequestCycle)RequestCycle.get();
		    HttpServletRequest request   = cycle.getWebRequest().getHttpServletRequest();
		    ServletResponse response = cycle.getWebResponse().getHttpServletResponse();
		    ServletContext context   = ((WebApplication)Application.get()).getServletContext();
		    
		    /*
		     * START Prepare Amp request with needed data
		     */
		    if (ComponentContext.getContext(request) == null){
				try {
					ComponentContext compContext = new ComponentContext();
					SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(request.getServerName(), null);
					Site site = siteDomain.getSite();

					if (_layout != null){
						// Put attributes into tiles context
						ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);
						Map contextParameters;
						
						StringTokenizer st = new StringTokenizer(_layout, ";");
						while (st.hasMoreTokens()){
							String temp = st.nextToken();
							contextParameters = viewConfig.getMasterContextAttributes(temp);
							compContext.addAll(contextParameters);
						}
					}
					ComponentContext.setContext(compContext, request);
					
					request.setAttribute(Constants.DIGI_CONTEXT, request.getContextPath() + siteDomain.getSitePath());
					request.setAttribute(Constants.CURRENT_SITE, siteDomain);
					
		    		AmpAuthWebSession webSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
		    		if (webSession != null){
		    			java.util.Locale locale = webSession.getLocale();
		    			Locale lang = new Locale();
		    			lang.setLeftToRight(true);
		    			lang.setMessageLangKey("ln:" + locale.getLanguage());
		    			lang.setCode(locale.getLanguage());
		    			request.setAttribute(Constants.NAVIGATION_LANGUAGE, lang);
		    		}
				} catch (DgException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    /*
		     * END
		     */
		    
		    try {
		    	// Make an attempt at locating the resource.
		    	// If it does not exist, the requestDispatcher.include will simply ignore it.
		    	if (context.getResource(_file) == null) {
					if (shouldThrowExceptionForMissingFile()) {
						throw new WicketRuntimeException(String.format("Cannot locate resource %s within current context: %s", _file, context));
					} else {
						logger.warn("File will not be processed. Cannot locate resource {" + _file + "} within current context: {" + context + "}");
					}
				}
			} catch (MalformedURLException e) {
				throw new WicketRuntimeException(e);
			}
		         
		    try {	
		    	context.getRequestDispatcher(_file).include(request, response);
			} catch (ServletException e) {
				throw new WicketRuntimeException(e);			
			} catch (IOException e) {
				throw new WicketRuntimeException(e);
			}	
		}
	
		private boolean shouldThrowExceptionForMissingFile() {
                    return Application.get().getResourceSettings().getThrowExceptionOnMissingResource();
		}
	}

	@Override
	public MarkupElement nextTag() throws ParseException {

		
		// Get the next tag from the next MarkupFilter in the chain
		// If null, no more tags are available
		final ComponentTag tag = nextComponentTag();
		if ((tag == null) || tag.isClose()) {
			return tag;
		}
		
		final String wicketMessageAttribute = tag.getAttributes().getString(WICKET_MESSAGE_ATTRIBUTE_NAME);

		if ((wicketMessageAttribute != null) && (wicketMessageAttribute.trim().length() > 0)) {
			// check if this tag is raw markup
			if (tag.getId() == null) {
				// if this is a raw tag we need to set the id to something so
				// that wicket will not merge this as raw markup and instead
				// pass it on to a resolver
				tag.setId(WICKET_MESSAGE_CONTAINER_ID);
				tag.setAutoComponentTag(true);
				tag.setModified(true);
			}
			
			
			//tag.addBehavior(new AttributeLocalizer());
		}
		
		//http://www.java2s.com/Open-Source/Java-Document/J2EE/wicket/org/apache/wicket/markup/parser/filter/WicketMessageTagHandler.java.htm
		return tag;
	}
}