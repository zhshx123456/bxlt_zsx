package com.bxlt.converter.domain;

import java.util.Date;

/**
 * @program: converter
 * @description: mysqlJson
 * @author: zsx
 * @create: 2018-08-24 09:50
 **/
public class MouldAttribute {
//    private String id;
//    private String typeId;
    private String maName;
    private String fieldtype;
    private String fieldexplain;

//    public String getId() {
////        return id;
////    }
////
////    public void setId(String id) {
////        this.id = id;
////    }
////
////
////    public String getTypeId() {
////        return typeId;
////    }
////
////    public void setTypeId(String typeId) {
////        this.typeId = typeId;
////    }
////
////
    public String getMaName() {
        return maName;
    }

    public void setMaName(String maName) {
        this.maName = maName;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public String getFieldexplain() {
        return fieldexplain;
    }

    public void setFieldexplain(String fieldexplain) {
        this.fieldexplain = fieldexplain;
    }
}
