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
	/* 消失时间 */
	"timeOut" : "1000",
	"extendedTimeOut" : "1000",
	"showMethod" : "fadeIn",
	"hideMethod" : "fadeOut",
	"progressBar" : true
};
var zTree;
var demoIframe;

var setting = {
	view : {
		dblClickExpand : false,
		showLine : true,
		selectedMulti : false
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "idkey"/*
							 * rootPId : ""
							 */
		}
	},
	callback : {
		beforeClick : function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.isParent) {
				zTree.expandNode(treeNode);
				return false;
			} else {
				// demoIframe.attr("src", treeNode.file + ".html");
				return true;
			}
		},
		onClick : TreeOnClick
	}
};
/*
 * var zNodes = [ { id : 1, pId : 0, name : "[core] 基本功能 演示", open : true }, {
 * id : 101, pId : 1, name : "最简单的树 -- 标准 JSON 数据", file : "core/standardData" }, {
 * id : 102, pId : 1, name : "最简单的树 -- 简单 JSON 数据", file : "core/simpleData" }, {
 * id : 103, pId : 1, name : "不显示 连接线", file : "core/noline" } ];
 */
/* 加载站点呢数据 */
xh.loadData = function() {
	// xh.maskShow();

	var app = angular.module("app", []);
	var start = 0;
	app.controller("site", function($scope, $http) {

		var num = $("#page-limit").val();
		$scope.count = "10";
		$scope.data_isActive=true;
		/*$http.get("../site/data.action?start=" + start + "&limit=" + num)
				.success(function(response) {
					$scope.data = response.items;
				});*/
		$http.get("../site/menu.action").success(function(response) {
			var zNodes = response.items;
			console.log(zNodes);
			var t = $("#tree");
			t = $.fn.zTree.init(t, setting, zNodes);
		});
		//获取spd检测仪下的spd模块
		$scope.spdModule=function(spdId){		
			$("#spd_module").modal('show');
			$http.get(
					"../site/spd_module.action?siteId="+$scope.site_items[0].siteId+"&spd_deviceId=" +spdId).success(
					function(response) {
						$scope.spd_module_data = response.items;
						$scope.spdId=spdId;
						if(response.items.length<1){
							$scope.spd_module_isNULL = 1;
						}else{
							$scope.spd_module_isNULL = 0;
						}
					});
		};
		// 刷新数据
		$scope.refresh = function() {
			xh.maskShow();
			$http.get(
					"../site/data.action?start=" + start + "&limit="
							+ $("#page-limit").val()).success(
					function(response) {
						$scope.data = response.items;
						padding(parseInt(response.total), $("#page-limit")
								.val());
						xh.maskHide();
					});
		};

		$scope.loadForm = function(id) {
			// $scope.refresh();
			$http.get("../site/id_data.action?siteId=" + id).success(
					function(response) {
						$scope.site_items=response.site_items;
						$scope.spd_items=response.spd_items;
						$scope.i_items=response.i_items;
						$scope.r_items=response.r_items;
						
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
						

					});

		};

	});
};
// 添加站点
xh.add = function() {
	$.ajax({
		url : '../site/add.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#addSiteForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#add').modal('hide');
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
// 修改用户
xh.update = function() {
	/*
	 * var appElement = document.querySelector('[ng-controller=site]'); var
	 * $scope = angular.element(appElement).scope(); //调用$scope中的方法 var
	 * str=$("#updateSiteForm").serializeArray(); str.push("id",$scope.id);
	 */
	$.ajax({
		url : '../site/update.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#updateSiteForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#update').modal('hide');
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
			$('#update').modal('hide');
			xh.refresh();
		}
	});
};
xh.loadForm = function(id) {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	$scope.loadForm(id);

};
function TreeOnClick(event, treeId, treeNode) {
	console.log(treeNode)
	xh.loadForm(treeNode.siteId);
};
// 刷新数据
xh.refresh = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();

};
// 分页
xh.padding = function(totals, count) {
	var appElement = document.querySelector('[ng-controller=site]');
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