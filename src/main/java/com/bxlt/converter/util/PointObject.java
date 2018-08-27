package com.bxlt.converter.util;

import java.util.HashMap;

/**
 * @program: converter
 * @description: PointObject类（对应POINT）
 * @author: zsx
 * @create: 2018-08-16 15:57
 **/
public class PointObject {
    private double x;
    private double y;
    private HashMap<String, Integer> spatialReference;

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }
    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
