package com.example.demo.infrastructure;


/** Custom Exception that throws a message when problem occurs.
 *  The displayed message depends where in the program exception occurred.
 */
public class LoginException extends RuntimeException{

  /**
   * @param validationMessage Information about the thrown exception.
   */
    public LoginException(String validationMessage){
      super(validationMessage);
    }
}
