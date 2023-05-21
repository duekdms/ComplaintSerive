package com.mju.complaint.domain.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    NON_EXCEPTION_INDEX(-7777, "API 호출 오류 : 불러올 수 없는 요청 값입니다."),
   ////////////////////////////////문의 게시판 오류/////////////////////////////////
    SERVER_REQUEST_FAILED(-1000, "서버 요청 실패: 유효한 응답을 받지 못했습니다."),
    NON_EXCEPTION_REPORTED_QUESTION(-2000, "신고된 문의 목록이 존재하지 않습니다."),
    NOT_REPORTED_QNABOARD(-6004, "신고되지 않은 게시글을 불러오고 있습니다."),
    NON_EXCEPTION_REPORTED_COMMEND(-2100, "신고된 답변 문의 목록이 존재하지 않습니다."),
    NOT_REPORTED_COMMEND(-6004, "신고되지 않은 답변을 불러오고 있습니다.")
    ;
    private final int code;
    private final String message;
}
