package org.digijava.module.aim.annotations.activityversioning;

import java.lang.reflect.Field;

public class CompareOutput {

    private String[] stringOutput;
    private String descriptionOutput;
    // Metadata for creating a new activity with merged values.
    private Field fieldOutput;
    private Object[] originalValueOutput;
    private boolean blockSingleChangeOutput;
    private boolean mandatoryForSingleChangeOutput;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String[] getStringOutput() {
        return stringOutput;
    }

    public void setStringOutput(String[] stringOutput) {
        this.stringOutput = stringOutput;
    }

    public String getDescriptionOutput() {
        return descriptionOutput;
    }

    public void setDescriptionOutput(String descriptionOutput) {
        this.descriptionOutput = descriptionOutput;
    }

    public CompareOutput(String descriptionOutput, String[] stringOutput, Field fieldOutput,
            Object[] originalValueOuput, boolean blockSingleChangeOutput, boolean mandatoryForSingleChangeOutput) {
        super();
        this.descriptionOutput = descriptionOutput;
        this.stringOutput = stringOutput;
        this.fieldOutput = fieldOutput;
        this.originalValueOutput = originalValueOuput;
        this.blockSingleChangeOutput = blockSingleChangeOutput;
        this.mandatoryForSingleChangeOutput = mandatoryForSingleChangeOutput;
    }

    public CompareOutput() {
        super();
    }

    public Field getFieldOutput() {
        return fieldOutput;
    }

    public void setFieldOutput(Field fieldOutput) {
        this.fieldOutput = fieldOutput;
    }

    public Object[] getOriginalValueOutput() {
        return originalValueOutput;
    }

    public void setOriginalValueOutput(Object[] originalValueOutput) {
        this.originalValueOutput = originalValueOutput;
    }

    public boolean getBlockSingleChangeOutput() {
        return blockSingleChangeOutput;
    }

    public void setBlockSingleChangeOutput(boolean blockSingleChangeOutput) {
        this.blockSingleChangeOutput = blockSingleChangeOutput;
    }

    public boolean getMandatoryForSingleChangeOutput() {
        return mandatoryForSingleChangeOutput;
    }

    public void setMandatoryForSingleChangeOutput(boolean mandatoryForSingleChangeOutput) {
        this.mandatoryForSingleChangeOutput = mandatoryForSingleChangeOutput;
    }
}
