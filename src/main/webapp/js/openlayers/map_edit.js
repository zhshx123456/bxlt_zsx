var aimsMap = {};
aimsMap.DefaultProperty  = {shapestatus:'Y',bizType:1,shpType:0};
/**
 * 连接地址
 */
aimsMap.url = {
	imgurl: "http://mt{0-3}.google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=Ga",
	labelUrl: "http://mt{0-3}.google.cn/vt/imgtp=png32&lyrs=h@169000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=",
	terrainulr: "http://mt{0-3}.google.cn/vt/lyrs=t@131,r@227000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=",
	vectorulr: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer/tile/{z}/{y}/{x}",
    wfsUrl:"http://192.168.2.117:8080/geoserver/geoTest/ows?service=WFS&version=1.0.0&request=GetFeature&" +
    "typeName=geoTest:random_data&maxFeatures=50&outputFormat=application/json&srsname=EPSG:4326"
}

/**
 * 页面部分js
 */
function caclSize() {
	var total = document.documentElement.clientHeight;
	var colHeight = total - document.getElementById('map').offsetTop - 40;
	$("#map").height(colHeight);
}
caclSize();
/**
 * 获取uuid
 */
function generateUUID() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random() * 16) % 16 | 0;
		d = Math.floor(d / 16);
		return(c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	});
	return uuid;
};
/**
 * 坐标格式转换
 */
var formator = {};
formator.formatLength = function(line, isGeo) {
	var length;
	if(isGeo) {
		var coordinates = line.getCoordinates();
		length = 0;
		var sourceProj = map.getView().getProjection();
		for(var i = 0, ii = coordinates.length - 1; i < ii; ++i) {
			var c1 = ol.proj.transform(coordinates[i], sourceProj, 'EPSG:4326');
			var c2 = ol.proj.transform(coordinates[i + 1], sourceProj, 'EPSG:4326');
			length += wgs84Sphere.haversineDistance(c1, c2);
		}
	} else {
		length = Math.round(line.getLength() * 100) / 100;
	}
	var output;
	if(length > 100) {
		output = (Math.round(length / 1000 * 100) / 100) +
			' ' + 'km';
	} else {
		output = (Math.round(length * 100) / 100) +
			' ' + 'm';
	}
	return output;
};

formator.formatArea = function(geometry, isGeo) {
	var area;
	area = Math.abs(ol.sphere.getArea(geometry));
	return area;
};

formator.toEsriJson = function(feature) {
	var spatialReference = {
		"wkid": 102113,
		"latestWkid": 3785
	};
	var outJson = {};
	var parser = new ol.format.EsriJSON();
	var json = parser.writeFeatures([feature]);
	json = eval("(" + json + ")");
	var features = json.features;
	if(features.length > 0) {
		var geometry = features[0].geometry;
		if(geometry.rings) { //if the shape is polygon
			var rings = geometry.rings;
			outJson.rings = rings;
			outJson.spatialReference = spatialReference;
			return outJson;
		} else { //if the shape is point
			var point = geometry;
			outJson.x = point.x;
			outJson.y = point.y;
			outJson.spatialReference = spatialReference;
			return outJson;
		}
	}
	return undefined;
}
//百度坐标系转谷歌坐标系
function bd_decrypt(bd_lat, bd_lon) {

	var x_pi = bd_lat * bd_lon / 180.0;
	var x = bd_lon - 0.0065,
		y = bd_lat - 0.006;
	var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	var gg_lon = z * Math.cos(theta);
	var gg_lat = z * Math.sin(theta);

	return [gg_lat, gg_lon];
}

/**
 * 数据格式转换
 */
var dataFormat = {};
dataFormat.toWKT = function(feature){
	var wkt = new ol.format.WKT();
	return wkt.writeGeometryText(feature.getGeometry());
}
/**
 * 地图编辑部分js
 */
