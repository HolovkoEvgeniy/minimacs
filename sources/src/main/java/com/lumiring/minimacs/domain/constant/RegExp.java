package com.lumiring.minimacs.domain.constant;

public class RegExp {
    public final static String username = "^[a-zA-Z0-9а-яА-Я._-]{4,50}$";
//    public final static String email = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public final static String email = "^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,63}$";
    public final static String password = "^[a-zA-Z0-9а-яА-Я.,:; _?!+=/'\\\\\"*(){}\\[\\]\\-]{8,100}$";
}
