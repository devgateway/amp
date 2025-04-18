package org.digijava.module.aim.helper;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Collection;




public class ChartParams {
    
    @SuppressWarnings("rawtypes")
    private Collection data;
    private String title;
    private int chartWidth;
    private int chartHeight;
    private HttpSession session;
    private PrintWriter writer;
    private String url;

    /**
     * @return Returns the chartHeight.
     */
    public int getChartHeight() {
        return chartHeight;
    }

    /**
     * @param chartHeight The chartHeight to set.
     */
    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    /**
     * @return Returns the chartWidth.
     */
    public int getChartWidth() {
        return chartWidth;
    }

    /**
     * @param chartWidth The chartWidth to set.
     */
    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * @return Returns the data.
     */
    @SuppressWarnings("rawtypes")
    public Collection getData() {
        return data;
    }

    /**
     * @param data The data to set.
     */
    public void setData(@SuppressWarnings("rawtypes") Collection data) {
        this.data = data;
    }

    /**
     * @return Returns the session.
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * @param session The session to set.
     */
    public void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the writer.
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * @param writer The writer to set.
     */
    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
    
    
}
