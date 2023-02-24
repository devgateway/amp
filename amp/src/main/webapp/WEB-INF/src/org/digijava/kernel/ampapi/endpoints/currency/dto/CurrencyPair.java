package org.digijava.kernel.ampapi.endpoints.currency.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class CurrencyPair {

    @ApiModelProperty(example = "EUR")
    private final String from;

    @ApiModelProperty(example = "USD")
    private final String to;

    public CurrencyPair(String from, String to) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(from, that.from)
                && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
