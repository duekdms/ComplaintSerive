package com.mju.complaint.presentation.controller;

import com.mju.complaint.application.CommendComplaintService;
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
@RequestMapping("/complaint-service/commend")
@CrossOrigin(origins = "*")
public class CommendComplaintController {

    private final CommendComplaintService commendComplaintService;

    private final ResponseService responseService;

    @GetMapping("/ping")
    public String ping() {
        return "paadfong";
    }
//////////////////////////////////////<답변 관리>//////////////////////////////////////////////////////////////

    //[강사진,수강생,관리자]답변 신고 등록
    @PostMapping(value = "/register/{commendIndex}")
    public CommonResult registerCommend(@PathVariable Long commendIndex, @RequestBody ComplaintRegisterDto complaintRegisterDto/*, @RequestHeader("Authorization") String token*/) {
//        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
//        qnAComplaintDto.setComplainerIndex(complainerIndex);
        commendComplaintService.registerCommend(commendIndex, complaintRegisterDto);
        return responseService.getSuccessfulResult();
    }

    //신고된 답변 List 조회
    @GetMapping(value = "/show/listQnA")
    public CommonResult listQnA() {
        List<Object> reportedCommendList = commendComplaintService.getCommendList();
        CommonResult commonResult = responseService.getListResult(reportedCommendList);
        return commonResult;
    }
    //신고된 답변 선택한것 조회(답변 내용)
    @GetMapping("/content/show/{reportedCommendIndex}")
    public CommonResult reportedCommendContentFindById(@PathVariable long reportedCommendIndex) {
        LinkedHashMap<String, Object> CommendContent = commendComplaintService.getCommendContent(reportedCommendIndex);
        CommonResult commonResult = responseService.getSingleResult(CommendContent);
        return commonResult;
    }

    //신고된 답변 선택한것 조회(신고 내용 목록)
    @GetMapping("/complaint/show/{reportedCommendIndex}")
    public CommonResult reportedQnAFindById(@PathVariable long reportedCommendIndex) {
        List<Complaint> commendComplaintList = commendComplaintService.getReportedCommend(reportedCommendIndex);
        CommonResult commonResult = responseService.getSingleResult(commendComplaintList);
        return commonResult;
    }
    // 신고내역 삭제
    @DeleteMapping("/delete/{complaintIndex}")
    public CommonResult deleteComplaint(@PathVariable long complaintIndex) {
        commendComplaintService.deleteComplaint(complaintIndex);
        return responseService.getSuccessfulResult();
    }



}
