package com.mju.complaint.presentation.dto;

import com.mju.complaint.domain.Complaint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ComplaintRegisterDto {
    private String complaintContent;
    private Complaint.ComplaintType type;
}
