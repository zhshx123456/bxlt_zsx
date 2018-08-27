package com.bxlt.converter.mapper;

import com.bxlt.converter.domain.MouldType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouldTypeMapper {
    public int insert(MouldType mouldType);
    public int update(MouldType mouldType);
    public int delete(String id);
    public MouldType find(String id);
    public List<MouldType> findList();
}
