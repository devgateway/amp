package org.digijava.kernel.ampapi.endpoints.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.ISO8601DateSerializer;

import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
public class YearWithRange {

    @ApiModelProperty(example = "2018")
    private int year;

    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-01-01")
    private Date start;

    @JsonSerialize(using = ISO8601DateSerializer.class)
    @ApiModelProperty(example = "2018-12-31")
    private Date end;

    public YearWithRange(int year, Date start, Date end) {
        this.year = year;
        this.start = start;
        this.end = end;
    }

    public int getYear() {
        return year;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
