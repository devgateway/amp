package com.mysticcoders.mysticpaste;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.io.IObjectStreamFactory;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;
import com.mysticcoders.mysticpaste.web.pages.paste.PasteItemPage;
import com.mysticcoders.mysticpaste.web.pages.history.HistoryPage;
import com.mysticcoders.mysticpaste.web.pages.view.ViewPublicPage;
import com.mysticcoders.mysticpaste.web.pages.view.ViewPrivatePage;
import com.mysticcoders.mysticpaste.web.pages.plugin.PluginPage;

import javax.servlet.ServletContext;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class MysticPasteApplication extends WebApplication {
    /**
     * Constructor
     */
    public MysticPasteApplication() {
    }

    private SpringComponentInjector springComponentInjector;

    public SpringComponentInjector getSpringComponentInjector() {
        return springComponentInjector;
    }

    public void setSpringComponentInjector(
            SpringComponentInjector springComponentInjector) {
        this.springComponentInjector = springComponentInjector;
    }

    @Override
    protected void init() {
        super.init();


        if (springComponentInjector == null) {
            this.springComponentInjector = new SpringComponentInjector(this);
        }
        addComponentInstantiationListener(springComponentInjector);

        getMarkupSettings().setStripWicketTags(true);

        mountBookmarkablePage("/home", HomePage.class);
        mountBookmarkablePage("/new", PasteItemPage.class);
        mountBookmarkablePage("/history", HistoryPage.class);
        mountBookmarkablePage("/plugin", PluginPage.class);
        mount(new IndexedParamUrlCodingStrategy("/view", ViewPublicPage.class));
        mount(new IndexedParamUrlCodingStrategy("/private", ViewPrivatePage.class));

        ServletContext servletContext = super.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        org.apache.wicket.util.lang.Objects.setObjectStreamFactory(new IObjectStreamFactory.DefaultObjectStreamFactory());

    }

    public static MysticPasteApplication getInstance() {
        return ((MysticPasteApplication) WebApplication.get());
    }


    public Class<PasteItemPage> getHomePage() {
        return PasteItemPage.class;
    }

    private ApplicationContext applicationContext;


    public Object getBean(String name) {
        if (name == null) return null;

        return applicationContext.getBean(name);
    }


}
