package com.bxlt.converter.mapper;

import com.bxlt.converter.domain.TbbizShape;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbbizShapeMapper {
    public void insert(TbbizShape tbbizShape);
    public void update(TbbizShape tbbizShape);
    public void delete(String id);
    public TbbizShape find(String id);
    public List<TbbizShape> findList();
}
