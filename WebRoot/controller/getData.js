if (!("xh" in window)) {
	window.xh = {};
};
var setting = {
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		view : {
			dblClickExpand : false
		},
		data : {
			simpleData : {
				enable : true
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
			onClick : onTreeClick
			/*onCheck : onCheck*/
		}
	};
	var setting_r = {
			check : {
				enable : true,
				chkStyle : "radio",
				radioType : "all"
			},
			view : {
				dblClickExpand : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : function(treeId, treeNode) {
					var zTree = $.fn.zTree.getZTreeObj("rTree");
					if (treeNode.isParent) {
						zTree.expandNode(treeNode);
						return false;
					} else {
						// demoIframe.attr("src", treeNode.file + ".html");
						return true;
					}
				},
				onClick : onRTreeClick
				/*onCheck : onCheck*/
			}
		};
	var setting_m = {
			check : {
				enable : true,
				chkStyle : "radio",
				radioType : "all"
			},
			view : {
				dblClickExpand : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : function(treeId, treeNode) {
					var zTree = $.fn.zTree.getZTreeObj("mTree");
					if (treeNode.isParent) {
						zTree.expandNode(treeNode);
						return false;
					} else {
						// demoIframe.attr("src", treeNode.file + ".html");
						return true;
					}
				},
				onClick : onMTreeClick
				/*onCheck : onCheck*/
			}
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
var app = angular.module("app", []);
app.controller("rtu", function($scope, $http) {
	$scope.bussiness_isActive=true;
	$scope.get_isActive=true;
	
//	$scope.getModel = function(){
//		var siteId = $("#siteId").val();
//		var deviceId = $("#deviceId").val();
//		if(siteId == "" || siteId == null || deviceId == "" || deviceId == null){
//			swal({
//				title : "提示",
//				text : "请先选择站点和设备",
//				type : "error"
//			});
//		} else {
//			$http.get('../site/r_model.action?siteId='+siteId+'&r_deviceId='+deviceId).success(function(res){
//				var zNodes = response.items;
//				var t = $("#mTree");
//				t = $.fn.zTree.init(t, setting_m, zNodes);
//				$("#mTree").show();
//			});
//		}
//	};
//	
//	$scope.setModel = function(index){
//		var model = $scope.model_data[index].model;
//		$("input[name='model']").val(model);
//	};
	
	$("#select-btn").click(function(e){
		$("#tree").show();
		e.stopPropagation(); //阻止事件冒泡，否则事件会冒泡到下面的文档点击事件 
	});
	$("#r-select-btn").click(function(e){
		$("#rTree").show();
		e.stopPropagation(); //阻止事件冒泡，否则事件会冒泡到下面的文档点击事件 
	});
	$("#model-select-btn").click(function(e){
		$("#mTree").show();
		e.stopPropagation();
	});
	xh.hideTree("tree");
	$http.get("../site/menu.action").success(function(response) {
		var zNodes = response.items;
		var t = $("#tree");
		t = $.fn.zTree.init(t, setting, zNodes);
	});
	
});
var timeout = false; //启动及关闭按钮  
var i=35;
var clearInter;
xh.start=function(){
	$("#sendBtn").attr("disabled","disabled");
	$("#r-value").html("");
	clearInter=setInterval(startInterval,1000)  ;
	
	$.ajax({
		url : '../rtu/rtu_data.action',
		type : 'post',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceId:$("input[name='deviceId']").val(),
			model:$("input[name='model']").val()
		},
		async : true,
		success : function(data) {
			$("#sendBtn").removeAttr("disabled");//将按钮可用
			clearInterval(clearInter);
			$("#sendBtn").text('获取数据');
			i=35;
			if (data.success) {
				toastr.success("获取数据完成",'提示');
				if(data.value==0){
					swal({
						title : "提示",
						text : "获取地阻失败",
						type : "error"
					});
				}else{
					$("#r-value").html(data.value);
				}
				
				
			} else {
				
				swal({
					title : "提示",
					text : data.message,
					type : "error"
				});
				
			}
		},
		error : function() {
			$("#sendBtn").removeAttr("disabled");//将按钮可用
			clearInterval(clearInter);
			$("#sendBtn").text('获取数据');
			i=35;
		}
	});
};
function startInterval()
{
	
	$("#sendBtn").text("剩余时间："+i+" s");
	if(i==0){
		clearInterval(clearInter);
		$("#sendBtn").text('获取数据');
		i=35;
	}
	i--;
	
 }
xh.hideTree = function(str) {
	var isOut = true;
	var dom = document.getElementById('tree');
	var dom2 = document.getElementById('rTree');
	var dom3 = document.getElementById('mTree');
	var other = window.document;
	other.onclick = function() {
		if (isOut) {
			dom.style.display = 'none';
			dom2.style.display = 'none';
			dom3.style.display = 'none';
		}
		isOut = true;
	};
	dom.onclick = function() {
		isOut = false;
		this.style.display = 'block';
	};
	dom2.onclick = function() {
		isOut = false;
		this.style.display = 'block';
	};
	dom3.onclick = function() {
		isOut = false;
		this.style.display = 'block';
	};

};
function onTreeClick(event, treeId, treeNode){
	$("input[name='siteId']").val(treeNode.siteId);
	$.ajax({
		url : '../site/r_tree.action',
		data : {
			siteId : treeNode.siteId
		},
		type : 'get',
		dataType : "json",
		async : false,
		success : function(response) {
			var zNodes = response.items;
			console.log(zNodes);
			var t = $("#rTree");
			t = $.fn.zTree.init(t, setting_r, zNodes);
			$("#r-select-btn").removeAttr("disabled");//将按钮可用
			
		},
		failure : function(response) {
		}
	});
}

function onMTreeClick(event, treeId, treeNode){
	$("input[name='model']").val(treeNode.model);
}

function onRTreeClick(event, treeId, treeNode){
	$("input[name='deviceId']").val(treeNode.id);
	var siteId = $("input[name='siteId']").val();
	$.ajax({
		url : '../site/r_model.action',
		data : {
			siteId: siteId,
			r_deviceId: treeNode.id
		},
		type : 'get',
		dataType : "json",
		async : false,
		success : function(response) {
			var zNodes = response.items;
			var node = [];
			for(var i=0;i<zNodes.length;i++){
				var newNode = {};
				newNode.model = zNodes[i].model;
				newNode.name = '地阻检测点'+zNodes[i].model;
				node.push(newNode);
			}
			console.log(zNodes);
			var t = $("#mTree");
			t = $.fn.zTree.init(t, setting_m, node);
			$("#model-select-btn").removeAttr("disabled");//将按钮可用
			
		},
		failure : function(response) {
		}
	});
	
}
