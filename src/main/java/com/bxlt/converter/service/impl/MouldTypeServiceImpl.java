package com.bxlt.converter.service.impl;

import com.bxlt.converter.domain.MouldAttribute;
import com.bxlt.converter.domain.MouldType;
import com.bxlt.converter.mapper.MouldAttributeMapper;
import com.bxlt.converter.mapper.MouldTypeMapper;
import com.bxlt.converter.service.MouldAttributeService;
import com.bxlt.converter.service.MouldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @program: converter
 * @description: MouldTypeServiceImpl
 * @author: zsx
 * @create: 2018-08-24 12:29
 **/
@Service
public class MouldTypeServiceImpl implements MouldTypeService {

    @Autowired
    private MouldTypeMapper mouldTypeMapper;

    @Override
    public MouldType find(String id) {
        return mouldTypeMapper.find(id);
    }

    @Override
    public List<MouldType> findList() {
        return mouldTypeMapper.findList();
    }

    @Override
    public int insertOrUpdate(MouldType mouldType) {
        int count = 0;
        if(null==mouldType.getId()){
            String id=UUID.randomUUID().toString().replaceAll("-", "");
            mouldType.setId(id);
            count = mouldTypeMapper.insert(mouldType);
        }else{
            count = mouldTypeMapper.update(mouldType);
        }
        return count;
    }


}
