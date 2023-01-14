package com.cs7eric.eatmore.common;

/**
 * 自定义异常类 处理
 *
 * @author cs7eric
 * @date 2023/01/14
 */
public class CustomException extends RuntimeException{

    public CustomException (String message){
        super(message);
    }

}
