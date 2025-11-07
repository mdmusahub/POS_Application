package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.GstTaxRequest;
import com.mecaps.posDev.Response.GstTaxResponse;

import java.util.List;

public interface GstTaxService {
    String createGstTax(GstTaxRequest gstTaxRequest);
    List<GstTaxResponse> getAllGstTax();
    GstTaxResponse getGstTaxById(Long id);
    String updateGstTax(Long id, GstTaxRequest gstTaxRequest);
    String deleteGstTax(Long id);
}
