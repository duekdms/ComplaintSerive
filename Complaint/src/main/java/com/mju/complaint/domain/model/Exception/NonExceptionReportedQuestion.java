package com.mju.complaint.domain.model.Exception;

public class NonExceptionReportedQuestion extends RuntimeException{
    private final ExceptionList exceptionList;

    public NonExceptionReportedQuestion(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
