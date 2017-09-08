/**
 * 邮件
 */
if (!("xh" in window)) {
	window.xh = {};
};
var frist = 0;
var appElement = document.querySelector('[ng-controller=user]');
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
xh.load = function() {
	var app = angular.module("app", []);
	var pageSize = $("#page-limit").val();
	app.controller("user", function($scope, $http) {
		xh.maskShow();
		$scope.count = "20";//每页数据显示默认值
		/*获取邮件信息*/
		$http.get("../../center/email/list?start=0&limit="+pageSize).
		success(function(response){
			xh.maskHide();
			$scope.data = response.items;
			$scope.totals = response.totals;
			xh.pagging(1, parseInt($scope.totals),$scope);
		});
		/* 刷新数据 */
		$scope.refresh = function() {
			$scope.search(1);
		};
		/* 显示详细model */
		$scope.detail = function(id) {
			$scope.editData = $scope.data[id];
			$scope.setReaded($scope.editData.id);
		};
		/*标记为已读*/
		$scope.setReaded = function(id) {
			$.ajax({
				url : '../../center/email/update',
				type : 'post',
				dataType : "json",
				data : {
					id : id
				},
				async : false,
				success : function(data) {
					xh.refresh();
				},
				error : function() {
				}
			});
		};
		/* 删除邮件 */
		$scope.del = function(id) {
			$.ajax({
				url : '../../center/email/del',
				type : 'post',
				dataType : "json",
				data : {
					id : id
				},
				async : false,
				success : function(data) {
					if (data.success) {
						toastr.success("删除邮件成功", '提示');
						$scope.refresh();
			    	
					} else {
						toastr.error("删除邮件失败", '提示');
					}
				},
				error : function() {
					$scope.refresh();
				}
			});
		};
		/* 查询数据 */
		$scope.search = function(page) {
			var $scope = angular.element(appElement).scope();
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
			$http.get("../../center/email/list?start="+start+"&limit="+pageSize).
			success(function(response){
				xh.maskHide();
				$scope.data = response.items;
				$scope.totals = response.totals;
				xh.pagging(page, parseInt($scope.totals),$scope);
			});
		};
		//分页点击
		$scope.pageClick = function(page,totals, totalPages) {
			var pageSize = $("#page-limit").val();
			var starttime=$("#starttime").val();
			var endtime=$("#endtime").val();
			var user=$("#user").val();
			var type=$("#type").val();
			var start = 1, limit = pageSize;
			page = parseInt(page);
			if (page <= 1) {
				start = 0;
			} else {
				start = (page - 1) * pageSize;
			}
			xh.maskShow();
			$http.get("../../center/email/list?start="+start+"&limit="+pageSize).
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
				$scope.totals = response.totals;
			});
			
		};
	});
};
/* 添加用户*/
xh.add = function() {
	$.ajax({
		url : '../../web/user/add',
		type : 'POST',
		dataType : "json",
		async : true,
		data : $("#addForm").serializeArray(),
		success : function(data) {

			if (data.success) {
				$('#add').modal('hide');
				xh.refresh();
				toastr.success(data.message, '提示');

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
/* 批量删除*/
xh.delMore = function() {
	var checkVal = [];
	$("[name='tb-check']:checkbox").each(function() {
		if ($(this).is(':checked')) {
			checkVal.push($(this).attr("value"));
		}
	});
	if (checkVal.length < 1) {
		toastr.error("请至少选择一条数据", '提示');
		return;
	}
	$.ajax({
		url : '../../center/email/del',
		type : 'post',
		dataType : "json",
		data : {
			id : checkVal.join(",")
		},
		async : false,
		success : function(data) {
			if (data.success) {
				toastr.success("删除邮件成功", '提示');
				xh.refresh();
			} else {
				toastr.error("删除邮件失败", '提示');
			}
		},
		error : function() {
		}
	});
};
/* 标记为已读*/
xh.setReaded = function() {
	var checkVal = [];
	$("[name='tb-check']:checkbox").each(function() {
		if ($(this).is(':checked')) {
			checkVal.push($(this).attr("value"));
		}
	});
	if (checkVal.length < 1) {
		toastr.error("请至少选择一条数据", '提示');
		return;
	}
	$.ajax({
		url : '../../center/email/update',
		type : 'post',
		dataType : "json",
		data : {
			id : checkVal.join(",")
		},
		async : false,
		success : function(data) {
			if (data.success) {
				toastr.success("标记成功", '提示');
				xh.refresh();
			} else {
				toastr.error("标记失败", '提示');
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

