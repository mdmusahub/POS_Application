package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.GstTax;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.GstTaxRepository;
import com.mecaps.posDev.Request.GstTaxRequest;
import com.mecaps.posDev.Response.GstTaxResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Getter
@Setter
@AllArgsConstructor
public class GstTaxServiceImpl {
    private final GstTaxRepository gstTaxRepository;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<?> createGstTax(GstTaxRequest gstTaxRequest) {
        Category category = categoryRepository.findById(gstTaxRequest.getCategory_id())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        GstTax gstTax = new GstTax();
        gstTax.setGst_name(gstTaxRequest.getGst_name());
        gstTax.setGst_rate(gstTaxRequest.getGst_rate());
        gstTax.setC_gst(gstTaxRequest.getC_gst());
        gstTax.setS_gst(gstTaxRequest.getS_gst());
        gstTax.setCategory(category);

        GstTax save = gstTaxRepository.save(gstTax);
        return new ResponseEntity<>(new GstTaxResponse(save), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllGstTax() {
        List<GstTax> gstTaxList = gstTaxRepository.findAll();

        if (gstTaxList.isEmpty()) {
            return new ResponseEntity<>("No GST tax records found", HttpStatus.NOT_FOUND);
        }

        List<GstTaxResponse> responseList = gstTaxList.stream()
                .map(GstTaxResponse::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    //  GET GST TAX BY ID
    public ResponseEntity<?> getGstTaxById(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));

        return new ResponseEntity<>(new GstTaxResponse(gstTax), HttpStatus.OK);
    }

    //  UPDATE GST TAX
    public ResponseEntity<?> updateGstTax(Long id, GstTaxRequest gstTaxRequest) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));

        // Update category (if provided)
        if (gstTaxRequest.getCategory_id() != null) {
            Category category = categoryRepository.findById(gstTaxRequest.getCategory_id())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            gstTax.setCategory(category);
        }

        gstTax.setGst_name(gstTaxRequest.getGst_name());
        gstTax.setGst_rate(gstTaxRequest.getGst_rate());
        gstTax.setC_gst(gstTaxRequest.getC_gst());
        gstTax.setS_gst(gstTaxRequest.getS_gst());

        GstTax updated = gstTaxRepository.save(gstTax);
        return new ResponseEntity<>(new GstTaxResponse(updated), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteGstTax(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));

        gstTaxRepository.delete(gstTax);
        return new ResponseEntity<>("GST tax deleted successfully", HttpStatus.OK);
    }
}

