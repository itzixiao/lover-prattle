package com.itzixiao.base;

import com.itzixiao.scheduled.MyScheduled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoverPrattleApplicationTests {

    @Autowired
    MyScheduled scheduled;

    @Test
    public void dsrw() {
        scheduled.dsrw();
    }

}
