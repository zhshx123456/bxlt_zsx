package com.bxlt.converter.mapper;

import com.bxlt.converter.domain.MouldAttribute;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouldAttributeMapper {
    public int insert(MouldAttribute mouldAttribute);
    public int update(MouldAttribute mouldAttribute);
    public int delete(String id);
    public MouldAttribute find(String id);
    public List<MouldAttribute> findList(String typeId);
}
