package org.digijava.module.aim.helper;

import java.util.Iterator;

import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

public class TreeItem extends HierarchyMember {

    public static final String TAG_NAME = "program";
    public static final String CHILDREN_TAG_NAME = "children";
    public static final String INDICATORS_TAG_NAME = "indicators";

    public boolean showIndicators;

    public TreeItem() {
        super();
    }

    public String getXml() {
        String result = "";
        if (this.getMember() == null) {
            return result;
        }
        AmpTheme realMe = (AmpTheme)this.getMember();

        //compose self XML
        result += "<" + TAG_NAME + " "
                + "id=\"" + realMe.getAmpThemeId().toString() + "\" "
                + "name=\"" + filter(realMe.getName()) + "\" "
                + "desc=\"" + filter(realMe.getDescription()) + "\" "
        		+ "parentID=\"";
        		if (realMe.getParentThemeId()==null){
        			result+="-1";
        		}else{
        			result+=realMe.getParentThemeId().getAmpThemeId().toString();
        		}
        		result+="\" ";
                result+= "level=\"" + String.valueOf(this.getLevel()) + "\""
                + ">\n";

        //compose sub themes XML if any.
        if (this.getChildren() != null && this.getChildren().size() > 0) {
            result += "  <" + CHILDREN_TAG_NAME + ">\n";
            for (Iterator iter = this.getChildren().iterator(); iter.hasNext(); ) {
                TreeItem item = (TreeItem) iter.next();
                result += "    " + item.getXml();
            }
            result += "  </" + CHILDREN_TAG_NAME + ">\n";
        }

        //compose indicators XML if any
        if (realMe.getIndicators() != null && realMe.getIndicators().size() > 0) {
            result += "  <" + INDICATORS_TAG_NAME + ">\n";
            for (Iterator indicIter = realMe.getIndicators().iterator();
                 indicIter.hasNext(); ) {
                AmpThemeIndicators item = (AmpThemeIndicators) indicIter.next();
                String indicatString = "<indicator id=\"" +
                        item.getAmpThemeIndId()
                        + "\" name=\"" + filter(item.getName()) + "\"/>\n";
                result += "    " + indicatString;
            }
            result += "  </" + INDICATORS_TAG_NAME + ">\n";
        }
        result += "</" + TAG_NAME + ">\n";
        return result;
    }

    public boolean isShowIndicators() {
        return showIndicators;
    }

    public void setShowIndicators(boolean showIndicators) {
        this.showIndicators = showIndicators;
    }
    
    private String filter(String text){
    	String result=null;
    	if(text!=null){
    		result=text.replaceAll(">","&gt;");
    		result=result.replaceAll("<","&lt;");
    	}
    	return result;
    }

}
