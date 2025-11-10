package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.GstTax;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.GstTaxRepository;
import com.mecaps.posDev.Request.GstTaxRequest;
import com.mecaps.posDev.Response.GstTaxResponse;
import com.mecaps.posDev.Service.GstTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GstTaxServiceImpl implements GstTaxService {

    private final GstTaxRepository gstTaxRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public String createGstTax(GstTaxRequest gstTaxRequest) {
        Category category = categoryRepository.findById(gstTaxRequest.getCategory_id())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        GstTax gstTax = new GstTax();
        gstTax.setGst_name(gstTaxRequest.getGst_name());
        gstTax.setGst_rate(gstTaxRequest.getGst_rate());
        gstTax.setC_gst(gstTaxRequest.getC_gst());
        gstTax.setS_gst(gstTaxRequest.getS_gst());
        gstTax.setCategory(category);

        gstTaxRepository.save(gstTax);
        return "GST tax created successfully";
    }

    @Override
    public List<GstTaxResponse> getAllGstTax() {
        List<GstTax> gstTaxList = gstTaxRepository.findAll();

        if (gstTaxList.isEmpty()) {
            throw new RuntimeException("No GST tax records found");
        }

        return gstTaxList.stream()
                .map(GstTaxResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public GstTaxResponse getGstTaxById(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));
        return new GstTaxResponse(gstTax);
    }

    @Override
    public String updateGstTax(Long id, GstTaxRequest gstTaxRequest) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));

        if (gstTaxRequest.getCategory_id() != null) {
            Category category = categoryRepository.findById(gstTaxRequest.getCategory_id())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            gstTax.setCategory(category);
        }

        gstTax.setGst_name(gstTaxRequest.getGst_name());
        gstTax.setGst_rate(gstTaxRequest.getGst_rate());
        gstTax.setC_gst(gstTaxRequest.getC_gst());
        gstTax.setS_gst(gstTaxRequest.getS_gst());

        gstTaxRepository.save(gstTax);
        return "GST tax updated successfully";
    }

    @Override
    public String deleteGstTax(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));

        gstTaxRepository.delete(gstTax);
        return "GST tax deleted successfully";
    }
}
