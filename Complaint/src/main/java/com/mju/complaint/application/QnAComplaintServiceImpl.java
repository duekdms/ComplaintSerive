package com.mju.complaint.application;

import com.mju.complaint.client.QuestionBoardServiceClient;
import com.mju.complaint.domain.Complaint;
import com.mju.complaint.domain.model.Exception.ExceptionList;
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
public class QnAComplaintServiceImpl implements QnAComplaintService {
    private final ComplaintRepository complaintRepository;
    private final QuestionBoardServiceClient questionBoardServiceClient;
    @Override
    @Transactional
    public void registerQnA(Long questionIndex, ComplaintRegisterDto complaintRegisterDto) {
        //Data Type: class java.util.LinkedHashMap
        SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.questionFindById(questionIndex);
        // 선택한 questionIndex의 유효성 확인
        LinkedHashMap<String, Object> data = requestResult.getData();

        if (data != null) {
            //{questionIndex=1002, questionTitle=ㅎㅎ용, questionImageList=[{imageIndex=402, imageUrl=https://iceamericano-board.s3.ap-northeast-2.amazonaws.com/57ca58ed-c5bc-45d7-a349-2d4058741130_캡처.PNG}], questionContent=테스트용ㅇ, createdAt=2023-05-08T02:05:44, updatedAt=2023-05-08T20:59:50, type=PAYMENT, goodCount=15, commendList=[]}
            Integer questionIndexInt  = (Integer) data.get("questionIndex");
            Long questionIndexValue = questionIndexInt.longValue();

            Complaint complaint = Complaint.builder()
                    .questionIndex(questionIndexValue)
                    .complaintContent(complaintRegisterDto.getComplaintContent())
                    .type(complaintRegisterDto.getType())
                    .build();
            complaintRepository.save(complaint);
        }else{
            throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
        }
    }

    @Override
    @Transactional //신고가 1번이라도 접수된적있는 문의게시글들을 중복되지않게 List로 불러오기
    public List<Object> getQnABoardList() {
        List<Complaint> questionBoardList = complaintRepository.findAllByQuestionIndexNotNull();
        List<Object> complaintQuestionList = new ArrayList<>();
        Set<Long> visitedQuestionIndexes = new HashSet<>();// 이미 가져온 questionIndex 저장을 위한 Set

        for (Complaint complaint : questionBoardList) {
            Long questionIndex = complaint.getQuestionIndex();
            // 이미 가져온 questionIndex인 경우 건너뛰기.
            if (visitedQuestionIndexes.contains(questionIndex)) {
                continue;
            }
            SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.questionFindById(questionIndex);
            LinkedHashMap<String, Object> data = requestResult.getData();
            if (data != null) {
                complaintQuestionList.add(data);
                visitedQuestionIndexes.add(questionIndex);
            }else {
                throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
            }
        }

        if (!complaintQuestionList.isEmpty()) {
            return complaintQuestionList;
        }else{
            throw new NonExceptionReportedQuestion(ExceptionList.NON_EXCEPTION_REPORTED_QUESTION);
        }
    }

    @Override
    @Transactional //questionindex가 null이아니면서 신고된 게시글인데 내가 선택한. 게시글의 내용을 불러오기
    public LinkedHashMap<String, Object> getQnAContent(long reportedQnAIndex) {
        List<Complaint> reportedQuestionList = complaintRepository.findAllByQuestionIndexNotNull();
        Complaint complaint = reportedQuestionList.stream()
                .filter(complaintComponent -> complaintComponent.getQuestionIndex() == reportedQnAIndex)
                .findFirst()
                .orElseThrow(() -> new NonExceptionReportedQuestion(ExceptionList.NOT_REPORTED_QNABOARD));

        SingleResult<LinkedHashMap<String, Object>> requestResult = questionBoardServiceClient.questionFindById(complaint.getQuestionIndex());
        LinkedHashMap<String, Object> data = requestResult.getData();
        if (data != null) {
            return data;
        }else{
            throw new ServerRequestFailed(ExceptionList.SERVER_REQUEST_FAILED);
        }
    }

    @Override
    @Transactional //신고된 questionindex에서 complaintList를 가져와야해.
    public List<Complaint> getReportedQnA(long reportedQnAIndex) {
        List<Complaint> qnaComplaintList = complaintRepository.findAllByQuestionIndexNotNullAndQuestionIndex(reportedQnAIndex);
        if(!qnaComplaintList.isEmpty()){
            return qnaComplaintList;
        }else {
            throw new NonExceptionReportedQuestion(ExceptionList.NOT_REPORTED_QNABOARD);
        }
    }

    @Override
    @Transactional
    public void deleteComplaint(long complaintIndex) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintIndex);
        if (optionalComplaint.isPresent()) {
            complaintRepository.delete(optionalComplaint.get());
        } else {
            throw new NonExceptionReportedQuestion(ExceptionList.NON_EXCEPTION_REPORTED_QUESTION);
        }
    }
}
