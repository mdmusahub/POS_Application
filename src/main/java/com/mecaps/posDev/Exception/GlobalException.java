package com.mecaps.posDev.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException  {  //  // fixed class name

    @ExceptionHandler(ProductNotFoundExpection.class)
    public ResponseEntity<ErrorResponse>  handleProductNotFound(ProductNotFoundExpection expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase() ,
                expection.getMessage(),
                httpServletRequest.getRequestURI()

        );

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProductAlreadyExist.class)

    public ResponseEntity<ErrorResponse> handleProductFound(ProductAlreadyExist exception , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),  // correct spelling of exception
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
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExist(CategoryAlreadyExist exception , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT); // correct Http error code

    }

    @ExceptionHandler(ProductInventoryNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleProductInventoryNotFound(ProductInventoryNotFoundExpection expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND) ;

    }

    @ExceptionHandler(ProductInventoryAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleProductInventoryAlreadyExist(ProductInventoryAlreadyExist exception , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT);

    }
    @ExceptionHandler(ProductVariantNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleProductVariant(ProductVariantNotFoundExpection exception , HttpServletRequest httpServletRequest){
     ErrorResponse errorResponse = new ErrorResponse(
          LocalDateTime.now(),
          HttpStatus.NOT_FOUND.value(),
          HttpStatus.NOT_FOUND.getReasonPhrase(),
          exception.getMessage(),
          httpServletRequest.getRequestURI()
     );

     return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProductVariantAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleProductVariantAlreadyExist(ProductAlreadyExist exception , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                httpServletRequest.getRequestURI()

        );

        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT) ;

    }

    @ExceptionHandler(ResourceNotFoundExpection.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundExpection exception , HttpServletRequest httpServletRequest){
     ErrorResponse errorResponse = new ErrorResponse(
             LocalDateTime.now(),
             HttpStatus.NOT_FOUND.value(),
             HttpStatus.NOT_FOUND.getReasonPhrase(),
             exception.getMessage(),
             httpServletRequest.getRequestURI()
     ) ;

    return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFound expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse= new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND) ;


    }

    @ExceptionHandler(CustomerAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExist(CustomerNotFound expection , HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse= new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                expection.getMessage(),
                httpServletRequest.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse , HttpStatus.CONFLICT) ;


    }

}
