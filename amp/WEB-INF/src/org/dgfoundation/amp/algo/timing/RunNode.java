package org.dgfoundation.amp.algo.timing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.algo.AlgoUtils;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

/**
 * an object returned by InclusiveTimer to represent a read-only identifying view in the runtime tree
 * @author Dolghier Constantin
 *
 */
public interface RunNode {

    int DEFAULT_INDENT = 3;

    public String getName();
    public Set<? extends RunNode> getSubNodes();
    public long getTotalTime();

    /** value is either java class (String / Integer / Long / blabla) OR java collection (List, Set, Map) */
    public Map<String, Object> getMeta();

    /**
     * @param key if a value with the given key already exists, will throw exception. Also, "name", "subNodes" and "totalTime" are disallowed keys
     * @param value if it is null, then this call does nothing
     */
    void putMeta(String key, Object value);

    default Map<String, Object> getDetails() {
        return asMap();
    }

    /**
     * renders the node as Json-ready bean
     * @return
     */
    default Map<String, Object> asMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", getName());
        result.put("totalTime", getTotalTime());
        for (Map.Entry<String, Object> entry : this.getMeta().entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        List<Map<String, Object>> subNodes = new ArrayList<>();
        if (getSubNodes() != null)
            for (RunNode subNode : getSubNodes()) {
                subNodes.add(subNode.asMap());
            }
        
        if (subNodes.size() > 0)
            result.put("subNodes", subNodes);

        return result;
    }
    
    default String getDetailsAsString() {
        try {
            return new ObjectMapper().writer().writeValueAsString(asMap());
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }

    public default String asString(IntFunction<String> prefixBuilder, LongFunction<String> numberFormatter, int depth) {
        StringBuilder subnodesString = getSubNodes() == null || getSubNodes().isEmpty() ? null : new StringBuilder(", subNodes: [");
        if (subnodesString != null) {
            boolean z = false;
            for(RunNode node: getSubNodes()) {
                if (z) subnodesString.append(", ");
                subnodesString.append(node.asString(prefixBuilder, numberFormatter, depth + 1));
                z = true;
            };
            subnodesString.append("]");
        }
        Map<String, Object> meta = getMeta();
        String metaStr = (meta == null || meta.isEmpty()) ? "" : meta.toString();
        if (!metaStr.isEmpty()) { // cut the Map-generated {PAYLOAD} braces
            metaStr = metaStr.substring(1, metaStr.length() - 1);
            metaStr = ", " + metaStr;
        }
        StringBuilder bld = new StringBuilder(String.format("%s{name: <%s>, totalTime: %s%s", prefixBuilder.apply(depth), getName(), numberFormatter.apply(getTotalTime()), metaStr));
        if (subnodesString != null)
            bld.append(subnodesString);
        bld.append("}");
        return bld.toString();
    }

    /**
     * returns a description of the state in pseudo-json format
     * @param numberFormatter
     * @return
     */
    public default String asFastString(LongFunction<String> numberFormatter) {
        return asString(z -> "", numberFormatter, 0);
    }

    public default String asUserString(final int blanksPerLevel) {
        return asUserString(blanksPerLevel, duration -> String.format("%d ms", duration));
    }

    public default String asUserString(final int blanksPerLevel, LongFunction<String> numberFormatter) {
        return asString(depth -> depth == 0 ? "" : ("\n" + StringUtils.repeat(" ", blanksPerLevel * (depth + 1))), numberFormatter, 0);
    }
}
