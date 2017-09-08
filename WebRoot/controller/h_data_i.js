/**
 * 邮件
 */
if (!("xh" in window)) {
	window.xh = {};
};
var frist = 0;
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
		"progressBar" : true,
	};
xh.loadData  = function() {
	var app = angular.module("app", []);
	var pageSize = $("#page-limit").val();
	app.controller("alarm", function($scope, $http) {
		xh.maskShow();
		var num = $("#page-limit").val();
		var site = $("#site").val();
		var deviceId=$("#deviceId").val();
		var province = "0";
		var city ="0";
		var county ="0";
		var company = $("#company").val();
		var startTime = $("#clockS").val();
		var endTime = $("#clockE").val();
		var start=0;
		$scope.data_isActive=true;
		$scope.count = "20";
		$http.get(
				"../site/h_data_i.action?site=" + site + "&province=" + province
				+ "&city=" + city+"&county="+county + "&company=" + company
				+ "&deviceId="+deviceId+"&startTime=" + startTime + "&endTime=" + endTime
				+ "&start=" + start + "&limit="
				+ $("#page-limit").val()).
			success(function(response) {
			xh.maskHide();
			$scope.data = response.items;
			$scope.totals = response.total;
			xh.pagging(1, parseInt($scope.totals),$scope);
		});
		/* 刷新数据 */
		$scope.refresh = function() {
			$scope.search(1);
		};
		/* 查询数据 */
		$scope.search = function(page) {
			var num = $("#page-limit").val();
			var site = $("#site").val();
			var province = $("#selProvince").val();
			var city = $("#selCity").val();
			var county = $("#selCounty").val();
			var company = $("#company").val();
			var deviceId=$("#deviceId").val();
			var startTime = $("#clockS").val();
			var endTime = $("#clockE").val();
			var pageSize = $("#page-limit").val();
			var start = 1, limit = pageSize;
			frist = 0;
			page = parseInt(page);
			if (page <= 1) {
				start = 0;

			} else {
				start = (page - 1) * pageSize;
			}
			xh.maskShow();
			$http.get(
					"../site/h_data_i.action?site=" + site + "&province=" + province
					+ "&city=" + city+"&county="+county + "&company=" + company
					+ "&deviceId="+deviceId+"&startTime=" + startTime + "&endTime=" + endTime
					+ "&start=" + start + "&limit="
					+ $("#page-limit").val()).
			success(function(response){
				xh.maskHide();
				$scope.data = response.items;
				$scope.totals = response.total;
				xh.pagging(page, parseInt($scope.totals),$scope);
			});
		};
		//分页点击
		$scope.pageClick = function(page,totals, totalPages) {
			var pageSize = $("#page-limit").val();
			var num = $("#page-limit").val();
			var site = $("#site").val();
			var province = $("#selProvince").val();
			var city = $("#selCity").val();
			var county = $("#selCounty").val();
			var company = $("#company").val();
			var deviceId=$("#deviceId").val();
			var startTime = $("#clockS").val();
			var endTime = $("#clockE").val();
			var start = 1, limit = pageSize;
			page = parseInt(page);
			if (page <= 1) {
				start = 0;
			} else {
				start = (page - 1) * pageSize;
			}
			xh.maskShow();
			$http.get(
					"../site/h_data_i.action?site=" + site + "&province=" + province
					+ "&city=" + city+"&county="+county + "&company=" + company
					+ "&deviceId="+deviceId+"&startTime=" + startTime + "&endTime=" + endTime
					+ "&start=" + start + "&limit="
					+ $("#page-limit").val()).
			success(function(response){
				xh.maskHide();
				
				$scope.start = (page - 1) * pageSize + 1;
				$scope.lastIndex = page * pageSize;
				if (page == totalPages) {
					if (totals > 0) {
						$scope.lastIndex = totals;
					} else {
						$scope.start = 0;
						$scope.lastIndex = 0;
					}
				}
				$scope.data = response.items;
				$scope.totals = response.total;
			});
			
		};
	});
};

/* 修改信息 */
xh.update = function() {
	$.ajax({
		url : '../../web/user/update',
		type : 'POST',
		dataType : "json",
		async : false,
		data : $("#editForm").serializeArray(),
		success : function(data) {
			if (data.success) {
				$('#edit').modal('hide');
				toastr.success(data.message, '提示');
				xh.refresh();

			} else {
				swal({
					title : "提示",
					text : data.message,
					type : "error"
				});
			}
		},
		error : function() {
		}
	});
};

// 刷新数据
xh.refresh = function() {
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();

};
xh.ExcelToIh=function(){
	xh.maskShow();
	var site = $("#site").val();
	var province = $("#selProvince").val();
	var city = $("#selCity").val();
	var county = $("#selCounty").val();
	var company = $("#company").val();
	var startTime = $("#clockS").val();
	var endTime = $("#clockE").val();
	$.ajax({
		url : '../site/ExcelToIh.action',
		data : {
			site : site,
			province : province,
			city : city,
			county:county,
			company:company,
			startTime:startTime,
			endTime:endTime
		},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			xh.maskHide();
			if(response.success){
				var url="../site/DownExcel?excelName=雷击电流历史告记录.xls";	
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
/* 数据分页 */
xh.pagging = function(currentPage,totals, $scope) {
	var pageSize = $("#page-limit").val();
	var totalPages = (parseInt(totals, 10) / pageSize) < 1 ? 1 : Math
			.ceil(parseInt(totals, 10) / pageSize);
	var start = (currentPage - 1) * pageSize + 1;
	var end = currentPage * pageSize;
	if (currentPage == totalPages) {
		if (totals > 0) {
			end = totals;
		} else {
			start = 0;
			end = 0;
		}
	}
	$scope.start = start;
	$scope.lastIndex = end;
	$scope.totals = totals;
	if (totals > 0) {
		$(".page-paging").html('<ul class="pagination"></ul>');
		$('.pagination').twbsPagination({
			totalPages : totalPages,
			visiblePages : 10,
			version : '1.1',
			startPage : currentPage,
			onPageClick : function(event, page) {
				if (frist == 1) {
					$scope.pageClick(page, totals, totalPages);
				}
				frist = 1;

			}
		});
	}
};

