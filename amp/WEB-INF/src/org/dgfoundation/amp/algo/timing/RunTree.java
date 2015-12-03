package org.dgfoundation.amp.algo.timing;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RunTree implements RunNode {
	protected final String name;
	protected final RunTree parent;
	
	public RunTree(String name, RunTree parent) {
		this.name = name;
		this.parent = parent;
		if (this.parent != null)
			this.parent.addSubtree(this);
	}
	
	/**
	 * the total runtime of this tree, including children
	 */
	protected long totalTime = 0;
	
	protected LinkedHashSet<RunTree> subtrees;
	
	protected void addSubtree(RunTree subtree) {
		if (subtrees == null)
			subtrees = new LinkedHashSet<>();
		subtrees.add(subtree);
	}
	
	@Override
	public long getTotalTime() {
		return this.totalTime;
	}
	
	protected void addTime(long toAdd) {
		if (toAdd < 0)
			throw new IllegalArgumentException("you're not allowed to decrease the total runtime!");
		this.totalTime += toAdd;
//		if (this.parent != null)
//			this.parent.addTime(toAdd);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public RunTree getParent() {
		return this.parent;
	}

	@Override
	public Set<? extends RunNode> getSubNodes() {
		return subtrees == null ? null : Collections.unmodifiableSet(subtrees);
	}
	
	@Override
	public String toString() {
		return this.asFastString(j -> String.format("%d ms", j));
	}
}

