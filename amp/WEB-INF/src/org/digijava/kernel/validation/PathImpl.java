package org.digijava.kernel.validation;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of field path.
 *
 * @author Octavian Ciubotaru
 */
public class PathImpl implements Path {

    private final List<Node> nodes;

    PathImpl() {
        nodes = ImmutableList.of();
    }

    private PathImpl(List<Node> nodes) {
        this.nodes = nodes;
    }

    PathImpl addPropertyNode(String name) {
        NodeImpl leaf = new NodeImpl(name);
        return new PathImpl(new ImmutableList.Builder<Node>().addAll(nodes).add(leaf).build());
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public String toString() {
        return nodes.stream().map(Path.Node::getName).collect(Collectors.joining("~"));
    }

    class NodeImpl implements Path.Node {

        private final String name;

        NodeImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NodeImpl)) {
                return false;
            }
            NodeImpl node = (NodeImpl) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PathImpl)) {
            return false;
        }
        PathImpl nodes1 = (PathImpl) o;
        return nodes.equals(nodes1.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
}
