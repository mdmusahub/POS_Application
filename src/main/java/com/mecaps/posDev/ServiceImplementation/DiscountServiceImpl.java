package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Discount;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.DiscountRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.DiscountRequest;
import com.mecaps.posDev.Response.DiscountResponse;
import com.mecaps.posDev.Service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductVariantRepository productVariantRepository;

    //  CREATE Discount
    @Override
    public String createDiscount(DiscountRequest discountRequest) {
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

        discountRepository.save(discount);
        return "Discount created successfully";
    }

    //  GET ALL Discounts
    @Override
    public List<DiscountResponse> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();

        if (discounts.isEmpty()) {
            throw new ResourceNotFoundException("No discounts found");
        }

        return discounts.stream()
                .map(DiscountResponse::new)
                .collect(Collectors.toList());
    }

    //  GET Discount BY ID
    @Override
    public DiscountResponse getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + id));

        return new DiscountResponse(discount);
    }

    //  UPDATE Discount
    @Override
    public String updateDiscount(Long id, DiscountRequest discountRequest) {
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

        discountRepository.save(discount);
        return "Discount updated successfully";
    }

    //  DELETE Discount
    @Override
    public String deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with ID: " + id));

        discountRepository.delete(discount);
        return "Discount deleted successfully";
    }
}
