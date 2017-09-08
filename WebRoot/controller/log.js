if (!("xh" in window)) {
	window.xh = {};
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
		/*  消失时间 */
		"timeOut" : "1000",
		"extendedTimeOut" : "1000",
		"showMethod" : "fadeIn",
		"hideMethod" : "fadeOut",
		"progressBar" : true
	};
/*加载日志*/
xh.loadLog = function() {
	xh.maskShow();
	
	xh.tableCheck();
	
	var app = angular.module("app", []);
	var start = 0;
	app.controller("log", function($scope, $http) {
		var data=xh.loginUserPower();
		$scope.del_btn=data==null?0:data.m_deleteLog;
		var num = $("#page-limit").val();
		$scope.system_isActive=true;
		$scope.log_isActive=true;
		$scope.count = "10";
		$http.get("../log/data.action?start=" + start + "&limit=" + $scope.count)
				.success(function(response) {
					$scope.data = response.items;
					padding(parseInt(response.total), $scope.count);
		});
		// 刷新日志记录
		$scope.refresh = function() {
			xh.maskShow();
			$http.get(
					"../log/data.action?start=" + start + "&limit="
							+ $("#page-limit").val()).success(
					function(response) {
						$scope.data = response.items;
						padding(parseInt(response.total), $("#page-limit").val());
						xh.maskHide();
					});
		};
		// 删除日志记录
		$scope.delLog= function(id) {
			swal({
				title : "提示",
				text : "确定要删除吗？",
				type : "info",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消"
				/*closeOnConfirm : false,
				closeOnCancel : false*/
			},function(isConfirm){
				if(isConfirm){
					$.ajax({
						url : '../log/del.action',
						type : 'post',
						dataType : "json",
						data : {
							deleteids:id
						},
						async : false,
						success : function(data) {
							if (data.success) {
								toastr.success("删除日志成功",'提示');
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
				}
			});
		};
		// 批量删除
		$scope.delManyLog= function() {
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("value"));
				}
			});
			if (checkVal.length < 1) {
				swal({
					title : "提示",
					text : "请至少选择一条数据",
					type : "error"
				});
				return;
			}
			$.ajax({
				url : '../log/del.action',
				type : 'post',
				dataType : "json",
				data : {
					deleteids:checkVal.join(",")
				},
				async : false,
				success : function(data) {
					if (data.success) {
						toastr.success("删除日志成功",'提示');
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
		// 分页
		var padding = function(totals, one_count) {
			if(totals==0){
				$scope.isNULL=1;
				return;
			}else{
				$scope.isNULL=0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$(".page-paging").html('<ul class="pagination"></ul>');
			$('.pagination').twbsPagination({
				totalPages : pageCount,
				visiblePages : 5,
				version : '1.1',
				startPage : 1,
				onPageClick : function(event, page) {
					xh.maskShow();
					var showTotal = $("#page-limit").val();
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
								"../log/data.action?start=" + start
										+ "&limit=" + showTotal).success(
								function(response) {
									$scope.data = response.items;
									xh.maskHide();

								});
					}
			});
			
			};	
			
			});
};
//刷新数据
xh.refresh=function(){
	var appElement = document.querySelector('[ng-controller=log]');
	var $scope = angular.element(appElement).scope();
	 //调用$scope中的方法
	 $scope.refresh();
	
};
//分页
xh.padding=function(totals,count){
	var appElement = document.querySelector('[ng-controller=log]');
	var $scope = angular.element(appElement).scope();
	 $scope.pading(totals,count);
};
/*表格全选*/
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