aimsMap.defaultConfig = {
	url: aimsMap.url.imgurl,
	mapExtent: [-2.0037508342787E7, -2.0037508342787E7, 2.0037508342787E7, 2.0037508342787E7],
	mapMinZoom: 1,
	mapMaxZoom: 19,
	attribution: new ol.control.Attribution({
		html: 'Tiles ? GoogleMap',
	}),
	tilePixelRatio: 1,
	fromProject: new ol.proj.Projection('ESPG:3857'),
	toProject: new ol.proj.Projection('ESPG:4326'),
	visible: true
};
var options = jQuery.extend({}, aimsMap.defaultConfig);
var layer0 = new ol.layer.Tile({
	/**
	 * 在openlayer3中，fromProject:'ESPG:3857'
	 * 在openlayer5中，fromProject:new ol.proj.Project('ESPG:3857')
	 */
	extent: ol.proj.transformExtent(options.mapExtent, options.fromProject, options.toProject),
	source: new ol.source.XYZ({
		attributions: [options.attribution],
		url: options.url,
		tilePixelRatio: options.tilePixelRatio,
		minZoom: options.mapMinZoom,
		maxZoom: options.mapMaxZoom
	})
});
aimsMap.source = new ol.source.Vector();
aimsMap.source = new ol.source.Vector({
    format: new ol.format.GeoJSON({
        /**
         * 将shp格式矢量文件导入PostgreGIS数据库中，
         * 对应的表中增加了一个字段名为geom的字段，
         * 所有这里的名称就是数据库表中增加的那个字段名称
         *
         * PostgreGIS:xian_polygon为工作空间：图层名
         */
        geometryName: 'geom'
    }),
    url: aimsMap.url.wfsUrl
});
var layer1 = new ol.layer.Vector({
	source: aimsMap.source
});
var layer2 = new ol.layer.Vector({
    source: aimsMap.source
});
aimsMap.view = new ol.View({
	center: [11464900, 4744239.5],
	zoom: 8
});
/**
 * 初始化地图
 */
aimsMap.map = new ol.Map({
	target: 'map',
	logo: false,
	layers: [layer0, layer2],
	view: aimsMap.view
});
/**
 * 选中与未选中地块样式
 */
var notInsuredStyle = new ol.style.Style({
	fill: new ol.style.Fill({
		color: 'rgba(255, 255, 255, 0.2)'
	}),
	stroke: new ol.style.Stroke({
		color: '#ffcc33',
		width: 2
	}),
	image: new ol.style.Circle({
		radius: 7,
		fill: new ol.style.Fill({
			color: '#ffcc33'
		})
	})
});

var insuredStyle = new ol.style.Style({
	fill: new ol.style.Fill({
		color: 'rgba(255, 255, 255, 0.2)'
	}),
	stroke: new ol.style.Stroke({
		color: '#40B4E4',
		width: 2
	}),
	image: new ol.style.Circle({
		radius: 7,
		fill: new ol.style.Fill({
			color: '#ffcc33'
		})
	})
});

var selectedStyle = new ol.style.Style({
	fill: new ol.style.Fill({
		color: 'rgba(255, 255, 255, 0.2)'
	}),
	stroke: new ol.style.Stroke({
		color: 'red',
		width: 2
	}),
	image: new ol.style.Circle({
		radius: 7,
		fill: new ol.style.Fill({
			color: '#ffcc33'
		})
	})
});

/**
 * 操作地块
 */
//画地块
aimsMap.draw = new ol.interaction.Draw({
    source: aimsMap.source,
    type: 'Polygon'
});
//焦点
aimsMap.snap = new ol.interaction.Snap({
    source: aimsMap.source
});
//选中地块
aimsMap.select = new ol.interaction.Select({
	wrapX: false
});
//编辑地块
aimsMap.modify = new ol.interaction.Modify({
	features: aimsMap.select.getFeatures()
});
//拆分地块
aimsMap.splitDraw = new ol.interaction.Draw({
	source: aimsMap.source,
	type: "LineString"
});
/**
 * 添加编辑地块的能力
 */
