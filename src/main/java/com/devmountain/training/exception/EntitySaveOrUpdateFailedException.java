package com.devmountain.training.exception;

public class EntitySaveOrUpdateFailedException extends RuntimeException {

    private static final long serialVersionUID = -156211082149877115L;

    public EntitySaveOrUpdateFailedException() {
        super();
    }

    public EntitySaveOrUpdateFailedException(String arg0) {
        super(arg0);
    }

    public EntitySaveOrUpdateFailedException(Throwable cause) {
        super(cause);
    }

    public EntitySaveOrUpdateFailedException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

}
