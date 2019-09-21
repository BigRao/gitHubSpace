package com.inspur.plugins.ldst.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrimZeroTest {

    @Test
    public void fomart() {
        TrimZero trimZero = new TrimZero();
        Object a = "1";
        assertEquals(a,TrimZero.format("1."));
    }
}