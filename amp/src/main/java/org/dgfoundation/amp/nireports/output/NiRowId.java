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
    private final List<Long> coordinates;

    public final long id;

    public NiRowId(long id) {
        this(ImmutableList.of(), ImmutableList.of(), id);
    }

    private NiRowId(List<NiSplitCell> splitters, List<Long> coordinates, long id) {
        this.splitters = splitters;
        this.coordinates = coordinates;
        this.id = id;
    }

    public NiRowId withSplitter(NiSplitCell c) {
        NiUtils.failIf(c == null, "splitter must be non-null");
        return new NiRowId(ImmutableList.<NiSplitCell> builder().addAll(this.splitters).add(c).build(), coordinates, id);
    }

    public NiRowId withCoordinates(List<Long> coordinates) {
        return new NiRowId(splitters, ImmutableList.copyOf(coordinates), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NiRowId niRowId = (NiRowId) o;
        return id == niRowId.id &&
                Objects.equal(splitters, niRowId.splitters) &&
                Objects.equal(coordinates, niRowId.coordinates);
    }

    @Override
    public int hashCode() {
        return coordinates.isEmpty() ? Objects.hashCode(splitters, id) : Objects.hashCode(splitters, id, coordinates);
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
        c = Integer.compare(splitters.size(), o.splitters.size());
        if (c != 0) {
            return c;
        }
        n = Math.min(coordinates.size(), o.coordinates.size());
        for (int i = 0; i < n; i++) {
            c = Long.compare(coordinates.get(i), o.coordinates.get(i));
            if (c != 0) {
                return c;
            }
        }
        return Integer.compare(coordinates.size(), o.coordinates.size());
    }
}
