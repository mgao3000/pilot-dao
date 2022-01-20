package com.devmountain.training.exception;

public class EntityCannotBeDeletedDueToNonEmptyChildrenException extends RuntimeException {

    private static final long serialVersionUID = -4840411356076665603L;

    public EntityCannotBeDeletedDueToNonEmptyChildrenException() {
        super();
    }

    public EntityCannotBeDeletedDueToNonEmptyChildrenException(String arg0) {
        super(arg0);
    }

    public EntityCannotBeDeletedDueToNonEmptyChildrenException(Throwable cause) {
        super(cause);
    }

    public EntityCannotBeDeletedDueToNonEmptyChildrenException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

}