aimsMap.addDrawAbilty = function () {
    if(!aimsMap.map) {
        throw new Error("please call aimsMap.initBasicMap init aimsMap.map");
    };
    aimsMap.remarkSource = aimsMap.source;
    aimsMap.draw.setActive(false);
    aimsMap.select.setActive(false);
    aimsMap.splitDraw.setActive(false);
    aimsMap.map.addInteraction(aimsMap.draw);
    aimsMap.map.addInteraction(aimsMap.snap);
    aimsMap.map.addInteraction(aimsMap.select);
    aimsMap.map.addInteraction(aimsMap.splitDraw);
    /********************************添加地块开始*****************************************************************/
    aimsMap.draw.on("drawend", function(evt) {
        var feature = evt.feature;
        feature.setStyle(insuredStyle);
        var json = JSON.stringify(formator.toEsriJson(feature));
        var polygon = eval("(" + json + ")");
        if(!polygon || !polygon.rings || polygon.rings.length == 0) {
            layer.msg("没有闭合的图形");
            return;
        }

        var geometry = feature.getGeometry();
        var output = formator.formatArea(geometry, false);
        var area = (output / 666.66667).toFixed(2);
        var areaInSqm = (output / 1).toFixed(2);
        var extent = geometry.getExtent();
        var x = (extent[0] + extent[2]) / 2;
        var y = (extent[1] + extent[3]) / 2;

        var tm = generateUUID();
        var anchor = new ol.Feature({
            geometry: new ol.geom.Point([x, y])
        });
        anchor.setId("area" + tm);

        anchor.setStyle(new ol.style.Style({
            text: new ol.style.Text({
                // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
                textAlign: "center",
                text: area + "亩",
                fill: new ol.style.Fill({
                    color: 'red'
                })
            })
        }));

        var remarks = "";
        var remarkMaker = new ol.Feature({
            geometry: new ol.geom.Point([x, y])
        });
        remarkMaker.setId("remark" + tm);
        remarkMaker.setStyle(new ol.style.Style({
            text: new ol.style.Text({
                // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
                text: remarks,
                textAlign: "center",
                offsetX: 10,
                offsetY: 20,
                fill: new ol.style.Fill({
                    color: 'red'
                })
            })
        }));
        aimsMap.remarkSource.addFeature(anchor);
        aimsMap.remarkSource.addFeature(remarkMaker);

        var entity = $.extend({},aimsMap.DefaultProperty);
        entity.id = tm;
        entity.shape = json;
        entity.wgs84Shape = "";
        entity.recognizees = "";
        entity.area = areaInSqm;
        sendAjax(feature,entity);
    });
    /********************************添加地块结束*****************************************************************/
    /********************************选择地块开始*****************************************************************/
    aimsMap.select.on('select', function(e) {
        if(e.selected.length == 0) {
            return;
        }
        var feature = e.selected[0];
        var pro = feature.getProperties();
        if(pro.shapestatus == 'Y') {
            modifyBlock();
        }
        var markUserChecked = $("#userInner").hasClass("change-edit");
        if(markUserChecked) {
            editUserInfo(e);
            feature.setStyle(selectedStyle);
        } else {
            feature.setStyle(selectedStyle);
        }
        features = e.target.getFeatures();

    });
    /********************************选择地块结束*****************************************************************/
    /********************************分割地块开始*****************************************************************/
    aimsMap.splitDraw.on("drawStart", function() {
        if(aimsMap.select.getFeatures().getArray().length<=0) {
            layer.msg("需要选择一个地块", {icon: 1});
            return;
        }
    });
    aimsMap.splitDraw.on("drawend", function(evt) {
        var featureLine = evt.feature;

        if(aimsMap.select.getFeatures().getArray().length<=0) {
            layer.msg("需要选择一个地块", {icon: 1});
            aimsMap.source.removeFeature(featureLine);
            return;
        }

        var selectFeature = aimsMap.select.getFeatures().getArray()[0];
        var polygon = getTurfObj(selectFeature);
        var polyline = getTurfObj(featureLine);

        polygon = turf.toWgs84(polygon);
        polyline = turf.toWgs84(polyline);
        var result = cut(polygon, polyline);
        selectFeature.setStyle(insuredStyle);
        /**
         * 删除选中地块注释
         * @type {ol.Feature}
         */
        var deleteId = selectFeature.getProperties().id;
        var oldAnchor = aimsMap.remarkSource.getFeatureById("area" + deleteId);
        aimsMap.remarkSource.removeFeature(oldAnchor);
        var oldRemark = aimsMap.remarkSource.getFeatureById("remark" + deleteId);
        if(oldRemark != null) {
            aimsMap.remarkSource.removeFeature(oldRemark);
        }
        aimsMap.source.removeFeature(selectFeature); //删除选中地块
        for(var j = 0; j < result.features.length; j++) {
            var o1 = turf.toMercator(result.features[j]);
            var feature = (new ol.format.GeoJSON()).readFeature(o1);
            aimsMap.source.addFeature(feature);
            feature.setStyle(insuredStyle);

            var json = JSON.stringify(formator.toEsriJson(feature));
            var polygon = eval("(" + json + ")");
            if(!polygon || !polygon.rings || polygon.rings.length == 0) {

                layer.msg("没有闭合的图形");
                return;
            }

            var geometry = feature.getGeometry();
            var output = formator.formatArea(geometry, false);
            var area = (output / 666.66667).toFixed(2);
            var areaInSqm = (output / 1).toFixed(2);
            var extent = geometry.getExtent();
            var x = (extent[0] + extent[2]) / 2;
            var y = (extent[1] + extent[3]) / 2;

            var tm = generateUUID();
            feature.setId(tm);
            var anchor = new ol.Feature({
                geometry: new ol.geom.Point([x, y])
            });
            anchor.setId("area" + tm);

            anchor.setStyle(new ol.style.Style({
                text: new ol.style.Text({
                    // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
                    textAlign: "center",
                    text: area + "亩",
                    fill: new ol.style.Fill({
                        color: 'red'
                    })
                })
            }));

            var remarks = "";
            var remarkMaker = new ol.Feature({
                geometry: new ol.geom.Point([x, y])
            });
            remarkMaker.setId("remark" + tm);
            remarkMaker.setStyle(new ol.style.Style({
                text: new ol.style.Text({
                    // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
                    text: remarks,
                    textAlign: "center",
                    offsetX: 10,
                    offsetY: 20,
                    fill: new ol.style.Fill({
                        color: 'red'
                    })
                })
            }));

            aimsMap.remarkSource.addFeature(anchor);
            aimsMap.remarkSource.addFeature(remarkMaker);
            var entity = $.extend({},aimsMap.DefaultProperty);
            entity.id = tm;
            entity.shape = json;
            entity.wgs84Shape = "";
            entity.recognizees = "";
            entity.area = areaInSqm;
            // feature.setProperties(entity);
            sendAjax(feature,entity);
        }
        try {
            setTimeout(function() {
                aimsMap.source.removeFeature(featureLine);
                aimsMap.splitDraw.setActive(false);
                aimsMap.draw.setActive(false);
            }, 500);
        } catch(e) {

        }
    });
    /********************************分割地块结束*****************************************************************/
}
/**
 * 画地块
 */
