package org.digijava.module.aim.dbentity;
import javax.persistence.*;

@Entity
@Table(name = "AMP_FILTERED_CURRENCY_RATE")
public class AmpFilteredCurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_filtered_currency_rate_seq")
    @SequenceGenerator(name = "amp_filtered_currency_rate_seq", sequenceName = "AMP_FILTERED_CURRENCY_RATE_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_currency_id")
    private AmpCurrency fromCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_currency_id")
    private AmpCurrency toCurrency;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public AmpCurrency getFromCurrency() {
            return fromCurrency;
        }
        public void setFromCurrency(AmpCurrency fromCurrency) {
            this.fromCurrency = fromCurrency;
        }
        public AmpCurrency getToCurrency() {
            return toCurrency;
        }
        public void setToCurrency(AmpCurrency toCurrency) {
            this.toCurrency = toCurrency;
        }
        
        
}
