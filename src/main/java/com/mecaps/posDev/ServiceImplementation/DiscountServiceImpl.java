package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Discount;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.DiscountRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.DiscountRequest;
import com.mecaps.posDev.Response.DiscountResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@AllArgsConstructor
public class DiscountServiceImpl {

    private final DiscountRepository discountRepository;
    private final ProductVariantRepository productVariantRepository;

    //  CREATE Discount
    public ResponseEntity<?> createDiscount(DiscountRequest discountRequest) {
        ProductVariant variant = productVariantRepository.findById(discountRequest.getProduct_variant())
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found"));

        Discount discount = new Discount();
        discount.setDiscount_name(discountRequest.getDiscount_name());
        discount.setDiscount_value(discountRequest.getDiscount_value());
        discount.setStart_date_time(discountRequest.getStart_date_time());
        discount.setEnd_date_time(discountRequest.getEnd_date_time());
        discount.setIs_active(discountRequest.getIs_active());
        discount.setWaiver_mode(discountRequest.getWaiver_mode());
        discount.setProductVariant(variant);

        Discount save = discountRepository.save(discount);
        return new ResponseEntity<>(new DiscountResponse(save), HttpStatus.CREATED);
    }

    //  GET ALL Discounts
    public ResponseEntity<?> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();

        if (discounts.isEmpty()) {
            return new ResponseEntity<>("No discounts found", HttpStatus.NOT_FOUND);
        }

        List<DiscountResponse> responseList = discounts.stream()
                .map(DiscountResponse::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    //  GET Discount BY ID
    public ResponseEntity<?> getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + id));

        return new ResponseEntity<>(new DiscountResponse(discount), HttpStatus.OK);
    }

    //  UPDATE Discount
    public ResponseEntity<?> updateDiscount(Long id, DiscountRequest discountRequest) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + id));

        if (discountRequest.getProduct_variant() != null) {
            ProductVariant variant = productVariantRepository.findById(discountRequest.getProduct_variant())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found"));
            discount.setProductVariant(variant);
        }

        discount.setDiscount_name(discountRequest.getDiscount_name());
        discount.setDiscount_value(discountRequest.getDiscount_value());
        discount.setStart_date_time(discountRequest.getStart_date_time());
        discount.setEnd_date_time(discountRequest.getEnd_date_time());
        discount.setIs_active(discountRequest.getIs_active());
        discount.setWaiver_mode(discountRequest.getWaiver_mode());

        Discount updated = discountRepository.save(discount);
        return new ResponseEntity<>(new DiscountResponse(updated), HttpStatus.OK);
    }

    //  DELETE Discount
    public ResponseEntity<?> deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + id));

        discountRepository.delete(discount);
        return new ResponseEntity<>("Discount deleted successfully", HttpStatus.OK);
    }
}
