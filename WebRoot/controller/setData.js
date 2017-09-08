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
	$scope.bussiness_isActive=true;
	$scope.set_isActive=true;
	
});
//设置地址码
xh.setDeviceid=function(){
	$.ajax({
		url : '../rtu/setdeviceid.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			deviceId2:$("input[name='deviceid2']").val()
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
//设置设备系统分秒
xh.setTimeMS=function(){
	$.ajax({
		url : '../rtu/settimems.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			timeM:$("input[name='timeM']").val(),
			timeS:$("input[name='timeS']").val()
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
//设置设备系统日时
xh.setTimeDH=function(){
	$.ajax({
		url : '../rtu/settimedh.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			timeD:$("input[name='timeD']").val(),
			timeH:$("input[name='timeH']").val()
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
//设置设备系统年月
xh.setTimeYM=function(){
	$.ajax({
		url : '../rtu/settimeym.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			timeYear:$("input[name='timeYear']").val(),
			timeMonth:$("input[name='timeMonth']").val()
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
//设置雷击记录数
xh.setIcount=function(){
	$.ajax({
		url : '../rtu/seticount.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			icount:$("input[name='icount']").val()
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
//设置雷击记录数索引
xh.setIindex=function(){
	$.ajax({
		url : '../rtu/setiindex.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			iindex:$("input[name='iindex']").val()
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
//参数复位
xh.dataBack=function(){
	$.ajax({
		url : '../rtu/databack.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			databack:$("input[name='databack']:checked").val()
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
//设备重启
xh.reboot=function(){
	$.ajax({
		url : '../rtu/reboot.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			reboot:$("input[name='reboot']:checked").val()
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
//清除历史记录
xh.clearAlarm=function(){
	$.ajax({
		url : '../rtu/clearalarm.action',
		type : 'get',
		dataType : "json",
		data : {
			rtuId:$("input[name='siteId']").val(),
			deviceType:$("input[name='devicetype']:checked").val(),
			deviceId:$("input[name='deviceid']").val(),
			clearAlarm:$("input[name='clearAlarm']:checked").val()
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