package com.bxlt.converter.service.impl;

import com.bxlt.converter.domain.MouldAttribute;
import com.bxlt.converter.mapper.MouldAttributeMapper;
import com.bxlt.converter.service.MouldAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @program: converter
 * @description: MouldAttributeServiceImpl
 * @author: zsx
 * @create: 2018-08-24 12:29
 **/
@Service
public class MouldAttributeServiceImpl implements MouldAttributeService {

    @Autowired
    private MouldAttributeMapper mouldAttributeMapper;

    @Override
    public MouldAttribute find(String id) {
        return mouldAttributeMapper.find(id);
    }

    @Override
    public List<MouldAttribute> findList(String typeId) {
        return mouldAttributeMapper.findList(typeId);
    }

//    @Override
//    public int insertOrUpdate(MouldAttribute mouldAttribute) {
//        int count = 0;
//        if(null==mouldAttribute.getId()){
//            String id=UUID.randomUUID().toString().replaceAll("-", "");
//            mouldAttribute.setId(id);
//            count = mouldAttributeMapper.insert(mouldAttribute);
//        }else{
//            count = mouldAttributeMapper.update(mouldAttribute);
//        }
//        return count;
//    }


}