var btnDrawShape = document.getElementById("btnDrawShape");
btnDrawShape.onclick = function() {
    aimsMap.select.getFeatures().clear();
	aimsMap.draw.setActive(true);
	aimsMap.splitDraw.setActive(false);
	aimsMap.select.setActive(false);
};

/**
 * 选中地块
 */
var btnSelectShape = document.getElementById("btnSelectShape");
btnSelectShape.onclick = function () {
    $("#userInner").removeClass("change-edit");
    aimsMap.select.getFeatures().clear();
	aimsMap.draw.setActive(false);
	aimsMap.select.setActive(true);
	aimsMap.splitDraw.setActive(false);
}
/**
 * 分割地块
 */
var btnSplitShape = document.getElementById("btnSplitShape");
btnSplitShape.onclick = function() {
    aimsMap.draw.setActive(false);
    aimsMap.select.setActive(false);
    aimsMap.splitDraw.setActive(true);
    if(aimsMap.select.getFeatures().getArray().length<=0) {
        layer.msg("需要选择一个地块", {icon: 1});
        aimsMap.splitDraw.setActive(false);
        return;
    }
}
/**
 * 编辑农户
 * @type {HTMLElement | null}
 */
var btnEditUser = document.getElementById("btnEditUser");
btnEditUser.onclick =function(){
    $("#userInner").addClass("change-edit");
    aimsMap.select.getFeatures().clear();
    aimsMap.draw.setActive(false);
    aimsMap.select.setActive(true);
    aimsMap.splitDraw.setActive(false);
}
/**
 * 编辑农户
 * @param e
 */
