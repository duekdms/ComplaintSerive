package com.mju.complaint.application;


import com.mju.complaint.domain.Complaint;
import com.mju.complaint.presentation.dto.ComplaintRegisterDto;

import java.util.LinkedHashMap;
import java.util.List;

public interface CommendComplaintService {
    public void registerCommend(Long commendIndex, ComplaintRegisterDto complaintRegisterDto);

    public List<Object> getCommendList();

    public LinkedHashMap<String, Object> getCommendContent(long reportedCommendIndex);

    public List<Complaint> getReportedCommend(long reportedCommendIndex);

    public void deleteComplaint(long complaintIndex);
}
