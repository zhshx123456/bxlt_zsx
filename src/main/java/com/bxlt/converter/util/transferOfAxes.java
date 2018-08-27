package com.bxlt.converter.util;

import com.esri.core.geometry.Point;

/**
 * @program: converter
 * @description: 坐标系转换工具
 * @author: zsx
 * @create: 2018-08-16 12:10
 **/
public class transferOfAxes {

    static final double pi = 3.14159265358979324;
    static  final double a = 6378137.0;
    static  final double ee = 0.0066943799901442;

    // WGS84经纬度坐标转换为火星经纬度坐标
    public static Point WGS2MGS(double wgLat, double wgLon)
    {
        Point point  = new Point();
        if (outOfChina(wgLat, wgLon))
        {
            point.setX(wgLon);
            point.setY(wgLat);
            return point;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        point.setX(wgLon + dLon);
        point.setY(wgLat + dLat);
        return point;
    }

    //火星经纬度坐标转换为WGS84经纬度坐标
    public static Point MGS2WGS(double mgLat, double mgLon)
    {
        Point point  = new Point();
        if (outOfChina(mgLat, mgLon))
        {
            point.setX(mgLon);
            point.setY(mgLat);
            return point;
        }
        double wgLat = mgLat;
        double wgLon = mgLon;
        double tmpmglat, tmpmglon;
        Point point1 = WGS2MGS(wgLat, wgLon);
        tmpmglat =  point1.getY();
        tmpmglon = point1.getX();
        while (true)
        {
            if (Math.abs(tmpmglat - mgLat) <= 0.000000000001 && Math.abs(tmpmglon - mgLon) <= 0.000000000001)
            {
                break;
            }
            else
            {
                wgLat = wgLat + (mgLat - tmpmglat) / 2;
                wgLon = wgLon + (mgLon - tmpmglon) / 2;
                point1 = WGS2MGS(wgLat, wgLon);
                tmpmglat =  point1.getY();
                tmpmglon = point1.getX();
            }
        }
        point.setX(wgLon);
        point.setY(wgLat);
        return point;

    }

    /**
     * 判断是否在国内，不在国内则不做偏移
     * @param lon
     * @param lat
     * @returns {boolean}
     */
    static boolean outOfChina(double lat, double lon)
    {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    static double transformLat(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

}
