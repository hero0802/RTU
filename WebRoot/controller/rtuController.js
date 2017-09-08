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
var app = angular.module("app", []);
app.controller("rtu", function($scope, $http) {
	
});
xh.start=function(){
	$.ajax({
		url : '../site/start.action',
		type : 'get',
		dataType : "json",
		data : {
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			func:$("input[name='func']:checked").val()
		},
		async : true,
		success : function(data) {
			if (data.success) {
				toastr.success("发送成功",'提示');
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