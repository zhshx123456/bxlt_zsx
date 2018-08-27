package com.bxlt.converter.web;

import com.bxlt.converter.domain.GeoTest;
import com.bxlt.converter.domain.TbbizShape;
import com.bxlt.converter.service.GeoTestService;
import com.bxlt.converter.service.TbbizShapeService;
import org.gdal.ogr.Geometry;

import com.esri.core.geometry.*;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: converter
 * @description: TbbizShapeController
 * @author: zsx
 * @create: 2018-08-14 12:21
 **/
@EnableTransactionManagement
@RestController
@RequestMapping("/tbbiz")
public class TbbizShapeController {
    @Autowired
    private TbbizShapeService tbbizShapeService;
    @Autowired
    private GeoTestService geoTestService;

    @RequestMapping(value = "/findById/{id}",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public TbbizShape findById(@PathVariable String id){
        TbbizShape tbbizShape = tbbizShapeService.find(id);
        return tbbizShape;
    }

    @RequestMapping(value = "/findList",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public List<TbbizShape> findList(){
        List<TbbizShape> tbbizShapeList = tbbizShapeService.findList();
        return tbbizShapeList;
    }

    @RequestMapping(value = "/jsonToGeometry",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public void jsonToGeometry(){
        List<TbbizShape> tbbizShapeList = tbbizShapeService.findList();
        try {
            //for(TbbizShape ts:tbbizShapeList){
                TbbizShape ts = tbbizShapeList.get(0);
                //json转Geometry
                String jsonShape = ts.getShape();
                String jsonWgs84Shape = ts.getWgs84Shape();
                JsonFactory jsonFactory = new JsonFactory();
                JsonParser jsonParser = jsonFactory.createJsonParser(jsonShape);
                JsonParser jsonParser84 = jsonFactory.createJsonParser(jsonWgs84Shape);
                MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
                MapGeometry mapGeometry84 = GeometryEngine.jsonToGeometry(jsonParser84);
                com.esri.core.geometry.Geometry geometry = mapGeometry.getGeometry();
                com.esri.core.geometry.Geometry geometry84 = mapGeometry84.getGeometry();

                String wktString = "";
                String wktString84 = "";
                OperatorExportToWkt exporterWKT = (OperatorExportToWkt) OperatorFactoryLocal.getInstance().getOperator(OperatorExportToWkt.local().getType());
                wktString = exporterWKT.execute(0, geometry, null);
                wktString84 = exporterWKT.execute(0, geometry84, null);
               // Point mPoint = (Point) mapGeometry.getGeometry();
               // Point mPoint84 = (Point) mapGeometry84.getGeometry();

               // Geometry geomTriangle =Geometry.CreateFromWkt(wktString);

                GeoTest gt = new GeoTest();
                gt.setShape(wktString);
                gt.setWgs84shape(wktString84);
                geoTestService.insertOrUpdate(gt);
           // }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
