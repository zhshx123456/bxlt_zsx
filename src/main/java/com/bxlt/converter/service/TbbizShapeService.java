package com.bxlt.converter.service;


import com.bxlt.converter.domain.TbbizShape;

import java.util.List;

/**
 * @program: converter
 * @description: TbbizShapeService
 * @author: zsx
 * @create: 2018-08-14 10:15
 **/
public interface TbbizShapeService {

    TbbizShape find(String id);

    List<TbbizShape> findList();
}
