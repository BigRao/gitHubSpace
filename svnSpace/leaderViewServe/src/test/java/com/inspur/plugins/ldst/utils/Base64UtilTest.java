package com.inspur.plugins.ldst.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class Base64UtilTest {

    @Test
    public void decoder() {
        Base64Util base64Util = new Base64Util();
        assertEquals("aa",Base64Util.decoder("YWE=")); ;
    }
    @Test
    public void decoder2() {
        Base64Util base64Util = new Base64Util();
        assertEquals("",Base64Util.decoder("a")); ;
    }
}