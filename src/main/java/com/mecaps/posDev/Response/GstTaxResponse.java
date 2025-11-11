package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.GstTax;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GstTaxResponse {
    private String gst_name;
    private Double gst_rate;
    private Double c_gst;
    private Double s_gst;
    private String category_name;

    public GstTaxResponse(GstTax gstTax) {
        this.gst_name = gstTax.getGst_name();
        this.gst_rate = gstTax.getGst_rate();
        this.c_gst = gstTax.getC_gst();
        this.s_gst = gstTax.getS_gst();
        this.category_name = gstTax.getCategory().getCategoryName();
    }
}