var editUserInfo = function (e) {
    if(e.selected.length == 0){
        return;
    }
    var feature = e.selected[0];
    var entity = feature.getProperties();
    var params = entity.params;
    params = params.split(":");
    var paramsC = entity.paramsC;
    paramsC = paramsC.split(":");
    var content = "<div style='padding: 15px'><form id='su' > <table>";
    for(var i = 0; i < params.length-1; i++) {
        content += "<tr><td style='width: 115px;'>" + paramsC[i] + ":</td><td><input id='" + params[i] + "' name='" + params[i] + "'></td> </tr>";
    }
    content += "<input hidden id='coordinate' name='coordinate' value='"+dataFormat.toWKT(feature)+"'>";
    // content += "<input hidden id='geoType' name='geoType' value='"+geoType+"'>";
    content += "<input hidden id='params' name='params' value='"+params+"'>";
    content += "<tr><td colspan='2'><input type='button' style='float: right;' id='tijiao' name='tijiao' value='提交' onclick='sub()'></td></tr>";
    content += "</form></div>";
    layer.open({
        tilte:'编辑农户',
        type:1,
        area:['306px','400px'],
        shadeClose:true,//点击遮罩关闭
        content:content
    });
}
/**
 * 编辑地块
 */
var modifyBlock = function() {
	aimsMap.map.addInteraction(aimsMap.modify);
	aimsMap.modify.on('modifyend', function(evt) {
		var features = evt.features;
		if(features.getArray()) {
			feature = features.getArray()[0];
			var oldProperty = feature.getProperties();
			var areaMark = aimsMap.remarkSource.getFeatureById("area" + oldProperty);
			var geo = feature.getGeometry();
			var output = formator.formatArea(geo, false);
			var area = (output / 666.66667).toFixed(2);
			areaMark.setStyle(new ol.style.Style({
				text: new ol.style.Text({
					textAlign: "center",
					text: area + "亩",
					fill: new ol.style.Fill({
						color: 'red'
					})
				})
			}));
		}
	});
}

/**
 * 保存NotInuredShape地块
 * @param feature
 * @param callback
 */
function saveNotInuredShape(feature,callback) {
    feature.setStyle(insuredStyle);

    var json  = JSON.stringify(formator.toEsriJson(feature));
    var polygon = eval("(" + json + ")");
    if (!polygon || !polygon.rings || polygon.rings.length == 0)
    {
        layer.msg("没有闭合的图形");
        return;
    }

    var oldProperty = feature.getProperties();
    var entity = $.extend({},aimsMap.DefaultProperty);
    // var bizId= $("#APP_C_PLY_ID").val();

    var geometry = feature.getGeometry();
    var area = (geometry.getArea() / 666.66667).toFixed(2);
    var areaInSqm = (geometry.getArea() / 1).toFixed(2);

    entity.id = oldProperty.id;
    entity.bizId = bizId;
    entity.shape = json;
    entity.wgs84Shape = "";
    entity.recognizees = "";
    entity.area = areaInSqm;
    var shapeProperty = $.extend({shapestatus:"Y"},entity);
    feature.setProperties(shapeProperty);
}

/**以下为地块分割相关功能**/

function getTurfObj(f) {
	var type = f.getGeometry().getType();
	var coordinates = f.getGeometry().getCoordinates();
	if(type === 'Polygon') {
		return turf.polygon(coordinates);
	} else if(type === 'MultiLineString') {
		return turf.multiLineString(coordinates);
	} else if(type === 'MultiPolygon') {
		return turf.multiPolygon(coordinates);
	} else if(type === 'Point') {
		return turf.point(coordinates);
	} else if(type === 'LineString') {
		return turf.lineString(coordinates);
	} else if(type === 'Feature') {
		var geometry = {
			"type": "Feature",
			"coordinates": coordinates
		};
		return turf.feature(geometry);
	}
	return undefined;
}

