package com.bxlt.converter.domain;

import com.esri.core.geometry.Geometry;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;


/**
 * @program: converter
 * @description: gettest
 * @author: zsx
 * @create: 2018-08-15 15:47
 **/
@Data
public class GeoTest {
    private String id;
    private String shape;
    private String wgs84shape;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getWgs84shape() {
        return wgs84shape;
    }

    public void setWgs84shape(String wgs84shape) {
        this.wgs84shape = wgs84shape;
    }
}
