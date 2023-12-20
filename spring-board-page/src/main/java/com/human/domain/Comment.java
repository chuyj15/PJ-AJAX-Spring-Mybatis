package com.human.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Comment {
    
    private int commentNo;
    private String parentTable;
    private int parentNo;
    private String writer;
    private String content;
    private Date regDate;
    private Date updDate;

}
