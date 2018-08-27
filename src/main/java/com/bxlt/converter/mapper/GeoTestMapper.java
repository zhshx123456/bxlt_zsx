package com.bxlt.converter.mapper;

import com.bxlt.converter.domain.GeoTest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoTestMapper {
    public int insert(GeoTest geoTest);
    public int update(GeoTest geoTest);
    public int delete(String id);
    public GeoTest find(String id);
    public List<GeoTest> findList();
}
