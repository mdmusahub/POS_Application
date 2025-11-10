package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GstTaxRequest {
    private String gst_name;
    private Double gst_rate;
    private Double c_gst;
    private Double s_gst;
    private Long category_id;
}
