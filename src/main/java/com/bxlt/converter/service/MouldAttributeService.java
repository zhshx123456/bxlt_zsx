package com.bxlt.converter.service;


import com.bxlt.converter.domain.GeoTest;
import com.bxlt.converter.domain.MouldAttribute;
import com.bxlt.converter.domain.TbbizShape;

import java.util.List;

/**
 * @program: converter
 * @description: MouldAttributeService
 * @author: zsx
 * @create: 2018-08-24 10:15
 **/
public interface MouldAttributeService {

    MouldAttribute find(String id);

    List<MouldAttribute> findList(String typeId);

   // int insertOrUpdate(MouldAttribute mouldAttribute);
}
