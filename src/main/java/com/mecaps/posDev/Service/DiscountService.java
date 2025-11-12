package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.DiscountRequest;
import com.mecaps.posDev.Response.DiscountResponse;

import java.util.List;

public interface DiscountService {
    String createDiscount(DiscountRequest discountRequest);
    List<DiscountResponse> getAllDiscounts();
    DiscountResponse getDiscountById(Long id);
    String updateDiscount(Long id, DiscountRequest discountRequest);
    String deleteDiscount(Long id);
}
