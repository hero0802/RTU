if (!("xh" in window)) {
	window.xh = {};
};
require.config({
	paths : {
		echarts : 'resources/static/echarts'
	}
});
var map = null;
var img = null;
var markers = [];
function LocalMapType() {
}
LocalMapType.prototype.tileSize = new google.maps.Size(256, 256);
LocalMapType.prototype.maxZoom = 12; // 地图显示最大级别
LocalMapType.prototype.minZoom = 5; // 地图显示最小级别
LocalMapType.prototype.name = "本地地图";
LocalMapType.prototype.alt = "显示本地地图数据";
LocalMapType.prototype.getTile = function(coord, zoom, ownerDocument) {
	img = ownerDocument.createElement("img");
	img.style.width = this.tileSize.width + "px";
	img.style.height = this.tileSize.height + "px";
	img.style.border = 0;
	// 地图存放路径
	// 谷歌矢量图 maptile/googlemaps/roadmap
	// 谷歌影像图 maptile/googlemaps/roadmap
	// MapABC地图 maptile/mapabc/
	// strURL = "maptile/googlemaps/roadmap/";

	mapPicDir = "maptile/googlemaps/roadmap/";
	var curSize = Math.pow(2, zoom);
	strURL = mapPicDir + zoom + "/" + (coord.x % curSize) + "/"
			+ (coord.y % curSize) + ".png";
	// strURL = "E:/地图文件/谷歌地图中国0-12级googlemaps/googlemaps/roadmap/" + zoom +
	// "/" + (coord.x % curSize )+ "/" + (coord.y % curSize)+ ".PNG";

	img.src = strURL;
	img.alt = "" + "没有地图数据";
	CheckImgExists(strURL);

	// img.onerror="javascript:this.src='resources/images/img/1.png'" ;
	return img;
};
toastr.options = {
	"debug" : false,
	"newestOnTop" : false,
	"positionClass" : "toast-top-center",
	"closeButton" : true,
	/* 动态效果 */
	"toastClass" : "animated fadeInRight",
	"showDuration" : "300",
	"hideDuration" : "1000",
	/* 消失时间 */
	"timeOut" : "1000",
	"extendedTimeOut" : "1000",
	"showMethod" : "fadeIn",
	"hideMethod" : "fadeOut",
	"progressBar" : true
};
// 检查图片是否存在
function CheckImgExists(imgurl) {
	/*
	 * var ImgObj = new Image(); //判断图片是否存在 ImgObj.src = imgurl; //没有图片，则返回-1 if
	 * (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
	 * console.log(imgurl+"存在"); } else { console.log(imgurl+"不存在");
	 * img.src='resources/images/img/maperror.png'; }
	 */
	$('img').error(function() {
		$(this).attr('src', "resources/images/img/maperror.png");
	});
}
xh.CreatMaker9 = function(record, icon, labelClass) {
	var wgloc = {};
	wgloc.lat = record.lat;
	wgloc.lng = record.lon;
	var lat = transformFromWGSToGCJ(wgloc).lat;
	var lng = transformFromWGSToGCJ(wgloc).lng;
	var marker = new MarkerWithLabel({
		position : new google.maps.LatLng(lat,lng),
		map : map,
		record : record,
		data : record,
		title : "ID:"+ record.siteId+ "省份："+ record.province+ " 城市："
				+ record.city+ " 区县："+ record.county+ " 单位："+ record.company,
		id : record.siteId,
		icon : icon,
		labelContent : record.name,
		labelAnchor : new google.maps.Point(30,1),
		labelClass : labelClass,
		labelStyle : {}
		});
	markers.push(marker);
	 google.maps.event.addListener(marker,'click',function() {
         xh.markerClick(this.data);
		});
}
xh.clearMarker = function(mark) {
	mark.setMap(null);
}
/* 加载数据 */
xh.loadData = function() {
	xh.loadPie();
	xh.loadBar();
	xh.initialize();
	$(".closeMakerPanel").click(function() {
		$("#aside-right").fadeToggle("fast");
		$("#aside-right").removeClass("show");

	})

	  dwr.engine.setActiveReverseAjax(true); 
	  dwr.engine.setAsync(false);//同步步
	  //设置在页面关闭时，通知服务端销毁会话 dwr.engine.setNotifyServerOnPageUnload( true);
	  xh.dwr(); dwr.engine.setErrorHandler(function(){
	  /*window.location.href="index.html"; */})
	 
	var app = angular.module("app", []);
	app.controller("alarm",function($scope, $http) {
	    $http.get('site/site_trade.action')
        .success(function(res) {
			$scope.trade_data = res.items;
		});
	$http.get("site/i_alarm_count.action")
	     .success(function(response) {
		    var i_alarm_count = response.totals;
		    $scope.i_value_alarm = response.items;
		    if (i_alarm_count > 0) {
			   $("#alarm-div").css("display","block");}
		});

	// 修改地图图标
	$scope.resetMaker = function() {
							/* markers=[]; */
     markers.slice(0, markers.length)
	$http.get("site/mapsite.action")
		.success(function(response) {
			var data = response.items;
			for (var i = 0; i < response.total; i++) {
			var image = "", marker = "";
			var wgloc = {};
			wgloc.lat = data[i].lat;
			wgloc.lng = data[i].lon;
			var lat = transformFromWGSToGCJ(wgloc).lat;
			var lng = transformFromWGSToGCJ(wgloc).lng;
			data[i].lat = lat;
			data[i].lng = lng;
			var alarmId = data[i].alarmId;
            if (data[i].status == 0) {
				var type = data[i].trade;
				var icon = "";
               for (var a = 0; a < $scope.trade_data.length; a++) {
                   if ($scope.trade_data[a].trade == type) {
					icon = "mapfiles/trade_"+ $scope.trade_data[a].typeId+ "_off.png";
                      }
				}
              marker = new MarkerWithLabel({
					position : new google.maps.LatLng(lat,lng),
					map : map,
					record : data[i],
					data : data[i],
					title : "ID:"+ data[i].siteId+ "省份："+ data[i].province+ " 城市："
							+ data[i].city+ " 区县："+ data[i].county+ " 单位："+ data[i].company,
					id : data[i].siteId,
					icon : icon,
					labelContent : data[i].name,
					labelAnchor : new google.maps.Point(30,1),
					labelClass : "marker-label-gray",
					labelStyle : {}
					});
			} else {
				var type = data[i].trade;
				var icon = "";
				for (var a = 0; a < $scope.trade_data.length; a++) {
					if ($scope.trade_data[a].trade == type) {
						icon = "mapfiles/trade_"+ $scope.trade_data[a].typeId+ "_online.png";
					}
				}
				var labelClass = "marker-label";
				if (alarmId != "") {labelClass = "marker-label-error"}
					marker = new MarkerWithLabel({
					position : new google.maps.LatLng(lat,lng),
					map : map,
					
					record : data[i],
					data : data[i],
					title : data[i].name,
					title : "ID:"+ data[i].siteId+ " 省份："+ data[i].province+ " 城市："
							+ data[i].city+ " 区县："+ data[i].county+ " 单位："+ data[i].company,
					id : data[i].siteId,
					icon : icon,
					
					labelContent : data[i].name,
					labelAnchor : new google.maps.Point(30,1),
					labelClass : labelClass, // the
					labelStyle : {}
					});
				}
				markers.push(marker);
		    }
          for (var i = 0; i < markers.length; i++) {
              google.maps.event.addListener(markers[i],'click',function() {
                xh.markerClick(this.data);
			});
			}
			});
		}
		$scope.resetMaker();

						$scope.loadForm = function(id) {
							$http
									.get("site/id_data.action?siteId=" + id)
									.success(
											function(response) {

												if (response.spd_total == 0) {
													$scope.spd_isNULL = 1;

												} else {
													$scope.spd_isNULL = 0;
												}
												if (response.i_total == 0) {
													$scope.i_isNULL = 1;

												} else {
													$scope.i_isNULL = 0;
												}
												if (response.r_total == 0) {
													$scope.r_isNULL = 1;

												} else {
													$scope.r_isNULL = 0;
												}

												$scope.site_items = response.site_items;
												$scope.spd_items = response.spd_items;
												$scope.i_items = response.i_items;
												$scope.r_items = response.r_items;

											});

						};
						// xh.setInter();

					});

};
function rtu_status(str) {
	var appElement=document.querySelector('[ng-controller=alarm]');
	var $scope=angular.element(appElement).scope();
	if (str ==null || str=="") {
		return;
	}
	
	var recvData = eval('(' + str + ')');
	var record=recvData.items[0];
	var rtuId = record.siteId;
	var image = "", marker = "";
	var alarmId = record.alarmId;
	var labelClass = "marker-label";
	var icon = "";
	for (var j = 0; j < markers.length; j++) {
		if (markers[j].id == rtuId) {
			
			
            if (record.status == 0) {
				var type = record.trade;
				labelClass="marker-label-gray";
               for (var a = 0; a < $scope.trade_data.length; a++) {
            	   
                   if ($scope.trade_data[a].trade == type) {
					icon = "mapfiles/trade_"+ $scope.trade_data[a].typeId+ "_off.png";
				
                      }
				}            
          
			} else {
				var type = record.trade;
				for (var a = 0; a < $scope.trade_data.length; a++) {
					if ($scope.trade_data[a].trade == type) {
						icon = "mapfiles/trade_"+ $scope.trade_data[a].typeId+ "_online.png";
						
					}
				}
				labelClass = "marker-label";
				if (alarmId != "") {labelClass = "marker-label-error"}
			}
				
               xh.clearMarker(markers[j]);
			   markers.splice(j, 1);
			  xh.CreatMaker9(record, icon, labelClass);
		}
	}
	

}
// 刷新数据
xh.refresh = function() {
	var appElement = document.querySelector('[ng-controller=alarm]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();
	xh.loadPie();
};
xh.setInter = function() {
	setInterval(xh.resetMaker(), 100);
}
xh.resetMaker = function() {
	var appElement = document.querySelector('[ng-controller=alarm]');
	var $scope = angular.element(appElement).scope();
	console.log("定时器=1");
	$scope.resetMaker();
};
// marker点击事件
xh.markerClick = function(data) {
	var appElement = document.querySelector('[ng-controller=alarm]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.loadForm(data.siteId);
	var type = data.trade;
	var icon = "";
	if (data.status == 0) {

		for (var a = 0; a < $scope.trade_data.length; a++) {
			if ($scope.trade_data[a].trade == type) {
				icon = "mapfiles/trade_" + $scope.trade_data[a].typeId
						+ "_off.png";
				// $scope.trade_icon = $scope.conArr[a];
			}
		}

	} else {
		for (var a = 0; a < $scope.trade_data.length; a++) {
			if ($scope.trade_data[a].trade == type) {
				icon = "mapfiles/trade_" + $scope.trade_data[a].typeId
						+ "_online.png";
				// $scope.trade_icon = $scope.conArr[a];
			}
		}
	}

	$("#trade-icon").attr("src", icon);
	if ($("aside-right").hasClass("show")) {

	} else {
		$("#aside-right").fadeToggle("fast");
		$("#aside-right").addClass("show");
	}

}
// 图标
xh.loadPie = function() {
	require([ 'echarts', 'echarts/chart/pie' ],
			function(ec) {

				var myChart = ec.init(document.getElementById('pie'));
				var totals = 1, alarm_count = 1;

				$.ajax({
					url : 'site/site_count.action',
					/*
					 * data : { site:site, timeStart:timeStart, timeEnd:timeEnd },
					 */
					type : 'post',
					dataType : "json",
					async : false,
					success : function(response) {
						totals = response.totals;
						alarm_count = response.alarm_totals;
						offline = response.offline

					},
					failure : function(response) {
					}
				});

				var option = {
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c}"
					},

					legend : {
						orient : 'vertical', // 'vertical'
						x : 'left', // 'center' | 'left' | {number},
						y : 'top', // 'center' | 'bottom' | {number}
						backgroundColor : '#eee',
						borderColor : '#fafafa',
						borderWidth : 1,
						padding : 10, // [5, 10, 15, 20]
						itemGap : 20,
						textStyle : {
							color : 'red'
						},
						data : [ '正常', '异常', '链路中断' ]
					},
					calculable : false,
					backgroundColor : 'rgba(0,0,0, .4)',
					/*
					 * backgroundColor : '#1b1b1b', toolbox : { show : true,
					 * feature : { mark : { show : false }, restore : { show :
					 * false }, saveAsImage : { show : true } } },
					 */
					series : [ {
						type : 'pie',
						radius : [ 0, 70 ],// 半径，默认为Min(width, height) / 2 -
											// 50,
						// 传数组实现环形图，[内半径，外半径]
						center : [ 150, 100 ],
						itemStyle : {
							normal : {// 默认样式
								label : {
									show : true,
									position : 'center',
									formatter : function() {
										var res = "站点总数：" + totals;
										return res;
									},
									textStyle : {
										baseline : 'bottom',
										color : '#fff'
									}

								},

								labelLine : {
									show : false
								},
								x : 'right'
							},

							emphasis : {// 强调样式（悬浮时样式
								label : {
									show : false,
									position : 'center',
									textStyle : {
										fontSize : '30',
										fontWeight : 'bold'
									}
								}

							}
						},
						data : [
								{
									value : parseInt(parseInt(totals)
											- parseInt(alarm_count)
											- parseInt(offline)),
									name : '正常',
									itemStyle : {
										normal : {
											color : 'green'
										}
									}
								}, {
									value : parseInt(alarm_count),
									name : '异常',
									itemStyle : {
										normal : {
											color : 'red'
										}
									}
								}, {
									value : parseInt(offline),
									name : '链路中断',
									itemStyle : {
										normal : {
											color : 'gray'
										}
									}
								} ]
					} ]
				};
				/*
				 * myChart.showLoading({ text : '数据获取中', effect: 'whirling' });
				 */
				myChart.setOption(option);
			});
};
// 图标
xh.loadBar = function() {
	require([ 'echarts', 'echarts/chart/bar' ], function(ec) {

		var myChart = ec.init(document.getElementById('bar'));
		var spd = 0, r = 0, i = 0;

		$.ajax({
			url : 'site/equipment_count.action',
			/*
			 * data : { site:site, timeStart:timeStart, timeEnd:timeEnd },
			 */
			type : 'post',
			dataType : "json",
			async : false,
			success : function(response) {
				spd = response.spd;
				r = response.r;
				i = response.i

			},
			failure : function(response) {
			}
		});

		var option = {
			backgroundColor : 'rgba(0,0,0, .4)',
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c}"
			},
			title : {
				text : "设备异常、超过阀值数量统计",
				subtext : "",
				x : 'center',
				// 正标题样式
				textStyle : {
					fontSize : 50,
					/* fontFamily : 'Arial', */
					fontWeight : 150,
					color : '#fff'
				},
				// 副标题样式
				subtextStyle : {
					fontSize : 30,
					fontFamily : 'Arial',
					color : "#fff"
				}
			},
			/*
			 * legend : { orient : 'horizontal', // 'vertical' x : 'center', //
			 * 'center' | 'left' | {number}, y : 'bottom', // 'center' |
			 * 'bottom' | {number} backgroundColor : '#eee', borderColor :
			 * '#fafafa', borderWidth : 1, //padding : 10, // [5, 10, 15, 20]
			 * itemGap : 20, textStyle : { color : 'red' }, data : [ 'SPD',
			 * '接地电阻', '雷电流' ] },rgba(0,0,0, .4)
			 */
			xAxis : [ {
				type : 'category',
				axisLabel : {
					show : true,
					textStyle : {
						color : "#fff"
					}
				},
				data : [ 'SPD', '接地电阻', '雷电流' ]
			} ],
			yAxis : [ {
				type : 'value',
				axisLabel : {
					show : true,
					textStyle : {
						color : "#fff"
					}
				}
			} ],
			calculable : false,

			series : [ {
				"type" : "bar",
				"data" : [ {
					name : 'spd异常',
					value : spd,

					itemStyle : {
						normal : {
							color : '#000'

						}
					}
				}, {
					value : r,
					itemStyle : {
						normal : {
							color : 'yellow'
						}
					}
				}, {
					value : i,
					itemStyle : {
						normal : {
							color : 'red'
						}
					}
				} ]
			} ]
		};
		/*
		 * myChart.showLoading({ text : '数据获取中', effect: 'whirling' });
		 */
		myChart.setOption(option);
	});
};
xh.initialize = function() {
	var myLatlng = new google.maps.LatLng(34.0890613, 103.5791015);
	var localMapType = new LocalMapType();
	var mapOptions = {
		zoom : 5,
		center : myLatlng,
		disableDefaultUI : true,
		mapTypeControl : true,
		mapTypeControlOptions : {
			style : google.maps.MapTypeControlStyle.DEFAULT,
			mapTypeIds : [ google.maps.MapTypeId.ROADMAP,
			/*
			 * google.maps.MapTypeId.HYBRID, google.maps.MapTypeId.SATELLITE,
			 */
			google.maps.MapTypeId.TERRAIN, 'localMap' ]
		// 定义地图类型
		},
		panControl : true,
		zoomControl : true, // 缩放控件
		mapTypeControl : true,
		scaleControl : true,// 尺寸
		streetViewControl : true, // 小人
		rotateControl : true,
		overviewMapControl : false
	};
	map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
	map.mapTypes.set('localMap', localMapType); // 绑定本地地图类型
	map.setMapTypeId('localMap'); // 指定显示本地地图
	map.setOptions({
		draggable : true
	});
	var image = 'mapfiles/spotlight-poi.png';
	/*
	 * marker.setMap(map);//将定义的标注显示在地图上
	 * 
	 */
	/* marker.addListener('click', toggleBounce); */
	/*
	 * map.addListener('click', function(event) { addMarker(event.latLng); });
	 */
	// 鼠标移动获取经纬度坐标
	google.maps.event.addListener(map, 'mousemove', function(event) {
		document.getElementById("showLatlng").innerHTML = "经度："
				+ event.latLng.lng() + ',纬度：' + event.latLng.lat();
	});
	// 鼠标滚动获取地图层级：
	google.maps.event.addListener(map, 'zoom_changed', function(event) {
		document.getElementById("showZoom").innerHTML = " 缩放级别："
				+ map.getZoom();
	});

	/*
	 * google.maps.event.addListener(marker, 'click', function() { alert(1) });
	 */

}
function toggleBounce() {
	if (marker.getAnimation() !== null) {
		marker.setAnimation(null);
	} else {
		marker.setAnimation(google.maps.Animation.BOUNCE);
	}
}
// Adds a marker to the map and push to the array.
function addMarker(location) {
	var marker = new google.maps.Marker({
		position : location,
		map : map
	});
	markers.push(marker);
}

