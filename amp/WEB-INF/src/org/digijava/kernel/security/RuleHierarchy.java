package org.digijava.kernel.security;

import java.util.*;

/**
 * @author Octavian Ciubotaru
 */
public class RuleHierarchy<T> {

    private final Map<T, Set<T>> reachableInOneOrMoreStepsRules;

    private RuleHierarchy(Map<T, Set<T>> reachableInOneOrMoreStepsRules) {
        this.reachableInOneOrMoreStepsRules = reachableInOneOrMoreStepsRules;
    }

    /**
     * Computes effective rules based on requested rules.
     *
     * @param requestedRules auth rules requested
     * @return effective auth rules
     */
    public Collection<T> getEffectiveRules(T[] rules) {
        Set<T> response = new HashSet<>();
        for (T rule : rules) {
            response.add(rule);
            if (reachableInOneOrMoreStepsRules.containsKey(rule)) {
                response.addAll(reachableInOneOrMoreStepsRules.get(rule));
            }
        }
        return response;
    }

    public static class Builder<T> {

        private Map<T, List<T>> reachableInOneStepRules = new HashMap<>();

        public Builder<T> addRuleDependency(T requestedRule, T requiredRule) {
            if (!reachableInOneStepRules.containsKey(requestedRule)) {
                reachableInOneStepRules.put(requestedRule, new ArrayList<>());
            }
            reachableInOneStepRules.get(requestedRule).add(requiredRule);
            return this;
        }

        public RuleHierarchy<T> build() {
            Map<T, Set<T>> reachableRules = new HashMap<>();

            for (T rule : reachableInOneStepRules.keySet()) {
                reachableRules.put(rule, computeReachableRules(rule));
            }

            return new RuleHierarchy<>(reachableRules);
        }

        private Set<T> computeReachableRules(T rule) {
            Queue<T> queue = new LinkedList<>();
            queue.add(rule);
            Set<T> reachableRules = new HashSet<>();
            while (!queue.isEmpty()) {
                T head = queue.poll();
                if (reachableInOneStepRules.containsKey(head)) {
                    queue.addAll(reachableInOneStepRules.get(head));
                }
                if (!reachableRules.add(head)) {
                    throw new IllegalStateException("Recursive rules are not allowed!");
                }
            }
            return reachableRules;
        }
    }
}
