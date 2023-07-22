package org.digijava.module.calendar.dbentity;
import javax.persistence.*;

@Entity
@Table(name = "AMP_EVENT_TYPE")
public class AmpEventType {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_event_type_seq")
    @SequenceGenerator(name = "amp_event_type_seq", sequenceName = "amp_event_type_seq", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COLOR")
    private String color;


    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
