package com.inspur.bjzx.operationalog.service.impl;


import com.inspur.bjzx.operationalog.dao.RecodeDao;
import com.inspur.bjzx.operationalog.model.Recode;
import com.inspur.bjzx.operationalog.service.RecodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class RecodeServiceImpl implements RecodeService {

    @Resource
    private RecodeDao recodeDao;


    @Override
    public Boolean setRecodeLogs(Recode recode) {
        int res = recodeDao.setRecodeLogs(recode);
        return res == 1;
    }

}
