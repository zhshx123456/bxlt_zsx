package com.bxlt.converter.util;

import java.util.HashMap;
import java.util.List;

/**
 * @program: converter
 * @description: LineStringObject类（对应 LineString）
 * @author: zsx
 * @create: 2018-08-16 15:59
 **/
public class LineStringObject {
    private List<List<Double[]>> paths;
    private HashMap<String, Integer> spatialReference;

    public List<List<Double[]>> getPaths() {
        return paths;
    }
    public void setPaths(List<List<Double[]>> paths) {
        this.paths = paths;
    }
    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }
    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
