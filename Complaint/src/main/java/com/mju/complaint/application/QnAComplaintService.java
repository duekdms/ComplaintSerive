package com.mju.complaint.application;

import com.mju.complaint.domain.Complaint;
import com.mju.complaint.presentation.dto.ComplaintRegisterDto;

import java.util.LinkedHashMap;
import java.util.List;

public interface QnAComplaintService {
    public void registerQnA(Long questionIndex, ComplaintRegisterDto complaintRegisterDto);

    public List<Object> getQnABoardList();

    public LinkedHashMap<String, Object> getQnAContent(long reportedQnAIndex);

    public List<Complaint> getReportedQnA(long reportedQnAIndex);

    public void deleteComplaint(long complaintIndex);
}
