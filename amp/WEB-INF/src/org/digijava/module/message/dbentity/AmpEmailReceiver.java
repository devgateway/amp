package org.digijava.module.message.dbentity;
import javax.persistence.*;


@Entity
@Table(name = "AMP_EMAIL_RECEIVER")
public class AmpEmailReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_receiver_seq")
    @SequenceGenerator(name = "email_receiver_seq", sequenceName = "AMP_EMAIL_RECEIVER_seq", allocationSize = 1)
    @Column(name = "receiver_Id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "email_id")
    private AmpEmail email;


    
    public AmpEmailReceiver (){
        
    }
    
    public AmpEmailReceiver (String address,AmpEmail email, String status){
        this.address=address;
        this.email=email;
        this.status=status;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AmpEmail getEmail() {
        return email;
    }
    public void setEmail(AmpEmail email) {
        this.email = email;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
