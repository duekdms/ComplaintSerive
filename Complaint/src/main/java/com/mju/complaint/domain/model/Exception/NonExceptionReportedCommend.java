package com.mju.complaint.domain.model.Exception;

public class NonExceptionReportedCommend extends RuntimeException{
    private final ExceptionList exceptionList;

    public NonExceptionReportedCommend(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
