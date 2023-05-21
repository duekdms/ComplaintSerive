package com.mju.complaint.application;

import com.mju.complaint.client.QuestionBoardServiceClient;
import com.mju.complaint.domain.Complaint;
import com.mju.complaint.domain.model.Exception.ExceptionList;
import com.mju.complaint.domain.model.Exception.NonExceptionReportedCommend;
import com.mju.complaint.domain.model.Exception.NonExceptionReportedQuestion;
import com.mju.complaint.domain.model.Exception.ServerRequestFailed;
import com.mju.complaint.domain.model.Result.SingleResult;
import com.mju.complaint.domain.repository.ComplaintRepository;
import com.mju.complaint.presentation.dto.ComplaintRegisterDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class CommendComplaintServiceImpl implements CommendComplaintService {
    private final ComplaintRepository complaintRepository;
    private final QuestionBoardServiceClient questionBoardServiceClient;
    @Override
    @Transactional
    public void registerCommend(Long commendIndex, ComplaintRegisterDto complaintRegisterDto) {
        //Data Type: class java.util.LinkedHashMap
        SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.commendFindById(commendIndex);
        // 선택한 questionIndex의 유효성 확인
        LinkedHashMap<String, Object> data = requestResult.getData();

        if (data != null) {
            //{commendIndex=158, commendContent=수정수정정, createdAt=2023-05-11T21:20:33, updatedAt=2023-05-11T21:30:20, goodCount=7, questionIndex=1002}
            Integer questionIndexInt  = (Integer) data.get("questionIndex");
            Integer commendIndexInt  = (Integer) data.get("commendIndex");
            Long questionIndexValue = questionIndexInt.longValue();
            Long commendIndexValue = commendIndexInt.longValue();

            Complaint complaint = Complaint.builder()
                    .questionIndex(questionIndexValue)
                    .commendIndex(commendIndexValue)
                    .complaintContent(complaintRegisterDto.getComplaintContent())
                    .type(complaintRegisterDto.getType())
                    .build();
            complaintRepository.save(complaint);
        }else{
            throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
        }
    }

    @Override
    @Transactional //신고가 1번이라도 접수된적있는 답변들을 중복되지않게 List로 불러오기
    public List<Object> getCommendList() {
        List<Complaint> complaintCommendList = complaintRepository.findAllByCommendIndexNotNull();
        List<Object> reportedCommendList = new ArrayList<>();
        Set<Long> visitedCommendIndexes = new HashSet<>();// 이미 가져온 questionIndex 저장을 위한 Set

        for (Complaint complaint : complaintCommendList) {
            Long commendIndex = complaint.getCommendIndex();

            if (visitedCommendIndexes.contains(commendIndex)) {
                continue;
            }
            SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.commendFindById(commendIndex);
            LinkedHashMap<String, Object> data = requestResult.getData();
            if (data != null) {
                reportedCommendList.add(data);
                visitedCommendIndexes.add(commendIndex);
            }else {
                throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
            }
        }

        if (!reportedCommendList.isEmpty()) {
            return reportedCommendList;
        }else{
            throw new NonExceptionReportedCommend(ExceptionList.NON_EXCEPTION_REPORTED_COMMEND);
        }
    }

    @Override
    @Transactional //questionindex가 null이아니면서 신고된 게시글인데 내가 선택한. 게시글의 내용을 불러오기
    public LinkedHashMap<String, Object> getCommendContent(long reportedCommendIndex) {
        List<Complaint> complaintCommendList = complaintRepository.findAllByCommendIndexNotNull();
        Complaint complaint = complaintCommendList.stream()
                .filter(complaintComponent -> complaintComponent.getCommendIndex() == reportedCommendIndex)
                .findFirst()
                .orElseThrow(() -> new NonExceptionReportedQuestion(ExceptionList.NOT_REPORTED_COMMEND));

        SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.commendFindById(complaint.getCommendIndex());
        LinkedHashMap<String, Object> data = requestResult.getData();
        if (data != null) {
            return data;
        }else{
            throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
        }
    }

    @Override
    @Transactional //신고된 commendIndex에서 complaintList를 가져와야해.
    public List<Complaint> getReportedCommend(long reportedCommendIndex) {
        List<Complaint> commendComplaintList = complaintRepository.findAllByCommendIndexNotNullAndCommendIndex(reportedCommendIndex);
        if(!commendComplaintList.isEmpty()){
            return commendComplaintList;
        }else {
            throw new NonExceptionReportedQuestion(ExceptionList.NOT_REPORTED_COMMEND);
        }
    }

    @Override
    @Transactional
    public void deleteComplaint(long complaintIndex) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintIndex);
        if (optionalComplaint.isPresent()) {
            complaintRepository.delete(optionalComplaint.get());
        } else {
            throw new NonExceptionReportedQuestion(ExceptionList.NON_EXCEPTION_REPORTED_COMMEND);
        }
    }
}
