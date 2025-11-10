package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.GstTaxRequest;
import com.mecaps.posDev.Response.GstTaxResponse;
import com.mecaps.posDev.Service.GstTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gst")
@RequiredArgsConstructor
public class GstTaxController {

    private final GstTaxService gstTaxService;

    @PostMapping("/create")
    public String createGstTax(@RequestBody GstTaxRequest gstTaxRequest) {
        return gstTaxService.createGstTax(gstTaxRequest);
    }

    @GetMapping("/all")
    public List<GstTaxResponse> getAllGstTax() {
        return gstTaxService.getAllGstTax();
    }

    @GetMapping("/{id}")
    public GstTaxResponse getGstTaxById(@PathVariable Long id) {
        return gstTaxService.getGstTaxById(id);
    }

    @PutMapping("/update/{id}")
    public String updateGstTax(@PathVariable Long id, @RequestBody GstTaxRequest gstTaxRequest) {
        return gstTaxService.updateGstTax(id, gstTaxRequest);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGstTax(@PathVariable Long id) {
        return gstTaxService.deleteGstTax(id);
    }
}
