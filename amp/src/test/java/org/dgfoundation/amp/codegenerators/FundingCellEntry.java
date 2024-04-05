package org.dgfoundation.amp.codegenerators;

import org.dgfoundation.amp.nireports.CategAmountCell;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.dgfoundation.amp.codegenerators.CodeGenerator.escape;
import static org.dgfoundation.amp.codegenerators.CodeGenerator.pad;

/**
 * a simplified representation of a {@link CategAmountCell}
 * @author Dolghier Constantin
 *
 */
@SuppressWarnings("serial")
public class FundingCellEntry implements Serializable {
    public final BigDecimal amount;
    public final String activityTitle;
    public final int year;
    public final String month;
    public final String pledge;
    public final String transaction_type;
    public final String agreement;
    public final String recipient_org;
    public final String recipient_role;
    public final String source_role;
    public final String adjustment_type;
    public final String donor_org;
    public final String funding_status;
    public final String mode_of_payment;
    public final String terms_assist;
    public final String financing_instrument;
    public final String transaction_date;
        
    public FundingCellEntry(BigDecimal amount, String activityTitle, String year, String month, 
                String pledge, String transaction_type, String agreement, String recipient_org, 
                String recipient_role, String source_role, String adjustment_type,
                String donor_org, String funding_status, String mode_of_payment, 
                String terms_assist, String financing_instrument, String transaction_date){
        this.amount = amount;
        this.activityTitle = activityTitle;
        this.year = extractYear(year);
        this.month = month;
        this.pledge = pledge;
        this.transaction_type = transaction_type;
        this.agreement = agreement;
        this.recipient_org = recipient_org;
        this.recipient_role = recipient_role;
        this.source_role = source_role;
        this.adjustment_type = adjustment_type;
        this.donor_org = donor_org;
        this.funding_status = funding_status;
        this.mode_of_payment = mode_of_payment;
        this.terms_assist = terms_assist;
        this.financing_instrument = financing_instrument;
        this.transaction_date = transaction_date;
    }
            
    static int extractYear(String yr) {
        try {
            return Integer.parseInt(yr);
        }
        catch(Exception e) {
            if (yr.startsWith("Fiscal Year ")) {
                yr = yr.substring(12);
                return Integer.parseInt(yr.substring(0, yr.indexOf(' ')));
            }
        }
        throw new RuntimeException("could not parse year: " + yr);
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", 
                pad(escape(amount.toString()), 20), 
                pad(escape(activityTitle), 60),
                year,
                pad(escape(month), 13),
                pad(escape(pledge), 30), 
                pad(escape(transaction_type), 30), 
                pad(escape(agreement), 30), 
                pad(escape(recipient_org), 30), 
                pad(escape(recipient_role), 30),
                pad(escape(source_role), 30),
                pad(escape(adjustment_type), 30),
                pad(escape(donor_org), 30), 
                pad(escape(funding_status), 30), 
                pad(escape(mode_of_payment), 30), 
                pad(escape(terms_assist), 30), 
                pad(escape(financing_instrument), 30),
                pad(escape(transaction_date), 10));
    }
}