// Sets the map on all markers in the array.
function setMapOnAll(map) {
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(map);
	}
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
	setMapOnAll(null);
}

// Shows any markers currently in the array.
function showMarkers() {
	setMapOnAll(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
	clearMarkers();
	markers = [];
}

function drop() {
	for (var i = 0; i < markerArray.length; i++) {
		setTimeout(function() {
			addMarkerMethod();
		}, i * 200);
	}
}
var pi = 3.14159265358979324;

//
// Krasovsky 1940
//
// a = 6378245.0, 1/f = 298.3
// b = a * (1 - f)
// ee = (a^2 - b^2) / a^2;
var a = 6378245.0;
var ee = 0.00669342162296594323;

function outOfChina(lat, lon) {
	if (lon < 104.891281 || lon > 103.019485)
		return 1;
	if (lat < 31.435693 || lat > 30.084542)
		return 1;
	return 0;
}
function transformLat(x, y) {
	var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2
			* Math.sqrt(x > 0 ? x : -x);
	ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
	ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
	return ret;
}
function transformLon(x, y) {
	var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
			* Math.sqrt(x > 0 ? x : -x);
	ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
	ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
	return ret;
}
function transformFromWGSToGCJ(wgLoc) {
	var mgLoc = {};
	mgLoc.lat = 0;
	mgLoc.lng = 0;
	if (outOfChina(wgLoc.lat, wgLoc.lng)) {
		mgLoc = wgLoc;
		return mgLoc;
	}
	var dLat = transformLat(wgLoc.lng - 105.0, wgLoc.lat - 35.0);
	var dLon = transformLon(wgLoc.lng - 105.0, wgLoc.lat - 35.0);

	var radLat = wgLoc.lat / 180.0 * pi;
	var magic = Math.sin(radLat);
	magic = 1 - ee * magic * magic;
	var sqrtMagic = Math.sqrt(magic);
	dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	mgLoc.lat = wgLoc.lat + dLat;
	mgLoc.lng = wgLoc.lng + dLon;

	return mgLoc;
}

/*
 * //定义一个点坐标数组变量，将所有点从头到尾连成一条线 var flightPlanCoordinates = [ new
 * google.maps.LatLng(37.772323, -122.214897), new google.maps.LatLng(21.291982,
 * -157.821856), new google.maps.LatLng(-18.142599, 178.431), new
 * google.maps.LatLng(-27.46758, 153.027892) ]; var flightPath = new
 * google.maps.Polyline({ path: flightPlanCoordinates,//相应点坐标 strokeColor:
 * "#FF0000",//定义线条颜色 strokeOpacity: 1.0, //线条透明的 取值0~1.0之间，由完全透明到不透明
 * strokeWeight: 2 //线条粗细，单位为px }); flightPath.setMap(map);//将线条显示在地图上
 */
// 绘制多边形
// 定义一个点坐标数组变量，将所有点从头到尾连成一条线
/*
 * var flightPlanCoordinates = [ new google.maps.LatLng(37.772323, -122.214897),
 * new google.maps.LatLng(21.291982, -157.821856), new
 * google.maps.LatLng(-18.142599, 178.431), new google.maps.LatLng(-27.46758,
 * 153.027892) ]; var flightPath = new google.maps.Polyline({ path:
 * flightPlanCoordinates,//相应点坐标 strokeColor: "#FF0000",//定义线条颜色 strokeOpacity:
 * 1.0, //线条透明的 取值0~1.0之间，由完全透明到不透明 strokeWeight: 2 //线条粗细，单位为px });
 * flightPath.setMap(map);//将线条显示在地图上
 */

/*
 * var infowindow = new google.maps.InfoWindow({ content: contentString, //显示内容
 * maxWidth:400 //定义最大宽度 }); var marker = new google.maps.Marker({ position: new
 * google.maps.LatLng(27.56,104.252), map: map, title:"重庆解放碑" });
 * //点击Maker标注显示InfoWindow google.maps.event.addListener(marker, 'click',
 * function() { infowindow.open(map,marker); });
 */
