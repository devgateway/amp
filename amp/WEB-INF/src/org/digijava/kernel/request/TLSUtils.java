package org.digijava.kernel.request;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Thread-local storage utils: data stored per-current-request in the TLS area, to avoid carrying HttpServletRequest deep inside the stack
 * @author Dolghier Constantin
 *
 */
public class TLSUtils {
    private static final Logger logger = Logger.getLogger(TLSUtils.class);
    private static ThreadLocal<TLSUtils> threadLocalInstance = new ThreadLocal<TLSUtils>();

    public Site site;
    public HttpServletRequest request;
    private static String forcedLangCode = null;
    private boolean filterGlobally;
    
    public static String getLangCode() {
        if (TLSUtils.forcedLangCode != null)
            return TLSUtils.forcedLangCode;
        try
        {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            Locale lang = (Locale) request.getAttribute(Constants.NAVIGATION_LANGUAGE);
            if ((lang == null) && (request.getSession() != null))
                lang = (Locale) request.getSession().getAttribute(Constants.NAVIGATION_LANGUAGE);
            if (lang == null){
                for (Cookie cookie: request.getCookies()){
                    if (cookie.getName().equals("digi_language")) {
                        return cookie.getValue();
                    }
                }
                //no cookie found
                //not needed since no cookies on rest
//              logger.error("Barely missed an exception, probably running Chrome on localhost");//, new RuntimeException("please enable cookies!")); //we shouldn't get here :) - but we do EVERYTIME IN CHROME
                return "en";
            }
            String code = lang.getCode();
            //logger.error("Current language:" + code);
            return code;
        }
        catch(Exception e)
        {
            // logger.info("trying to get TLSLangCode threw an exception. THIS IS AN ERROR IF happened OUTSIDE A REQUEST", e);
            return "en";
        }
    }
    
    public final static boolean equalValues(Object a, Object b)
    {
        if (a == null)
            return b == null;
        return a.equals(b);
    }
    
    /**
     * DANGEROUS! ONLY USE IN TESTCASES!
     * @param langCode
     */
    public void setForcedLangCode(String langCode)
    {
        if (!equalValues(langCode, forcedLangCode))
        {
            try
            {
                PersistenceManager.getRequestDBSession().flush();
                PersistenceManager.getRequestDBSession().clear();
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        offlineSetForcedLanguage(langCode);
    }
    
    public static void offlineSetForcedLanguage(String langCode) {
        forcedLangCode = langCode;
    }
    
    /**
     * calculates the effectively-used language code, e.g. either the currently-set one OR the default one ("en")
     * @return
     */
    public static String getEffectiveLangCode()
    {
        String langCode = getLangCode();
        if (langCode == null)
            langCode = "en";
        return langCode;
    }

    public static Long getSiteId()
    {
        Site site = getSite();
        return Site.getIdOf(site);
    }
    
    public static Site getSite()
    {
        TLSUtils instance = getThreadLocalInstance();
        return instance.site;
    }
    
    public static HttpServletRequest getRequest()
    {
        TLSUtils instance = getThreadLocalInstance();
        return instance.request;
    }

    public static TLSUtils getThreadLocalInstance()
    {
        TLSUtils res = threadLocalInstance.get();
        if (res == null)
        {
            res = new TLSUtils();
            threadLocalInstance.set(res);
        }
        return res;
    }
    
    public static void populate(HttpServletRequest request){
        populate(request, null);
    }
    
    public static void populate(HttpServletRequest request, SiteDomain siteDomain) {
        if (siteDomain == null) {
            siteDomain = RequestUtils.getSiteDomain(request);
        } else {
            RequestUtils.setSiteDomain(request, siteDomain);
        }
        TLSUtils.getThreadLocalInstance().request = request;
        TLSUtils.getThreadLocalInstance().site = siteDomain == null ? null : siteDomain.getSite();
    }
    
    public static void clean() {
        TLSUtils.getThreadLocalInstance().request = null;
        TLSUtils.getThreadLocalInstance().site = null;
    }

    /**
     * populate TLSUtils using a mock objects to store request and session
     * objects so they can be used in environments where no request is available
     * (such as jobs)
     */
    public static void populateMockTlsUtils() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);
        
        
        ServletContext mockServletContext=Mockito.mock(ServletContext.class);
        
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        final Map<String, Object> requestAttributes = new ConcurrentHashMap<String, Object>();

        getMockSetter(requestAttributes).when(mockRequest).setAttribute(Mockito.anyString(), Mockito.anyObject());
        getMockGetter(requestAttributes).when(mockRequest).getAttribute(Mockito.anyString());

        final Map<String, Object> sessionAttributes = new ConcurrentHashMap<String, Object>();
        
        getMockSetter(sessionAttributes).when(mockSession).setAttribute(Mockito.anyString(), Mockito.anyObject());
        getMockGetter(sessionAttributes).when(mockSession).getAttribute(Mockito.anyString());

        
        Mockito.when(mockRequest.getServletContext()).thenReturn(mockServletContext);
        final Map<String, Object> servletContextAttributes = new ConcurrentHashMap<String, Object>();

        
        getMockSetter(servletContextAttributes).when(mockServletContext).setAttribute(Mockito.anyString(), Mockito.anyObject());
        getMockGetter(servletContextAttributes).when(mockServletContext).getAttribute(Mockito.anyString());

        
        Stubber schemaStubber=Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                String schemaPath = invocation.getArgumentAt(0, String.class);
                return Paths.get(schemaPath).toAbsolutePath().toString();

            }
            
        });
        schemaStubber.when(mockServletContext).getRealPath(Mockito.anyString());
        
        TLSUtils.getThreadLocalInstance().request = mockRequest;
    }

    private static Stubber getMockGetter(final Map<String, Object> sessionAttributes) {
        Stubber s=
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgumentAt(0, String.class);
                Object value = sessionAttributes.get(key);
                return value;
            }
        });
        return s;
    }

    private static Stubber getMockSetter(final Map<String, Object> sessionAttributes) {
        Stubber s=
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String key = invocation.getArgumentAt(0, String.class);
                Object value = invocation.getArgumentAt(1, Object.class);
                sessionAttributes.put(key, value);
                return null;
            }
        });
        return s;
    }

    public static boolean isFilterGlobally() {
        return getThreadLocalInstance().filterGlobally;
    }

    public static <K> K inGlobalFilterContext(Supplier<K> supplier) {
        TLSUtils tls = getThreadLocalInstance();
        boolean oldFilterGlobally = tls.filterGlobally;
        try {
            tls.filterGlobally = true;
            return supplier.get();
        } finally {
            tls.filterGlobally = oldFilterGlobally;
        }
    }
}
