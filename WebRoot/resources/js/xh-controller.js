/*between：检测输入的值是否在两个指定的值之间。
callback：通过回调函数返回验证信息。
creditCard：验证信用卡格式。
different：如果输入值和给定的值不同返回true。
digits：如果输入的值只包含数字返回true。
emailAddress：验证电子邮件格式是否有效。
greaterThan：如果输入的值大于或等于指定的值返回true。
hexColor：验证一个hex格式的颜色值是否有效。
identical：验证输入的值是否和指定字段的值相同。
lessThan：如果输入的值小于或等于指定的值返回true。
notEmpty：检测字段是否为空。
regexp：检测输入值是否和指定的javascript正则表达式匹配。
remote：通过AJAX请求来执行远程代码。
stringLength：验证字符串的长度。
uri：验证URL地址是否有效。
usZipCode：验证美国的邮政编码格式。*/
/*注册账号*/
if (!("xh" in window)) {
	window.xh = {}
}

var provinceArray = [];
var currCity = [];
var currProvince = '';
var currCity = '';

xh.maskShow = function() {
	var html = "<div class='xh-mask text-white'>";
	html += "<i class='fa fa-spinner fa-spin fa-2x'></i>";
	html += "<i class=''>加载中...</i>";
	html += "</div>";
	$("body").append(html);
};
xh.maskHide = function() {
	$(".xh-mask").fadeOut(300);
};
xh.getcookie = function(name) {
	var strcookie = document.cookie;
	var arrcookie = strcookie.split(";");
	for (var i = 0; i < arrcookie.length; i++) {
		var arr = arrcookie[i].split("=");
		if (arr[0].match(name) == name)
			return arr[1];
	}
	return "";
};

/*
 * 获取json文件地址数据
 */

xh.initProvince = function() {
	$.getJSON("../zone/province", function(data) {
		provinceArray = data.items;
		document.all.selProvince.length = 0;
		if(provinceArray.length>1){
			document.all.selProvince.options[0] = new Option("全部","0");
			for (var i = 0; i < provinceArray.length; i++) {
				document.all.selProvince.options[i+1] = new Option(
						provinceArray[i].Name, provinceArray[i].ID);
			}
		}else{
			for (var i = 0; i < provinceArray.length; i++) {
				document.all.selProvince.options[i] = new Option(
						provinceArray[i].Name, provinceArray[i].ID);
			}
		}
		
		
		xh.getCity();
		xh.getCounty();
	});
};

xh.getCity = function() {
	var currProvince=$("#selProvince").val();
	
	$.getJSON("../zone/city?parentId="+currProvince, function(dataArr) {
		var data = dataArr.items;
		document.all.selCity.length = 0;
		if(data.length>1){
			document.all.selCity.options[0] = new Option("全部","0");	
			for (var i = 0; i < data.length; i++) {
				document.all.selCity.options[i+1] = new Option(
	                data[i].Name, data[i].ID);
			}
		}else if(data.length==1){
			for (var i = 0; i < data.length; i++) {
				document.all.selCity.options[i] = new Option(
	                data[i].Name, data[i].ID);
			}
		}else{
			document.all.selCity.options[0] = new Option("全部","0");
		}

		
		xh.getCounty();
		
	});
};

xh.getCounty = function() {
	var currCity=$("#selCity").val();
	if(currCity==null){currCity=0;}
	
	$.getJSON("../zone/district?parentId="+currCity, function(dataArr) {
		var data = dataArr.items;
		document.all.selCounty.length = 0;
		if(data.length>1){
			document.all.selCounty.options[0] = new Option("全部","0");	
			for (var i = 0; i < data.length; i++) {
				document.all.selCounty.options[i+1] = new Option(
	                data[i].Name, data[i].ID);
			}
		}else if(data.length==1){
			for (var i = 0; i < data.length; i++) {
				document.all.selCounty.options[i] = new Option(
	                data[i].Name, data[i].ID);
			}
		}else{
			document.all.selCounty.options[0] = new Option("全部","0");	
		}
		
		
	});
};



xh.initProvince_p = function() {
	$.getJSON("../zone/province", function(data) {
		provinceArray = data.items;
		document.all.Province.length = 0;
		for (var i = 0; i < provinceArray.length; i++) {
			document.all.Province.options[i] = new Option(
					provinceArray[i].Name, provinceArray[i].ID);
		}
		xh.getCity_p();
		/*xh.getCounty_p();*/
	});
};

xh.getCity_p = function() {
	var currProvince=$("#Province").val();
	
	$.getJSON("../zone/city?parentId="+currProvince, function(dataArr) {
		var data = dataArr.items;
		document.all.City.length = 0;
		for (var i = 0; i < data.length; i++) {
			document.all.City.options[i] = new Option(
                data[i].Name, data[i].ID);
		}
		xh.getCounty_p();
		
	});
};

