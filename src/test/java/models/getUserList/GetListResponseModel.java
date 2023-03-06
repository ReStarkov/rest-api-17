package models.getUserList;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetListResponseModel {
    String page,per_page,total,total_pages;
    Object support;
    ArrayList<Object> data;

}
