package org.dgfoundation.amp.algo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;


/**
 * generic graph algorithm holder
 * TODO: replace Set<Integer> with a more efficient native-based implementation (like Trove or HPPC), in case this class will see heavy usage
 * @author Dolghier Constantin
 *
 * @param <K> the payload type
 */
public class Graph<K> {
    private int nodeNr = 1;
    //protected final HashSet<GraphNode<K>> nodes = new HashSet<>();
    protected final LinkedHashMap<K, Integer> elemToNode = new LinkedHashMap<>();
    protected final LinkedHashMap<Integer, K> nodeToElem = new LinkedHashMap<>();
    
    /**
     * Map<node_nr, dependency_node_nrs>
     */
    protected final HashMap<Integer, Set<Integer>> elemDependencies = new HashMap<>();
    protected final Function<K, Collection<K>> dependenciesComputer;
            
    /**
     * constructs an instance
     * @param items a set of root elements. The whole graph should be contained in this collection OR in its recursively discoverable dependencies
     * @param dependenciesComputer
     */
    public Graph(Collection<K> items, Function<K, Collection<K>> dependenciesComputer) {
        this.dependenciesComputer = dependenciesComputer;
        discoverGraph(items);
        elemToNode.forEach((item, node) -> {
            elemDependencies.put(node, dependenciesComputer.apply(item).stream().map(dep -> elemToNode.get(dep)).collect(Collectors.toSet()));
        });
    }
    
    /**
     * iteratively runs a wave on the graph, adding all the discovered nodes
     * @param init
     */
    protected final void discoverGraph(Collection<K> init) {
        LinkedHashSet<K> queue = new LinkedHashSet<>(init);
        boolean smthAdded = true;
        while(smthAdded) {
            smthAdded = false;
            LinkedHashSet<K> nextQueue = new LinkedHashSet<>();
            for(K item:queue) {
                if (addItemToDatastructures(item)) {
                    smthAdded = true;
                    nextQueue.addAll(dependenciesComputer.apply(item));
                }
            }
            queue = nextQueue;
        }
    }
    
    protected final boolean addItemToDatastructures(K item) {
        if (!elemToNode.containsKey(item)) {
            elemToNode.put(item, nodeNr ++);
            nodeToElem.put(elemToNode.get(item), item);
            return true;
        }
        return false;
    }

    class TopoNodeInfo {
        public final int node;
        public int unmetDependencies;
        public HashSet<Integer> inboundEdges = new HashSet<>();
        
        public TopoNodeInfo(int node) {
            this.node = node;
            Preconditions.checkArgument(nodeToElem.containsKey(node), "nonexistant node!");
            this.unmetDependencies = elemDependencies.get(node).size();
        }
        
        @Override
        public String toString() {
            return String.format("node %d [%s] depends on <%s>", node, elemToNode.get(node), inboundEdges.toString());
        }
    }
    
    //TODO: create e MapWithDefaultValue once we move to jdk8
    public LinkedHashSet<K> sortTopologically() {
        Map<Integer, TopoNodeInfo> nodesInfo = new HashMap<>();
        Set<Integer> terminalNodes = new TreeSet<>();
        for(int node:elemToNode.values()) {
            nodesInfo.put(node, new TopoNodeInfo(node));
            if (elemDependencies.get(node).isEmpty())
                terminalNodes.add(node);
        }
        
        for(int node:elemToNode.values()) {
            for(int dependency:elemDependencies.get(node)) {
                nodesInfo.get(dependency).inboundEdges.add(node);
            }
        }
        LinkedHashSet<K> res = new LinkedHashSet<K>();
        Set<Integer> allOutputNodes = new HashSet<>();
        while (!terminalNodes.isEmpty()) {
            Set<Integer> newTerminalNodes = new TreeSet<>();
            for(int node:terminalNodes) {
                res.add(nodeToElem.get(node));
                allOutputNodes.add(node);
                for(int inboundNode:nodesInfo.get(node).inboundEdges) {
                    TopoNodeInfo nodeInfo = nodesInfo.get(inboundNode);
                    nodeInfo.unmetDependencies --;
                    if (nodeInfo.unmetDependencies < 0)
                        throw new RuntimeException("unmetDependencies < 0: bug or invalid input graph"); 
                    if (nodeInfo.unmetDependencies == 0)
                        newTerminalNodes.add(inboundNode);
                }
            }
            terminalNodes = newTerminalNodes;
        }
        checkGraphForCycles(nodesInfo, allOutputNodes, res);
        return res;
    }
    
    protected void checkGraphForCycles(Map<Integer, TopoNodeInfo> nodesInfo, Set<Integer> allOutputNodes, LinkedHashSet<K> res) {
        Preconditions.checkArgument(allOutputNodes.size() == res.size(), "nodes set has a different size than indices set");
        if (nodesInfo.size() != allOutputNodes.size()) {
            // we've got a loop in the graph
            Set<Integer> missingNodes = new TreeSet<>(nodesInfo.keySet());
            missingNodes.removeAll(allOutputNodes);
            StringBuilder bld = new StringBuilder();
            for(int node:missingNodes)
                bld.append(String.format("[node %s, id = %d]", nodeToElem.get(node), node));
            throw new RuntimeException("cycle detected, the notoutput data is: " + bld.toString());
        }
    }
}
