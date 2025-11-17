package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
private Long product_id;
private Long product_variant_id;
private Long quantity;
}
