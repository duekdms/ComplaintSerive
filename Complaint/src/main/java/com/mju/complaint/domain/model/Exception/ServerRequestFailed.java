package com.mju.complaint.domain.model.Exception;

public class ServerRequestFailed extends RuntimeException{
    private final ExceptionList exceptionList;

    public ServerRequestFailed(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