function cut(poly, line, tolerance, toleranceType) {
	if(tolerance == null) {
		tolerance = .0001;
	}
	if(toleranceType == null) {
		toleranceType = {
			units: 'miles'
		};
	}
	if(poly.geometry === void 0 || poly.geometry.type !== 'Polygon')
		throw('"turf-cut" only accepts Polygon type as victim input');
	if(line.geometry === void 0 || (line.geometry.type !== 'LineString' && line.geometry.type !== 'Polygon' && line.geometry.type != 'MultiLineString'))
		throw('"turf-cut" only accepts LineString or polygon type as axe input');
	if(line.geometry.type == "LineString") {
		if(turf.inside(turf.point(line.geometry.coordinates[0]), poly) ||
            turf.inside(turf.point(line.geometry.coordinates[line.geometry.coordinates.length - 1]), poly))
		{
			layer.msg("分割线和地块没有交点，请重新绘制", {icon: 1});
			return;
		}
	} else {
		var points = turf.explode(line);
		if(!turf.within(points, turf.featureCollection([poly])))
			throw('All points of polygon must be within tract.');
	}

	var lineIntersect = turf.lineIntersect(line, poly);
	var lineExp = turf.explode(line);
	for(var p = 1; p < lineExp.features.length - 1; ++p) {
		lineIntersect.features.push(turf.point(lineExp.features[p].geometry.coordinates));
	}

	var _axe = (
			line.geometry.type == 'LineString' ||
			line.geometry.type == 'MultiLineString') ?
		turf.buffer(line, tolerance, toleranceType) :
		line;

	var _body = turf.difference(poly, _axe);
	var pieces = [];

	if(_body.geometry.type == 'Polygon') {
		pieces.push(turf.polygon(_body.geometry.coordinates));
	} else {
		_body.geometry.coordinates.forEach(function(a) {
			pieces.push(turf.polygon(a))
		});
	}

	// Zip the polygons back together
	for(p in pieces) {
		piece = pieces[p];
		for(c in piece.geometry.coordinates[0]) {
			var coord = piece.geometry.coordinates[0][c];
			var p = turf.point(coord);
			for(lp in lineIntersect.features) {
				lpoint = lineIntersect.features[lp];
				if(turf.distance(lpoint, p, toleranceType) <= tolerance * 2) {
					piece.geometry.coordinates[0][c] = lpoint.geometry.coordinates;
				}
			}
		}
	}

	// Filter out duplicate points
	for(p in pieces) {
		var newcoords = [];
		piece = pieces[p];
		for(c in piece.geometry.coordinates[0]) {
			var coord = piece.geometry.coordinates[0][c];
			if(c == 0 || coord[0] != newcoords[newcoords.length - 1][0] || coord[1] != newcoords[newcoords.length - 1][1]) {
				newcoords.push(coord);
			}
		}
		piece.geometry.coordinates[0] = newcoords;
	}

	pieces.forEach(function(a) {
		a.properties = poly.properties
	});

	return turf.featureCollection(pieces);
}

/**以上为地块分割相关功能**/
/**
 * 合并地块
 */
