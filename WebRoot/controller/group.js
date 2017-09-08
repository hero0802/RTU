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
/*加载站点呢数据*/
xh.loadData = function() {	
	xh.maskShow();
	xh.tableCheck();
	var app = angular.module("app", []);
	var start = 0;
	app.controller("group", function($scope, $http) {
		var data=xh.loginUserPower();
		$scope.add_btn=data==null?0:data.m_addGroup;
		$scope.del_btn=data==null?0:data.m_deleteGroup;
		$scope.update_btn=data==null?0:data.m_updateGroup;
		var num = $("#page-limit").val();
		$scope.count = "10";
		$scope.system_isActive=true;
		$scope.group_isActive=true;
		$http.get("../group/data.action?start=" + start + "&limit=" + num)
				.success(function(response) {
					$scope.data = response.items;
					padding(parseInt(response.total), num);
		});
		// 刷新数据
		$scope.refresh = function() {
			xh.maskShow();
			$http.get(
					"../group/data.action?start=" + start + "&limit="
							+ $("#page-limit").val()).success(
					function(response) {
						$scope.data = response.items;
						padding(parseInt(response.total), $("#page-limit").val());
						xh.maskHide();
					});
		};
		// 删除用户组
		$scope.del = function(id) {
			swal({
				title : "提示",
				text : "确定要删除该用户组吗？",
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
						url : '../group/del.action',
						type : 'post',
						dataType : "json",
						data : {
							deleteids:id
						},
						async : false,
						success : function(data) {
							if (data.success) {
							
								toastr.success("删除用户组成功",'提示');
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
							xh.refresh();
						}
					});
				}
			});
		};
		// 批量删除
		$scope.delMore = function() {
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
				url : '../group/del.action',
				type : 'post',
				dataType : "json",
				data : {
					deleteids:checkVal.join(",")
				},
				async : false,
				success : function(data) {
					if (data.success) {
						toastr.success('删除用户组成功','提示');
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
		// 修改用户组
		$scope.update = function(id) {
			console.log($scope.data[id]);
			$scope.id=$scope.data[id].id;
			$scope.name=$scope.data[id].name;
			$scope.level=$scope.data[id].level;
			
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
				visiblePages : 7,
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
								"../group/data.action?start=" + start
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
//添加用户组
xh.add=function(){
	$.ajax({
		url : '../group/add.action',
		type : 'post',
		dataType : "json",
		async : false,
		data :$("#addForm").serializeArray(),
		success : function(data) {
			if (data.success) {
				
				$('#add').modal('hide');
				/*swal({
					title : "提示",
					text : data.message,
					type : "success"
				});*/
				toastr.success(data.message,'提示');
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
//修改用户
xh.save=function(){
	$.ajax({
		url : '../group/update.action',
		type : 'post',
		dataType : "json",
		async : false,
		data :$("#updateForm").serializeArray(),
		success : function(data) {
			if (data.success) {
				
				$('#update').modal('hide');
				toastr.success(data.message,'提示');
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
			$('#update').modal('hide');
			xh.refresh();
		}
	});
};
//刷新数据
xh.refresh=function(){
	var appElement = document.querySelector('[ng-controller=group]');
	var $scope = angular.element(appElement).scope();
	 //调用$scope中的方法
	 $scope.refresh();
	
};
//分页
xh.padding=function(totals,count){
	var appElement = document.querySelector('[ng-controller=group]');
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