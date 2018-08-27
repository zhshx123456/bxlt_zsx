package com.bxlt.converter.service;

import com.bxlt.converter.domain.GeoTest;

import java.util.List;

/**
 * @program: converter
 * @description: GeoTestTService
 * @author: zsx
 * @create: 2018-08-14 10:15
 **/
public interface GeoTestService {

    GeoTest find(String id);

    List<GeoTest> findList();

    int insertOrUpdate(GeoTest geoTest);
}
