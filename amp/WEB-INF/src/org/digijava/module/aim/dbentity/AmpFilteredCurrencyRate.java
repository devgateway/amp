package org.digijava.module.aim.dbentity;

public class AmpFilteredCurrencyRate {
        private Long id;
        private AmpCurrency fromCurrency;
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
