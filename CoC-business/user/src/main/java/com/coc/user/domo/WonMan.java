package com.coc.user.domo;

import org.springframework.stereotype.Component;

/**
 * @author yanHaiFeng
 * @since: 2022/11/01
 */
@Component("nv")
public class WonMan implements Human{
    @Override
    public void said() {
        System.out.println("i am woman");
    }
}
