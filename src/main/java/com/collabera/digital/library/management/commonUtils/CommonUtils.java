package com.collabera.digital.library.management.commonUtils;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonUtils {
    public static Sort getSort(String sortStr){
        List<String> sortList= Arrays.stream(sortStr.split(",")).toList();
        List<Sort.Order> orders= sortList.stream().map(sort->{
            Sort.Direction direction=sort.contains("-")? Sort.Direction.DESC:Sort.Direction.ASC;
            String sortValue=sort.replaceAll("-","");
            return new Sort.Order(direction,sortValue);
        }).toList();
        return Sort.by(orders);
    }
}
