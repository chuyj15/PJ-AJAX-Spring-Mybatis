package com.human.domain;

import lombok.Data;

@Data
public class Likes {
    
    private int likeNo;
    private String parentTable;
    private int parentNo;
    private String username;

}
