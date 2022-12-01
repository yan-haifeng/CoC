package com.coc.user.domo;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author yanHaiFeng
 * @since: 2022/11/01
 */
@Component
public class HumanTask {
    private final Collection<Human> humanQueue;

    public HumanTask(Collection<Human> humanQueue) {
        this.humanQueue = humanQueue;
        System.out.println("加载");
    }

    public Collection<Human> getMap() {
        return humanQueue;
    }

    @Override
    public String toString() {
        return "HumanTask{" +
                "humanList=" + humanQueue +
                '}';
    }
}
