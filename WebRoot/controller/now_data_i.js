if (!("xh" in window)) {
	window.xh = {};
};
require.config({
	paths : {
		echarts : '../resources/static/echarts'
	}
});

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
	xh.maskShow();
	//xh.loadPie();
	xh.tableCheck();

	var app = angular.module("app", []);
	var start = 0;
	app.controller("alarm", function($scope, $http) {
		var num = $("#page-limit").val();
		var site = $("#site").val();
		var province = "0";
		var city ="0";
		var county ="0";
		var company = $("#company").val();
		$scope.count = "20";
		$scope.data_isActive=true;
		$http.get(
				"../site/now_data_i.action?site=" + site + "&province=" + province
						+ "&city=" + city +"&county="+county +  "&company=" + company
						+ "&start=" + start + "&limit="
						+ $("#page-limit").val()).success(function(response) {
			$scope.dataI = response.items;
			xh.maskHide();
			padding(parseInt(response.total), $scope.count);
		}).error(function() {
			xh.maskHide();
		});
		/*//省份
		$http.get(
				"../zone/province").success(
				function(response) {
					$scope.provinceItems = response.items;
				});
		//市
		$http.get(
				"../zone/city").success(
				function(response) {
					$scope.cityItems = response.items;
				});
		//县
		$http.get(
				"../zone/district").success(
				function(response) {
					$scope.districtItems = response.items;
				});*/
		// 刷新数据
		$scope.refresh = function() {
			xh.maskShow();
			var site = $("#site").val();
			var province = $("#selProvince").val();
			var city = $("#selCity").val();
			var county = $("#selCounty").val();
			var company = $("#company").val();
			$http.get(
					"../site/now_data_i.action?site=" + site + "&province=" + province
					+ "&city=" + city +"&county="+county+ "&company=" + company
					+ "&start=" + start + "&limit="
					+ $("#page-limit").val()).success(
					function(response) {
						$scope.dataI = response.items;
						padding(parseInt(response.total), $("#page-limit")
								.val());
						xh.maskHide();
					});
			
		};
		// 重载数据
		$scope.reload = function() {
			xh.maskShow();
			$("#settingForm")[0].reset();
			var site = $("#site").val();
			var province = $("#selProvince").val();
			var city = $("#selCity").val();
			var county = $("#selCounty").val();
			var company = $("#company").val();
			//xh.loadPie();
			$http.get(
					"../site/now_data_i.action?site=" + site + "&province=" + province
					+ "&city=" + city +"&county="+county +"&company=" + company
					+ "&start=" + start + "&limit="
					+ $("#page-limit").val()).success(
					function(response) {
						$scope.dataI = response.items;
						padding(parseInt(response.total), $("#page-limit")
								.val());
						xh.maskHide();
					});
		};
		// 查询数据
		$scope.search = function() {
			$("#aside-right").fadeOut("fast");
			xh.maskShow();
			var site = $("#site").val();
			var province = $("#selProvince").val();
			var city = $("#selCity").val();
			var county = $("#selCounty").val();
			var company = $("#company").val();
			
			$http.get(
					"../site/now_data_i.action?site=" + site + "&province=" + province
					+ "&city=" + city + "&county="+county+"&company=" + company
					+ "&start=" + start + "&limit="
					+ $("#page-limit").val()).success(
					function(response) {
						$scope.dataI = response.items;
						padding(parseInt(response.total), $("#page-limit").val());
						xh.maskHide();
					});
		};
		
		var padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.isNULL = 1;
				return;
			} else {
				$scope.isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$(".page-paging").html('<ul class="pagination"></ul>');
			$('.pagination').twbsPagination(
					{
						totalPages : pageCount,
						visiblePages : 5,
						version : '1.1',
						startPage : 1,
						onPageClick : function(event, page) {
							xh.maskShow();
							var site = $("#site").val();
							var province = $("#selProvince").val()==null?"0": $("#selProvince").val();
							var city = $("#selCity").val()==null?"0": $("#selCity").val();
							var county = $("#selCounty").val()==null?"0":$("#selCounty").val();
							var company = $("#company").val();
							var limit = (page) * $("#page-limit").val();
							var start = (page - 1) * $("#page-limit").val();
							if (limit > totals) {
								limit = totals;
							}
							$scope.index = start;
							$scope.lastIndex = limit;
							$scope.totals = totals;
							$scope.nowPage = page;
							$http.get(
									"../site/now_data_i.action?site=" + site + "&province=" + province
									+ "&city=" + city +"&county="+county+ "&company=" + company
									+ "&start=" + start + "&limit="
									+ $("#page-limit").val())
									.success(function(response) {
										$scope.dataI = response.items;
										xh.maskHide();

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
xh.ExcelToI=function(){
	xh.maskShow();
	var site = $("#site").val();
	var province = $("#selProvince").val();
	var city = $("#selCity").val();
	var county = $("#selCounty").val();
	var company = $("#company").val();
	$.ajax({
		url : '../site/ExcelToI.action',
		data : {
			site : site,
			province : province,
			city : city,
			county:county,
			company:company
		},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			xh.maskHide();
			if(response.success){
				var url="../site/DownExcel?excelName=雷击电流实时告警.xls";	
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
		var spd = 0, r = 0, i = 0;
		var site = $("#site").val();
		var timeStart = $("#clockS").val();
		var timeEnd = $("#clockE").val();

		$.ajax({
			url : '../alarm/count.action',
			data : {
				site : site,
				timeStart : timeStart,
				timeEnd : timeEnd
			},
			type : 'post',
			dataType : "json",
			async : false,
			success : function(response) {
				spd = response.spd;
				r = response.r;
				i = response.i;
				$("#spd_val").html(spd);
				$("#r_val").html(r);
				$("#i_val").html(i);
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
				orient : 'vertical',
				x : 'left',
				data : [ 'spd', '地阻--R', '雷电流--I' ]
			},
			calculable : true,
			backgroundColor : '#fff',
			/*
			 * backgroundColor : '#1b1b1b', toolbox : { show : true, feature : {
			 * mark : { show : false }, restore : { show : false }, saveAsImage : {
			 * show : true } } },
			 */
			series : [ {
				type : 'pie',
				radius : [ 40, 70 ],// 半径，默认为Min(width, height) / 2 - 50,
				// 传数组实现环形图，[内半径，外半径]
				center : [ 230, 100 ],
				itemStyle : {
					normal : {// 默认样式
						label : {
							show : true
						},
						labelLine : {
							show : true
						},
						x : 'right'
					},
					emphasis : {// 强调样式（悬浮时样式
						label : {
							show : false,
							position : 'top',
							textStyle : {
								fontSize : '30',
								fontWeight : 'bold'
							}
						}

					}
				},
				data : [ {
					value : spd,
					name : 'spd'
				}, {
					value : r,
					name : '地阻--R'
				}, {
					value : i,
					name : '雷电流--I'
				} ]
			} ]
		};
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