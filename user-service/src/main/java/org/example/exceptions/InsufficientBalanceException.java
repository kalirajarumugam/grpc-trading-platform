package org.example.exceptions;

public class InsufficientBalanceException extends RuntimeException{

    private static final String MESSAGE = "User [id-%d] does not have enough funds to complete the transaction";

    public InsufficientBalanceException(Integer userId){
        super(MESSAGE.formatted(userId));
    }


}
