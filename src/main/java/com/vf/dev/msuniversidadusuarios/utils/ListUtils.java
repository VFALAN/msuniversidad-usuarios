package com.vf.dev.msuniversidadusuarios.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<List<T>> partition(List<T> originalList, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for(int i =0;i< originalList.size();i+=size){
            partitions.add(originalList.subList(i,Math.min(i+size, originalList.size())));
        }
        return partitions;
    }
}
