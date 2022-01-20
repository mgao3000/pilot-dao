package com.devmountain.training.exception;

public class EntityNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1415548448625453136L;

    public EntityNotExistException() {
        super();
    }

    public EntityNotExistException(String arg0) {
        super(arg0);
    }

    public EntityNotExistException(Throwable cause) {
        super(cause);
    }

    public EntityNotExistException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

}
