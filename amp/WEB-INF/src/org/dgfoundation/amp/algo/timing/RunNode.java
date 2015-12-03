package org.dgfoundation.amp.algo.timing;

import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import org.apache.commons.lang.StringUtils;

/**
 * an object returned by InclusiveTimer to represent a read-only identifying view in the runtime tree
 * @author Dolghier Constantin
 *
 */
public interface RunNode {
	public String getName();
	public Set<? extends RunNode> getSubNodes();
	public long getTotalTime();
	
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
		StringBuilder bld = new StringBuilder(String.format("%s{name: <%s>, totalTime: %s", prefixBuilder.apply(depth), getName(), numberFormatter.apply(getTotalTime())));
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
