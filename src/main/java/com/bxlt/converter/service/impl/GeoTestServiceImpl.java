package com.bxlt.converter.service.impl;

import com.bxlt.converter.domain.GeoTest;
import com.bxlt.converter.domain.TbbizShape;
import com.bxlt.converter.mapper.GeoTestMapper;
import com.bxlt.converter.service.GeoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @program: converter
 * @description: GeoTestServiceImpl
 * @author: zsx
 * @create: 2018-08-14 12:29
 **/
@Service
public class GeoTestServiceImpl implements GeoTestService {

    @Autowired
    private GeoTestMapper geoTestMapper;

    @Override
    public GeoTest find(String id) {
        return geoTestMapper.find(id);
    }

    @Override
    public List<GeoTest> findList() {
        return geoTestMapper.findList();
    }

    @Override
    public int insertOrUpdate(GeoTest geoTest) {
        int count = 0;
        if(null==geoTest.getId()){
            String id=UUID.randomUUID().toString().replaceAll("-", "");
            geoTest.setId(id);
            count = geoTestMapper.insert(geoTest);
        }else{
            count = geoTestMapper.update(geoTest);
        }
        return count;
    }


}
