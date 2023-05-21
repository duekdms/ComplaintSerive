package com.mju.complaint.domain.repository;

import com.mju.complaint.domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    public List<Complaint> findAllByQuestionIndexNotNull();

    //QuestionIndex가 null이 아니고 동시에 주어진 reportedQnAIndex와 동일한 값을 가지는 Complaint 엔티티를 조회
    public List<Complaint> findAllByQuestionIndexNotNullAndQuestionIndex(long reportedQnAIndex);

    public List<Complaint> findAllByCommendIndexNotNull();

    //CommendIndex가 null이 아니고 동시에 주어진 reportedCommendIndex와 동일한 값을 가지는 Complaint 엔티티를 조회
    public List<Complaint> findAllByCommendIndexNotNullAndCommendIndex(long reportedCommendIndex);
}
