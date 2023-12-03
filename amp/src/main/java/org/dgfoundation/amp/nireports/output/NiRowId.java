package org.dgfoundation.amp.nireports.output;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class NiRowId implements Comparable<NiRowId> {

    private final List<NiSplitCell> splitters;

    public final long id;

    public NiRowId(long id) {
        this(ImmutableList.of(), id);
    }

    private NiRowId(List<NiSplitCell> splitters, long id) {
        this.splitters = splitters;
        this.id = id;
    }

    public NiRowId withSplitter(NiSplitCell c) {
        NiUtils.failIf(c == null, "splitter must be non-null");
        return new NiRowId(ImmutableList.<NiSplitCell> builder().addAll(this.splitters).add(c).build(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NiRowId niRowId = (NiRowId) o;
        return id == niRowId.id &&
                Objects.equal(splitters, niRowId.splitters);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(splitters, id);
    }

    @Override
    public int compareTo(NiRowId o) {
        int c = Long.compare(id, o.id);
        if (c != 0) {
            return c;
        }
        int n = Math.min(splitters.size(), o.splitters.size());
        for (int i = 0; i < n; i++) {
            c = splitters.get(i).compareTo(o.splitters.get(i));
            if (c != 0) {
                return c;
            }
        }
        return Integer.compare(splitters.size(), o.splitters.size());
    }
}
