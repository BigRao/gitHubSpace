package com.inspur.plugins.ldst.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class WildCardTest {
    @Test
    public void test() {
        WildCard wildCard = new WildCard();
        Object a = "a%";
        assertEquals(a,WildCard.withWildCard("a"));
    }
}