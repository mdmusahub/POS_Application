package com.mecaps.posDev.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GobalExpection {

    @ExceptionHandler(ProductNotFoundExpection.class)
    public ResponseEntity<ErrorResponse>  handleProductNotFound(ProductNotFoundExpection expextion , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase() ,
                expextion.getMessage(),
                httpServletRequest.getRequestURI()

        );

        return new ResponseEntity(errorResponse , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProductAlreadyExist.class)

    public ResponseEntity<ErrorResponse> handleProductFound(ProductAlreadyExist expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()

        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT) ;

    }

    @ExceptionHandler(CategoryNotFoundException.class)

    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException exception , HttpServletRequest httpServletRequest){
     ErrorResponse errorResponse=new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        exception.getMessage(),
        httpServletRequest.getRequestURI()
        );

         return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND) ;

    }

    @ExceptionHandler(CategoryAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExist(CategoryAlreadyExist expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT);

    }

    @ExceptionHandler(ProductInventoryNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleProductInventoryNotFound(ProductInventoryNotFoundExpection expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND) ;

    }

    @ExceptionHandler(ProductInventoryAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleProductInventoryAlreadyExist(ProductInventoryAlreadyExist expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(ProductVariantNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleProductVariant(ProductVariantNotFoundExpection expection , HttpServletRequest httpServletRequest){
     ErrorResponse errorResponse=new ErrorResponse(
          LocalDateTime.now(),
          HttpStatus.NOT_FOUND.value(),
          HttpStatus.NOT_FOUND.getReasonPhrase(),
          expection.getMessage(),
          httpServletRequest.getRequestURI()
     );

     return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProductVariantAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleProductVariantAlreadyExist(ProductAlreadyExist expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()

        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT) ;

    }

    @ExceptionHandler(ResourceNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundExpection expection , HttpServletRequest httpServletRequest){
     ErrorResponse errorResponse=new ErrorResponse(
             LocalDateTime.now(),
             HttpStatus.NOT_FOUND.value(),
             HttpStatus.NOT_FOUND.getReasonPhrase(),
             expection.getMessage(),
             httpServletRequest.getRequestURI()
     ) ;

    return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }


}
