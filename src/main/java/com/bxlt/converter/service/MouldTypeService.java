package com.bxlt.converter.service;

import com.bxlt.converter.domain.MouldType;

import java.util.List;

/**
 * @program: converter
 * @description: MouldTypeService
 * @author: zsx
 * @create: 2018-08-24 10:15
 **/
public interface MouldTypeService {

    MouldType find(String id);

    List<MouldType> findList();

    int insertOrUpdate(MouldType mouldType);
}
