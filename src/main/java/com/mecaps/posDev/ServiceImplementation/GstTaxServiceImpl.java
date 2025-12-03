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

/**
 * Service implementation for managing GST (Goods & Services Tax) records.
 * <p>
 * Handles creating, updating, retrieving, and deleting GST tax information
 * associated with product categories.
 */
@Service
@RequiredArgsConstructor
public class GstTaxServiceImpl implements GstTaxService {

    private final GstTaxRepository gstTaxRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Creates a new GST tax entry for a specific category.
     *
     * @param gstTaxRequest the request containing GST details
     * @return success message after saving GST data
     * @throws CategoryNotFoundException if the given category does not exist
     */
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

    /**
     * Retrieves all GST tax records stored in the system.
     *
     * @return list of GST tax responses
     * @throws RuntimeException if no GST records are found
     */
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

    /**
     * Retrieves a GST tax record by its ID.
     *
     * @param id the GST tax ID
     * @return GST tax response for the given ID
     * @throws RuntimeException if no GST record is found
     */
    @Override
    public GstTaxResponse getGstTaxById(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));
        return new GstTaxResponse(gstTax);
    }

    /**
     * Updates an existing GST tax entry.
     *
     * @param id            the ID of the GST record to update
     * @param gstTaxRequest the updated GST data
     * @return success message after updating
     * @throws RuntimeException         if GST record is not found
     * @throws CategoryNotFoundException if new category ID is invalid
     */
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

    /**
     * Deletes a GST tax record by ID.
     *
     * @param id the ID of the GST entry to delete
     * @return success message after deletion
     * @throws RuntimeException if the GST record does not exist
     */
    @Override
    public String deleteGstTax(Long id) {
        GstTax gstTax = gstTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GST tax not found with ID: " + id));
        gstTaxRepository.delete(gstTax);
        return "GST tax deleted successfully";
    }
}
