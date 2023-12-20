package com.human.domain;

import lombok.Data;

@Data
public class PageInfo {

    private int startPage;
    private int endPage;
    private int firstPage;
    private int lastPage;
    private int prev;
    private int next;
    private int totalCount;

}
