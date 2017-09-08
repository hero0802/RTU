if (!("xh" in window)) {
	window.xh = {};
};
require.config({
	paths : {
		echarts : 'resources/static/echarts'
	}
});
var appElement = document.querySelector('[ng-controller=alarm]');
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
/* 加载数据 */
xh.loadData = function() {

	xh.tableCheck();

	var app = angular.module("app", []);
	app.config(['$locationProvider',function($locationProvider){
		$locationProvider.html5Mode({
			enabled:true,
			requireBase:false
		});
	}])
	app.controller("alarm", function($scope, $http,$location) {
		$scope.siteId=$location.search().siteId;
		console.log("siteId="+$scope.siteId)
		$scope.count = "10";
		$scope.start = 0;
		$scope.data_isActive = true;
		$http.get(
				"../alarm/spd_alarm.action?site=" + $scope.siteId + "&start=" + $scope.start
						+ "&limit=" + $("#spd-page-limit").val()).success(
				function(response) {
					$scope.spd_alarm = response.spd_items;
					$scope.spd_totals=response.spd_total;
					spd_padding(parseInt(response.spd_total), $scope.count);
				}).error(function() {
		});
		$http.get(
				"../alarm/r_alarm.action?site=" + $scope.siteId + "&start=" + $scope.start
						+ "&limit=" + $("#r-page-limit").val()).success(
				function(response) {
					$scope.r_alarm = response.r_items;
					$scope.r_totals=response.r_total;
					r_padding(parseInt(response.r_total), $scope.count);
				}).error(function() {
			xh.maskHide();
		});
		$http.get(
				"../alarm/i_alarm.action?site=" + $scope.siteId + "&start=" + $scope.start
						+ "&limit=" + $("#i-page-limit").val()).success(
				function(response) {
					$scope.i_alarm = response.i_items;
					$scope.i_totals=response.i_total;
					i_padding(parseInt(response.i_total), $scope.count);
				}).error(function() {
			xh.maskHide();
		});
		// 刷新数据
		$scope.refresh = function() {
			$http.get(
					"../alarm/spd_alarm.action?site=" +  $scope.siteId + "&start=" + $scope.start
							+ "&limit=" + $("#spd-page-limit").val()).success(
					function(response) {
						$scope.spd_alarm = response.spd_items;
						$scope.spd_totals=response.spd_total;
						spd_padding(parseInt(response.spd_total), $scope.count);
					}).error(function() {
			});
			$http.get(
					"../alarm/r_alarm.action?site=" + $scope.siteId + "&start=" + $scope.start
							+ "&limit=" + $("#r-page-limit").val()).success(
					function(response) {
						$scope.r_alarm = response.r_items;
						$scope.r_totals=response.r_total;
						r_padding(parseInt(response.r_total), $scope.count);
					}).error(function() {
			});
			$http.get(
					"../alarm/i_alarm.action?site=" +  $scope.siteId+ "&start=" + $scope.start
							+ "&limit=" + $("#i-page-limit").val()).success(
					function(response) {
						$scope.i_alarm = response.i_items;
						$scope.i_totals=response.i_total;
						i_padding(parseInt(response.i_total), $scope.count);
					}).error(function() {
			});
		};
		
		

		// 分页
		var spd_padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.spd_isNULL = 1;
				return;
			} else {
				$scope.spd_isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$("#spd-page").html('<ul class="pagination" style="margin:0"></ul>');
			$('#spd-page .pagination').twbsPagination(
					{
						totalPages : pageCount,
						visiblePages : 5,
						version : '1.1',
						startPage : 1,
						onPageClick : function(event, page) {
							var showTotal = $("#spd-page-limit").val();
							var limit = (page) * showTotal;
							var start = (page - 1) * showTotal;
							if (limit > totals) {
								limit = totals;
							}
							$scope.spd_index = start;
							$scope.spd_lastIndex = limit;
							$scope.spd_totals = totals;
							$http.get(
									"../alarm/spd_alarm.action?site=" + $scope.siteId
											+ "&start="
											+ start + "&limit=" + showTotal)
									.success(function(response) {
										$scope.spd_alarm = response.spd_items;
								
									});
						}
					});
		};
		// 地阻分页
		var r_padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.r_isNULL = 1;
				return;
			} else {
				$scope.r_isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$("#r-page").html('<ul class="pagination" style="margin:0"></ul>');
			$('#r-page .pagination').twbsPagination(
					{
						totalPages : pageCount,
						visiblePages : 5,
						version : '1.1',
						startPage : 1,
						onPageClick : function(event, page) {
							var showTotal = $("#r-page-limit").val();
							var limit = (page) * showTotal;
							var start = (page - 1) * showTotal;
							if (limit > totals) {
								limit = totals;
							}
							$scope.r_index = start;
							$scope.r_lastIndex = limit;
							$scope.r_totals = totals;
							$http.get(
									"../alarm/r_alarm.action?site=" + $scope.siteId
											+ "&start="
											+ start + "&limit=" + showTotal)
									.success(function(response) {
										$scope.r_alarm = response.r_items;
									
									});
						}
					});
		};
		// 雷电流分页
		var i_padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.i_isNULL = 1;
				return;
			} else {
				$scope.i_isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$("#i-page").html('<ul class="pagination" style="margin:0"></ul>');
			$('#i-page .pagination').twbsPagination(
					{
						totalPages : pageCount,
						visiblePages : 5,
						version : '1.1',
						startPage : 1,
						onPageClick : function(event, page) {
							var showTotal = $("#i-page-limit").val();
							var limit = (page) * showTotal;
							var start = (page - 1) * showTotal;
							if (limit > totals) {
								limit = totals;
							}
							$scope.i_index = start;
							$scope.i_lastIndex = limit;
							$scope.i_totals = totals;
							$http.get(
									"../alarm/i_alarm.action?site=" +  $scope.siteId
											+ "&start="
											+ start + "&limit=" + showTotal)
									.success(function(response) {
										$scope.i_alarm = response.i_items;
									
									});
						}
					});
		};

	});
};
// 刷新数据
xh.refresh = function() {
	var appElement = document.querySelector('[ng-controller=alarm]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();
	xh.loadPie();

};
xh.ExcelToDEvices=function(){
	var $scope = angular.element(appElement).scope();
	xh.maskShow();
	var site = $scope.siteId;
	$.ajax({
		url : '../site/ExcelToDevices.action',
		data : {
			site : site
		},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			xh.maskHide();
			if(response.success){
				var url="../site/DownExcel?excelName="+site+"号站点告警.xls";	
				window.open(url, '_self','width=1,height=1,toolbar=no,menubar=no,location=no')	
			}else{
				toastr.error("导出数据失败","提示")
			}
		},
		failure : function(response) {
			xh.maskHide();
		}
	});

}
// 图标
xh.loadPie = function() {
	require([ 'echarts', 'echarts/chart/pie' ], function(ec) {

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
				orient : 'horizontal', // 'vertical'
				x : 'right', // 'center' | 'left' | {number},
				y : 'top', // 'center' | 'bottom' | {number}
				backgroundColor : '#eee',
				borderColor : '#fafafa',
				borderWidth : 1,
				padding : 10, // [5, 10, 15, 20]
				itemGap : 20,
				textStyle : {
					color : 'red'
				},
				data : [ '正常', '异常' ]
			},
			calculable : false,
			backgroundColor : '#fff',
			/*
			 * backgroundColor : '#1b1b1b', toolbox : { show : true, feature : {
			 * mark : { show : false }, restore : { show : false }, saveAsImage : {
			 * show : true } } },
			 */
			series : [ {
				type : 'pie',
				radius : [ 60, 70 ],// 半径，默认为Min(width, height) / 2 - 50,
				// 传数组实现环形图，[内半径，外半径]
				center : [ 150, 100 ],
				itemStyle : {
					normal : {// 默认样式
						label : {
							show : true,
							position : 'center',
							formatter : function() {
								var res = "设备总数：" + totals;
								return res;
							},
							textStyle : {
								baseline : 'bottom',
								color : '#000'
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
				data : [ {
					value : parseInt(parseInt(totals) - parseInt(alarm_count)),
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
				} ]
			} ]
		};
		/*
		 * myChart.showLoading({ text : '数据获取中', effect: 'whirling' });
		 */
		myChart.setOption(option);
	});
};
/*xh.ExcelToSite=function(){
	var $scope = angular.element(appElement).scope();
	
	window.location.href="../site/ExcelToDevices?site="+$scope.siteId
}*/
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
					fontSize : 20,
					fontFamily : 'Arial',
					fontWeight : 60,
				// color:'#1a4eb0',
				},
				// 副标题样式
				subtextStyle : {
					fontSize : 18,
					fontFamily : 'Arial',
					color : "#1a4eb0",
				},
			},
			/*
			 * legend : { orient : 'horizontal', // 'vertical' x : 'center', //
			 * 'center' | 'left' | {number}, y : 'bottom', // 'center' |
			 * 'bottom' | {number} backgroundColor : '#eee', borderColor :
			 * '#fafafa', borderWidth : 1, //padding : 10, // [5, 10, 15, 20]
			 * itemGap : 20, textStyle : { color : 'red' }, data : [ 'SPD',
			 * '接地电阻', '雷电流' ] },
			 */
			xAxis : [ {
				type : 'category',
				data : [ 'SPD', '接地电阻', '雷电流' ]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			calculable : false,
			backgroundColor : '#fff',
			series : [ {
				"type" : "bar",
				"data" : [ {
					name : 'spd异常',
					value : spd,
					itemStyle : {
						normal : {
							color : 'green'
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
// 分页
xh.padding = function(totals, count) {
	var appElement = document.querySelector('[ng-controller=alarm]');
	var $scope = angular.element(appElement).scope();
	$scope.pading(totals, count);
};
/* 表格全选 */
xh.tableCheck = function() {
	$(".table-check").click(function() {
		var checkVal = [];
		if (this.checked) {
			$("[name='tb-check']").prop("checked", true);
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("value"));
				}
			});
		} else {
			$("[name='tb-check']").prop("checked", false);
		}
	});
};