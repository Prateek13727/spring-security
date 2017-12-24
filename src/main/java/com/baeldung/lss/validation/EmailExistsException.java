package com.baeldung.lss.validation;

/**
 * Created by tracxn-lp-175 on 24/12/17.
 */
public class EmailExistsException extends Throwable {

    public EmailExistsException(final String message) {
        super(message);
    }

}
