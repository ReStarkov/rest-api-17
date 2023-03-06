package models.getlist;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GetListResponseModel {
    String page,per_page,total,total_pages;
    Object support;
    ArrayList<Object> data;

}
