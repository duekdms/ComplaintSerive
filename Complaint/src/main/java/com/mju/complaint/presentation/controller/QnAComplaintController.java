package com.mju.complaint.presentation.controller;

import com.mju.complaint.application.QnAComplaintService;
import com.mju.complaint.domain.Complaint;
import com.mju.complaint.domain.model.Result.CommonResult;
import com.mju.complaint.domain.service.ResponseService;
import com.mju.complaint.presentation.dto.ComplaintRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint-service/question")
@CrossOrigin(origins = "*")
public class QnAComplaintController {

    private final QnAComplaintService qnAComplaintService;

    private final ResponseService responseService;

    @GetMapping("/ping")
    public String ping() {
        return "paadfong";
    }
//////////////////////////////////////<문의 게시글 관리>//////////////////////////////////////////////////////////////

    //[강사진,수강생,관리자]QnA 신고 등록
    @PostMapping(value = "/register/{questionIndex}")
    public CommonResult registerQnA(@PathVariable Long questionIndex, @RequestBody ComplaintRegisterDto complaintRegisterDto/*, @RequestHeader("Authorization") String token*/) {
//        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
//        qnAComplaintDto.setComplainerIndex(complainerIndex);
        qnAComplaintService.registerQnA(questionIndex, complaintRegisterDto);
        return responseService.getSuccessfulResult();
    }

    //신고된 QnA List 조회
    @GetMapping(value = "/show/listQnA")
    public CommonResult listQnA() {
        List<Object> reportedQnAList = qnAComplaintService.getQnABoardList();
        CommonResult commonResult = responseService.getListResult(reportedQnAList);
        return commonResult;
    }
    //신고된 QnA 선택한것 조회(게시글내용)
    @GetMapping("/content/show/{reportedQnAIndex}")
    public CommonResult reportedQnAContentFindById(@PathVariable long reportedQnAIndex) {
        LinkedHashMap<String, Object> qnaContent = qnAComplaintService.getQnAContent(reportedQnAIndex);
        CommonResult commonResult = responseService.getSingleResult(qnaContent);
        return commonResult;
    }

    //신고된 QnA 선택한것 조회(신고 내용 목록)
    @GetMapping("/complaint/show/{reportedQnAIndex}")
    public CommonResult reportedQnAFindById(@PathVariable long reportedQnAIndex) {
        List<Complaint> qnaComplaintList = qnAComplaintService.getReportedQnA(reportedQnAIndex);
        CommonResult commonResult = responseService.getSingleResult(qnaComplaintList);
        return commonResult;
    }
    // 신고내역 삭제
    @DeleteMapping("/delete/{complaintIndex}")
    public CommonResult deleteComplaint(@PathVariable long complaintIndex) {
        qnAComplaintService.deleteComplaint(complaintIndex);
        return responseService.getSuccessfulResult();
    }




//    //[관리자]게시글 신고 조회
//    @PostMapping(value = "question/register/{questionIndex}")
//    public CommonResult registerQnA(@PathVariable Long questionIndex, @RequestBody QnAComplaintDto qnAComplaintDto/*, @RequestHeader("Authorization") String token*/) {
////        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
////        qnAComplaintDto.setComplainerIndex(complainerIndex);
//        complaintService.registerQnA(questionIndex, qnAComplaintDto);
//        return responseService.getSuccessfulResult();
//    }
}
