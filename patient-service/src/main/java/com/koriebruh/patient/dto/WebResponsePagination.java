package com.koriebruh.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponsePagination<T> {

    private String status;

    private T data;

    private PagingResponse paging;

}



