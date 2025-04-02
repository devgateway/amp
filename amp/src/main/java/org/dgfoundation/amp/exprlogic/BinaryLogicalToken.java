/**
 * 
 */
package org.dgfoundation.amp.exprlogic;

/**
 * @author mihai
 *
 */
public abstract class BinaryLogicalToken extends LogicalToken {
    protected LogicalToken left;
    protected LogicalToken right;

    public BinaryLogicalToken(LogicalToken left, LogicalToken right, boolean negation) {
        this.left=left;
        this.right=right;
        this.negation=negation;
    }
}
