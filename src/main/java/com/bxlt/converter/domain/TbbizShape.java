package com.bxlt.converter.domain;

/**
 * @program: switch2geometry
 * @description: mysqlJson
 * @author: zsx
 * @create: 2018-08-14 09:50
 **/
public class TbbizShape {
    private String id;
    private String shape;
    private String wgs84Shape;

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

    public String getWgs84Shape() {
        return wgs84Shape;
    }

    public void setWgs84Shape(String wgs84Shape) {
        this.wgs84Shape = wgs84Shape;
    }
}
