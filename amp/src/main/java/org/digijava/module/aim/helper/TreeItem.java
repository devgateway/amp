package org.digijava.module.aim.helper;


import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.IndicatorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**

 * TreeItem represents AMP program tree node. It extends HierarchyMember for

 * tree operations and adds XML generation.

 * 

 * @author Irakli Kobiashvili - ikobiashvili@picktek.com

 * @see org.digijava.kernel.util.collections.HierarchyMember

 * 

 */

public class TreeItem extends HierarchyMember {



    public static final String TAG_NAME = "program";



    public static final String CHILDREN_TAG_NAME = "children";



    public static final String INDICATORS_TAG_NAME = "indicators";



    /**

     * Level of the program. This is ThredLocal cos usually there may be many

     * requests in separate threads that construct XML from the set of this
     * objects.

     */

    private static ThreadLocal<Integer> _level = null;


    public boolean showIndicators;



    public TreeItem() {

        super();

    }



    private void incrementLevel() {

        if (_level == null) {

            _level = new ThreadLocal<Integer>();
        }

        synchronized (_level) {

            Integer oldLevel = _level.get();
            if (oldLevel == null) {

                oldLevel = new Integer(0);

            }

            Integer newLevel = new Integer(oldLevel.intValue() + 1);

            _level.set(newLevel);

        }

    }



    private void decrementLevel() {

        synchronized (_level) {

            Integer oldLevel = _level.get();
            _level.set(new Integer(oldLevel.intValue() - 1));

        }

    }



    private int getProgLevel() {

        Integer level = _level.get();
        if (level == null) {

            return 0;

        }

        return level.intValue();

    }



    /**

     * Generates XML for this program including children programs and indicators if they exist.
     * It is better to use Velocity here.
     * 

     * @return text representing XML of this node and all child nodes.
     * @throws Exception 

     */

    @SuppressWarnings("unchecked")
    public String getXml() throws Exception {
        String result = "";

        if (this.getMember() == null) {

            return result;

        }

        AmpTheme realMe = (AmpTheme) this.getMember();

        incrementLevel();

        // compose self XML

        //String translatedText= TranslatorWorker.translateText(DbUtil.filter(realMe.getName()), request);
        String filteredText= DbUtil.filter(realMe.getName());
        
        result += "<" + TAG_NAME + " " + "id=\""

                + realMe.getAmpThemeId().toString() + "\" " + "name=\""

                + filteredText + "\" " + "desc=\""

                + DbUtil.filter(realMe.getDescription()) + "\" " + "parentID=\"";

        if (realMe.getParentThemeId() == null) {

            result += "-1";

        } else {

            result += realMe.getParentThemeId().getAmpThemeId().toString();

        }

        result += "\" ";

        result += "level=\"" + getProgLevel() + "\"" + ">\n";



        // compose sub themes XML if any.

        if (this.getChildren() != null && this.getChildren().size() > 0) {

            result += "  <" + CHILDREN_TAG_NAME + ">\n";

            for (Iterator iter = this.getChildren().iterator(); iter.hasNext();) {

                TreeItem item = (TreeItem) iter.next();

                result += "    " + item.getXml();

            }

            result += "  </" + CHILDREN_TAG_NAME + ">\n";

        }



        // compose indicators XML if any

        if (realMe.getIndicators() != null && realMe.getIndicators().size() > 0) {

            result += "  <" + INDICATORS_TAG_NAME + ">\n";



            // sort indicators by name

            List<IndicatorTheme> sortedIndics = new ArrayList<IndicatorTheme>(realMe.getIndicators());
            Collections.sort(sortedIndics,new IndicatorUtil.IndThemeIndciatorNameComparator());

            
            
            for (IndicatorTheme indicatorTheme : sortedIndics) {
                filteredText=DbUtil.filter(indicatorTheme.getIndicator().getName());
                    
                    
                String indicatString = "<indicator id=\""

                    + indicatorTheme.getIndicator().getIndicatorId() + "\" name=\""
                    + filteredText + "\"/>\n";
                result += "    " + indicatString;

            }

            result += "  </" + INDICATORS_TAG_NAME + ">\n";

        }

        result += "</" + TAG_NAME + ">\n";

        decrementLevel();

        return result;

    }



    public boolean isShowIndicators() {

        return showIndicators;

    }



    public void setShowIndicators(boolean showIndicators) {

        this.showIndicators = showIndicators;

    }



    



}

