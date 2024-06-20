package org.digijava.module.aim.action.dataimporter.model;

import java.util.List;

public class Program {
    private Long id;
    private Long program;
    private List<Object> indirect_programs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }

    public List<Object> getIndirect_programs() {
        return indirect_programs;
    }

    public void setIndirect_programs(List<Object> indirect_programs) {
        this.indirect_programs = indirect_programs;
    }

    // Getters and setters
}