var btnCombine = document.getElementById("btnCombine");
btnCombine.onclick = function() {
	var selectedFeatures = aimsMap.select.getFeatures();
	if(selectedFeatures.getArray().length < 2) {
		layer.msg("需要选择2个以上地块才能合并");
		return;
	}
	//合并操作
	var parser = new jsts.io.OL3Parser();
	var jstsShapes = [];
	var firstFeature;
    var deleteIds = [];
    var firstFeatureProperty;
	for(var i = 0; i < selectedFeatures.getLength(); i++) {
		var feature = selectedFeatures.item(i);
		var jstsGeom = parser.read(feature.getGeometry());
		if(i >= 1) {
			aimsMap.source.removeFeature(feature);
            deleteIds.push(feature.getProperties().id);
		} else {
            firstFeatureProperty = feature.getProperties();
			firstFeature = feature;
            deleteIds.push(feature.getProperties().id);
		}
		jstsShapes[i] = jstsGeom;
	}

	var outShape = jstsShapes[0];
	for(var j = 1; j < selectedFeatures.getLength(); j++) {
		outShape = outShape.union(jstsShapes[j]);
	}

	firstFeature.setGeometry(parser.write(outShape));
	firstFeature.setStyle(insuredStyle);

	var geometry = firstFeature.getGeometry();

	var output = formator.formatArea(geometry, false);
	var area = (output / 666.66667).toFixed(2);
	var areaInSqm = (output / 1).toFixed(2);

	var json = JSON.stringify(formator.toEsriJson(firstFeature));
	var entity = jQuery.extend({
		id: firstFeatureProperty.id,
		bizId: firstFeatureProperty.bizId,
		shape: json,
		wgs84Shape: ""
	}, aimsMap.DefaultProperty);
	entity.area = areaInSqm;
	var shapes = [entity];
	//删除标注
	for(var i = 0; i < deleteIds.length; i++) {
		var deleId = deleteIds[i];
		var deleFeature = aimsMap.remarkSource.getFeatureById("area" + deleId);
		var delemarker = aimsMap.remarkSource.getFeatureById("remark" + deleId);
		aimsMap.remarkSource.removeFeature(deleFeature);
		if(delemarker != null) {
			aimsMap.remarkSource.removeFeature(delemarker);
		}
	}
    var newId = entity.id;
    var geo = firstFeature.getGeometry();
    var extent = geo.getExtent();
    var x = (extent[0] + extent[2]) / 2;
    var y = (extent[1] + extent[3]) / 2;

    var anchor = new ol.Feature({
        geometry: new ol.geom.Point([x, y])
    });
    anchor.setId("area" + newId);
    anchor.setStyle(new ol.style.Style({
        text: new ol.style.Text({
            // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
            textAlign: "center",
            text: area + "亩",
            fill: new ol.style.Fill({
                color: 'red'
            })
        })
    }));
    var remarkMaker = new ol.Feature({
        geometry: new ol.geom.Point([x, y])
    });
    remarkMaker.setId('remark' + newId);
    remarkMaker.setStyle(new ol.style.Style({
        text: new ol.style.Text({
            // font: '10px sans-serif' 默认这个字体，可以修改成其他的，格式和css的字体设置一样
            // text: remarks,
            textAlign: 'center',
            offsetX: 10,
            offsetY: 20,
            fill: new ol.style.Fill({
                color: 'red'
            })
        })
    }));
    aimsMap.remarkSource.addFeature(anchor);
    aimsMap.remarkSource.addFeature(remarkMaker);
}
/**
 * 跟后台（服务端）交互
 */
var sendAjax = function(feature,entity){
    // var categoryItemCode = document.getElementById('categoryItem').value;
    var categoryItemCode = 1;
    var geoType = feature.getGeometry().getType();
    $.ajax({
        type: "get",
        url: "http://localhost:8090/mouldAttribute/findList/" + categoryItemCode,
        error: function(error) {
            console.log(error);
        },
        success: function(data) {
            var content = "<div style='padding: 15px'><form id='su' > <table>";
            var params = "";
            var paramsC = "";
            for(var i = 0; i < data.length; i++) {
                params+=data[i].maName+":";
                paramsC+=data[i].fieldexplain+":";
                content += "<tr><td style='width: 115px;'>" + data[i].fieldexplain + ":</td><td><input id='" + data[i].maName + "' name='" + data[i].maName + "'></td> </tr>";
            }
            entity.params=params;
            entity.paramsC=paramsC;
            feature.setProperties(entity);
            content += "<input hidden id='coordinate' name='coordinate' value='"+dataFormat.toWKT(feature)+"'>";
            content += "<input hidden id='geoType' name='geoType' value='"+geoType+"'>";
            content += "<input hidden id='params' name='params' value='"+params+"'>";
            content += "<tr><td colspan='2'><input type='button' style='float: right;' id='tijiao' name='tijiao' value='提交' onclick='sub()'></td></tr>";
            content += "</form></div>";
            layer.open({
                title:'添加农户',
                type: 1,
                area: ['360px', '400px'],
                shadeClose: true, //点击遮罩关闭
                content: content
            });
        }
    });
}