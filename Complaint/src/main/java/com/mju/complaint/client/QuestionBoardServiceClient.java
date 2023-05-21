package com.mju.complaint.client;

import com.mju.complaint.domain.model.Result.CommonResult;
import com.mju.complaint.domain.model.Result.SingleResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient(name = "board-service")
public interface QuestionBoardServiceClient {

    @GetMapping("/board-service/question/show/request/{questionIndex}")
    public SingleResult questionFindById(@PathVariable long questionIndex);

    @GetMapping("/board-service/commend/show/request/{commendIndex}")
    public SingleResult commendFindById(@PathVariable long commendIndex);

}
