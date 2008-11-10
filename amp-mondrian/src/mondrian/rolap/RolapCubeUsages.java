/*
// $Id: //open/mondrian/src/main/mondrian/rolap/RolapCubeUsages.java#1
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2001-2007 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
*/

package mondrian.rolap;

import mondrian.olap.MondrianDef;

/**
 * <code>RolapCubeUsages</code>
 * This provides us with the base cubes that a virtual cube uses and
 * specifies if unrelated dimensions to measures from these cubes should be
 * ignored.
 * @author ajoglekar
 * @since Nov 22 2007
 * @version $Id: RolapCubeUsages.java,v 1.1 2008-11-10 10:06:58 mpostelnicu Exp $
 */

public class RolapCubeUsages {
    private MondrianDef.CubeUsages cubeUsages;

    public RolapCubeUsages(MondrianDef.CubeUsages cubeUsage) {
        this.cubeUsages = cubeUsage;
    }

    public boolean shouldIgnoreUnrelatedDimensions(String baseCubeName) {
        if(cubeUsages==null || cubeUsages.cubeUsages == null){
            return false;
        }
        for (MondrianDef.CubeUsage usage : cubeUsages.cubeUsages) {
            if(usage.cubeName.equals(baseCubeName)
                && Boolean.TRUE.equals(usage.ignoreUnrelatedDimensions)) {
                return true;
            }
        }
        return false;
    }
}
