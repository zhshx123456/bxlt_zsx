var map;
var untiled;
var tiled;

function init() {

    format = 'image/png';

    //default bounds of tongji
    var bounds = new OpenLayers.Bounds(
        121.4923568590001, 31.282414953000057, 121.50168630400003, 31.287686663000045);

    var options = {
        controls: [],
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection('EPSG:4326'),
        units: "m",
        maxResolution: 156543.0339,
        maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34,
           20037508.34, 20037508.34)
    };
    map = new OpenLayers.Map('map', options);

    // create　and add Google Mercator layers
    var gphy = new OpenLayers.Layer.Google(
        "　Google Physical",
        {type: google.maps.MapTypeId.TERRAIN, numZoomLevels: 22}
        );
    var gmap = new OpenLayers.Layer.Google(
        "　Google Streets",
        {numZoomLevels: 22}
        );
    var ghyb = new OpenLayers.Layer.Google(
        "　Google Hybrid",
        {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 22}
        );
    var gsat = new OpenLayers.Layer.Google(
        "　Google Satellite",  // the default , no 偏移
        {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
        );

    map.addLayers([
        gsat, gphy, gmap, ghyb
        ]);

    // add tongji layer
    xueyuan = new OpenLayers.Layer.WMS("　学院楼", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:xueyuan_google',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    daolu = new OpenLayers.Layer.WMS("　道路", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:daolu',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    jiaoxuelou = new OpenLayers.Layer.WMS("　教学楼", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:jiaoxuelou',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    lvhuadai = new OpenLayers.Layer.WMS("　绿化带", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:lvhuadai',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    otherbuilding = new OpenLayers.Layer.WMS("　其他建筑", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:otherbuilding',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    sanhaowu = new OpenLayers.Layer.WMS("　三好坞", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:sanhaowu',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    shitang = new OpenLayers.Layer.WMS("　食堂", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:shitang',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    shuiyu = new OpenLayers.Layer.WMS("　水域", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:shuiyu',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    sushe = new OpenLayers.Layer.WMS("　宿舍楼", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:sushe',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    yundongchang = new OpenLayers.Layer.WMS("　运动场", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:yundongchang',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    zhongyaojianzhu = new OpenLayers.Layer.WMS("　重要建筑", "http://localhost:8080/geoserver/tongji/wms", {
        LAYERS: 'tongji:zhongyaojianzhu',
        STYLES: '',
        format: format,
        tiled: true,
        transparent: "true",
    }, {
        opacity: 0.7,
        isBaseLayer: false
    });

    map.addLayers([xueyuan,daolu,jiaoxuelou,lvhuadai,otherbuilding,sanhaowu,shitang,sushe,yundongchang,zhongyaojianzhu]);

    var vector = new OpenLayers.Layer.Vector("　编辑");
    map.addLayer(vector);

    // build up all controls
    map.addControl(new OpenLayers.Control.PanZoomBar({
        position: new OpenLayers.Pixel(2, 15)
    }));
    map.addControl(new OpenLayers.Control.Navigation());
    map.addControl(new OpenLayers.Control.Scale());
    map.addControl(new OpenLayers.Control.MousePosition());
    map.addControl(new OpenLayers.Control.LayerSwitcher());
    map.addControl(new OpenLayers.Control.EditingToolbar(vector));

    var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
    var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    map.zoomToExtent(bounds.transform( fromProjection, toProjection));

    map.events.register("click", map, onMapClick);

}

function onMapClick(e){
    var params = {
        REQUEST:        "GetFeatureInfo",                // WMS GetFeatureInfo
        EXCEPTIONS:      "application/vnd.ogc.se_xml",    // Exception 类型
        BBOX:            map.getExtent().toBBOX(),       // 地图的地图范围
        SERVICE: "WMS",
        INFO_FORMAT: 'text/html',
        QUERY_LAYERS: 'tongji:xueyuan_google',
        FEATURE_COUNT: 50,
        Layers: 'tongji:xueyuan_google',
        WIDTH: map.size.w,
        HEIGHT: map.size.h,
        format: format,
        styles: "",
        srs: "EPSG:900913",
        X:               parseInt(e.xy.x),                         // 屏幕坐标X
        Y:               parseInt(e.xy.y)                         // 屏幕坐标X
    };
    OpenLayers.loadURL("http://localhost:8080/geoserver/tongji/wms?", params, this, onComplete, onFailure);
    OpenLayers.Event.stop(e);
}

function onComplete (response){
   document.getElementById('nodelist').innerHTML = response.responseText;
}

function onFailure (response){
   document.getElementById('nodelist').innerHTML = response.responseText;
}

function updateFilter(){
  var filterType = document.getElementById('filterType').value;
  var filter = document.getElementById('filter').value;

  var filterParams = {
    filter: null,
    cql_filter: null,
    featureId: null
};
if (OpenLayers.String.trim(filter) != "") {
    if (filterType == "cql")
        filterParams["cql_filter"] = filter;
    if (filterType == "ogc")
        filterParams["filter"] = filter;
    if (filterType == "fid")
        filterParams["featureId"] = filter;
}

mergeNewParams(filterParams);
}

function resetFilter() {
    document.getElementById('filter').value = "";
    updateFilter();
}

function mergeNewParams(params){
    xueyuan.mergeNewParams(params);
}
