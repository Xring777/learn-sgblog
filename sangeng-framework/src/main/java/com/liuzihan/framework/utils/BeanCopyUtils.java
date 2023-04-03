package com.liuzihan.framework.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

//    public static <T> T copyBean(Object source,Class<T> clazz){
//        T result = null;
//        try {
//            result = clazz.newInstance();
//            BeanUtils.copyProperties(source,result);
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public static <T,R> List<R> copyBeanList(List<T> source,Class<R> clazz){
//        return source.stream()
//                .map(o -> copyBean(o, clazz))
//                .collect(Collectors.toList());
//    }

}