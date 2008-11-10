/*
// $Id: SegmentDataset.java,v 1.1 2008-11-10 10:06:00 mpostelnicu Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2002-2002 Kana Software, Inc.
// Copyright (C) 2002-2007 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 21 March, 2002
*/
package mondrian.rolap.agg;

import mondrian.rolap.CellKey;

import java.util.Map;

/**
 * A <code>SegmentDataset</code> holds the values in a segment.
 *
 * @author jhyde
 * @since 21 March, 2002
 * @version $Id: SegmentDataset.java,v 1.1 2008-11-10 10:06:00 mpostelnicu Exp $
 */
interface SegmentDataset extends Iterable<Map.Entry<CellKey,Object>> {
    /**
     * Returns the value at a given coordinate.
     *
     * @param pos Coordinate position
     * @return Value
     */
    Object get(CellKey pos);

    /**
     * Returns the number of bytes occupied by this dataset.
     *
     * @return number of bytes
     */
    double getBytes();

    void put(CellKey key, Object value);
}

// End SegmentDataset.java