xh.getCounty_p = function() {
	var currCity=$("#City").val();
	if(currCity==null){currCity=0;}
	
	$.getJSON("../zone/district?parentId="+currCity, function(dataArr) {
		var data = dataArr.items;
		document.all.County.length = 0;
		for (var i = 0; i < data.length; i++) {
			document.all.County.options[i] = new Option(
                data[i].Name, data[i].ID);
		}
		
	});
};




xh.nowDateTime = function(long) {
	var today = new Date();
	if (long == null) {
		long = 0;
	}
	var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24 * long;

	var yesterday = new Date();
	yesterday.setTime(yesterday_milliseconds);

	var strYear = yesterday.getFullYear();

	var strDay = yesterday.getDate();
	var strMonth = yesterday.getMonth() + 1;
	var strHours = yesterday.getHours();
	var strMinutes = yesterday.getMinutes();
	var strSeconds = yesterday.getSeconds();

	if (strMonth < 10) {
		strMonth = "0" + strMonth;
	}
	if (strDay < 10) {
		strDay = "0" + strDay;
	}
	if (strHours < 10) {
		strHours = "0" + strHours;
	}
	if (strMinutes < 10) {
		strMinutes = "0" + strMinutes;
	}
	if (strSeconds < 10) {
		strSeconds = "0" + strSeconds;
	}
	var strYesterday = strYear + "-" + strMonth + "-" + strDay + " " + strHours
			+ ":" + strMinutes + ":" + strSeconds;
	return strYesterday;
}
xh.user = function() {
	$.ajax({
		url : '/RTU/user/loginuser.action',
		data : {},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			/*
			 * console.log("username:"+response.items[0].username);
			 * console.log("email:"+response.items[0].password);
			 */

			$("#login_username").val(response.items[0].username)
			$("#login_email").val(response.items[0].email)
			$("#login_tel").val(response.items[0].tel)
		},
		failure : function(response) {
		}
	});
}
xh.saveUser = function() {
	$.ajax({
		url : '/RTU/user/updatemyself.action',
		data : {
			username : $("#login_username").val(),
			password : $("#login_password").val(),
			email : $("#login_email").val(),
			tel : $("#login_tel").val()
		},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(data) {
			if (data.success) {

				$('#update_member').modal('hide');
				toastr.success(data.message, '提示');

			} else {
				swal({
					title : "提示",
					text : data.message,
					type : "error"
				});
			}
		},
		failure : function(response) {
		}
	});
}
xh.LoginOut = function() {
	$.ajax({
		url : '/RTU/user/loginout.action',
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			window.location.href = "/RTU/view/login.html";
		},
		failure : function(response) {
		}
	});
}
xh.loginUserInfo = function() {
	$.ajax({
		url : '/RTU/user/loginuser.action',
		data : {},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			$("#login-user").text(response.items[0].username)
		},
		failure : function(response) {
		}
	});
}
xh.loginUserPower = function() {
	var data = null;
	$.ajax({
		url : '/RTU/user/userpower.action',
		data : {},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			var power = response.items[0];
			data = power;
		},
		failure : function(response) {
		}
	});
	return data;
}
xh.fullScreen = function() {
	var el = document.documentElement, rfs = el.requestFullScreen
			|| el.webkitRequestFullScreen || el.mozRequestFullScreen
			|| el.msRequestFullScreen, wscript;

	if (typeof rfs != "undefined" && rfs) {
		rfs.call(el);
		return;
	}

	if (typeof window.ActiveXObject != "undefined") {
		wscript = new ActiveXObject("WScript.Shell");
		if (wscript) {
			wscript.SendKeys("{F11}");
		}
	}
}

xh.exitFullScreen = function() {
	var el = document, cfs = el.cancelFullScreen || el.webkitCancelFullScreen
			|| el.mozCancelFullScreen || el.exitFullScreen, wscript;

	if (typeof cfs != "undefined" && cfs) {
		cfs.call(el);
		return;
	}

	if (typeof window.ActiveXObject != "undefined") {
		wscript = new ActiveXObject("WScript.Shell");
		if (wscript != null) {
			wscript.SendKeys("{F11}");
		}
	}
}
xh.dwr = function() {
	TcpDwr.centerStatus(1)
}
function statusUtil(status) {
	console.log("dwr");
	if (status == 1) {
		$("#centerStatus").html(
				"<d style='background:green;color:#ffffff'>已经连接</d>")
	} else {
		$("#centerStatus").html(
				"<d style='background:red;color:#ffffff'>正在连接</d>");
	}
}
