package com.bxlt.converter.service.impl;

import com.bxlt.converter.domain.TbbizShape;
import com.bxlt.converter.mapper.TbbizShapeMapper;
import com.bxlt.converter.service.TbbizShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: converter
 * @description: TbbizShapeServiceImpl
 * @author: zsx
 * @create: 2018-08-14 12:29
 **/
@Service
public class TbbizShapeServiceImpl implements TbbizShapeService {

    @Autowired
    private TbbizShapeMapper tbbizShapeMapper;

    @Override
    public TbbizShape find(String id) {
        return tbbizShapeMapper.find(id);
    }

    @Override
    public List<TbbizShape> findList() {
        return tbbizShapeMapper.findList();
    }
}
