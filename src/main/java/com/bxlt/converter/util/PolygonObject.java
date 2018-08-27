package com.bxlt.converter.util;

import java.util.HashMap;
import java.util.List;

/**
 * @program: converter
 * @description: PolygonObject类（对应 Polygon 和 MULTIPOLYGON）
 * @author: zsx
 * @create: 2018-08-16 16:00
 **/
public class PolygonObject {
    private List<List<Double[]>> rings;
    private HashMap<String, Integer> spatialReference;

    public List<List<Double[]>> getRings() {
        return rings;
    }
    public void setRings(List<List<Double[]>> rings) {
        this.rings = rings;
    }
    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }
    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
