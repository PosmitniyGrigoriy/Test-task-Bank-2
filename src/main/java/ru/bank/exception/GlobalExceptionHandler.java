package ru.bank.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationIsNotViaJWTException.class)
    public ResponseEntity<String> handleAuthenticationIsNotViaJWTException(AuthenticationIsNotViaJWTException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(TokenDurationIncorrectException.class)
    public ResponseEntity<String> handleTokenDurationIncorrectException(TokenDurationIncorrectException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<String> handleEntityDoesNotExistException(EntityDoesNotExistException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FieldInPersonalDataIsIncorrectlyFilledInException.class)
    public ResponseEntity<String> handleFieldInPersonalDataIsIncorrectlyFilledInException(
            FieldInPersonalDataIsIncorrectlyFilledInException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmountIsSpecifiedIncorrectlyException.class)
    public ResponseEntity<String> handleAmountIsSpecifiedIncorrectlyException(AmountIsSpecifiedIncorrectlyException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBalanceIncreaseArgumentsException.class)
    public ResponseEntity<String> handleInvalidBalanceIncreaseArgumentsException(InvalidBalanceIncreaseArgumentsException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankOperationStatusIsIncorrectlySpecifiedException.class)
    public ResponseEntity<String> handleBankOperationStatusIsIncorrectlySpecifiedException(
            BankOperationStatusIsIncorrectlySpecifiedException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContactDataEmptyException.class)
    public ResponseEntity<String> handleContactDataEmptyException(ContactDataEmptyException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContactDataException.class)
    public ResponseEntity<String> handleInvalidContactDataException(InvalidContactDataException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MustBeObjectsInCollectionsException.class)
    public ResponseEntity<String> handleMustBeObjectsInCollectionsException(MustBeObjectsInCollectionsException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectContactDeletionDataException.class)
    public ResponseEntity<String> handleIncorrectContactDeletionDataException(IncorrectContactDeletionDataException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ContactDataAlreadyInUseException.class)
    public ResponseEntity<String> handleContactDataAlreadyInUseException(ContactDataAlreadyInUseException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginBusyException.class)
    public ResponseEntity<String> handleLoginBusyException(LoginBusyException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

}
