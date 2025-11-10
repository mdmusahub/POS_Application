package com.mecaps.posDev.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class FullResponse {
    private ProductResponse productResponse;
    List<ViResponse>  viResponseList ;

}
