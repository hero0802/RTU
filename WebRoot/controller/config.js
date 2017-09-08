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
xh.load=function(){
	var app = angular.module("app", []);
	app.controller("config",function($http,$scope){
		$scope.config_isActive=true;
		$http.get("../config/data.action")
		.success(function(response) {
			/*$scope.ip=response.ip;
			$scope.port=response.port;*/
			$scope.i_time = response.i_time;
			$scope.r_time = response.r_time;
			$scope.spd_time = response.spd_time;
			
		$scope.update=function(){
			$.ajax({
				url : '../config/update.action',
				type : 'post',
				dataType : "json",
				async : false,
				data : {
					i_time : $("#i_time").val(),
					/*ip : $("#ip").val(),
					port : $("#port").val(),*/
					r_time:$("#r_time").val(),
					spd_time:$("#spd_time").val()
					
				},
				success : function(data) {
					if (data.success) {
						toastr.success("更新参数成功", '提示');
					} else {
						swal({
							title : "提示",
							text : "错误码",
							type : "error"
						});
					}
				},
				error : function() {
				}
			});
			
		};
		});
	});
};

