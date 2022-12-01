package com.coc.user.domo;

import org.springframework.stereotype.Component;

/**
 * @author yanHaiFeng
 * @since: 2022/11/01
 */
@Component("nan")
public class Man implements Human{
    @Override
    public void said() {
        System.out.println("i am man");
    }
}
