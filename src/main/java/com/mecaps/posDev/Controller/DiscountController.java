package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.DiscountRequest;
import com.mecaps.posDev.Response.DiscountResponse;
import com.mecaps.posDev.Service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/create")
    public String createDiscount(@RequestBody DiscountRequest discountRequest) {
        return discountService.createDiscount(discountRequest);
    }

    @GetMapping("/all")
    public List<DiscountResponse> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{id}")
    public DiscountResponse getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id);
    }

    @PutMapping("/update/{id}")
    public String updateDiscount(@PathVariable Long id, @RequestBody DiscountRequest discountRequest) {
        return discountService.updateDiscount(id, discountRequest);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDiscount(@PathVariable Long id) {
        return discountService.deleteDiscount(id);
    }
}
