package com.edurt.sli.slismps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Integer id;
    private String userName;
    private String title;

}
