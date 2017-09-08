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
/* 加载用户数据 */
xh.loadUser = function() {
	xh.maskShow();

	xh.tableCheck();

	var app = angular.module("app", []);
	var start = 0;
	app.controller("member", function($scope, $http) {
		var num = $("#page-limit").val();
		var data = xh.loginUserPower();
		$scope.system_isActive=true;
		$scope.user_isActive=true;
		$scope.count = "10";
		$scope.menuTag = "site";
		$scope.add = data == null ? 0 : data.m_addUser;
		$scope.del = data == null ? 0 : data.m_deleteUser;
		$scope.update = data == null ? 0 : data.m_updateUser;
		$scope.lock = data == null ? 0 : data.m_lockUser;
		$scope.userPower_btn = data == null ? 0 : data.m_editPower;
		$scope.provinces = provinceArray;
		$http.get("../user/data.action?start=" + start + "&limit=" + num)
				.success(function(response) {
					$scope.data = response.items;
					padding(parseInt(response.total), num);
				});
		$http.get("../zone/province").success(function(response){
			$scope.provincesData = response.items;
			var arr={};
			
			$scope.provincesData.push()
		});
		// 刷新用户数据
		$scope.refresh = function() {
			xh.maskShow();
			$http.get(
					"../user/data.action?start=" + start + "&limit="
							+ $("#page-limit").val()).success(
					function(response) {
						$scope.data = response.items;
						padding(parseInt(response.total), $("#page-limit")
								.val());
						xh.maskHide();
					});
		};
		// 删除用户
		$scope.delUser = function(id) {
			swal({
				title : "提示",
				text : "确定要删除该用户吗？",
				type : "info",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消"
			/*
			 * closeOnConfirm : false, closeOnCancel : false
			 */
			}, function(isConfirm) {
				if (isConfirm) {
					$.ajax({
						url : '../user/del.action',
						type : 'post',
						dataType : "json",
						data : {
							deleteids : id
						},
						async : false,
						success : function(data) {
							if (data.success) {
								toastr.success("删除用户成功", '提示');
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
		$scope.delManyUser = function() {
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
				url : '../user/del.action',
				type : 'post',
				dataType : "json",
				data : {
					deleteids : checkVal.join(",")
				},
				async : false,
				success : function(data) {
					if (data.success) {
						toastr.success("删除用户成功", '提示');
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
		/*$scope.getCity = function(currProvince) {
			// 当前 所选择 的 省
			var i;
			// 清空 城市 下拉选单
			document.all.selCity.length = 0;
			console.log("city=="+currProvince);
			for (i = 0; i < search_cityArray.length; i++) {
				// 得到 当前省 在 城市数组中的位置
				if (search_cityArray[i][0] == currProvince) {
					// 得到 当前省 所辖制的 地市
					var tmpcityArray = search_cityArray[i][1].split("|");
					$scope.citys = tmpcityArray;
					console.log(tmpcityArray)
					//$scope.citys=document.all.selCity.options[0].value;

					
					 * for(j=0;j<tmpcityArray.length;j++) { //填充 城市 下拉选单
					 * document.all.selCity.options[document.all.selCity.length] =
					 * new Option(tmpcityArray[j],tmpcityArray[j]); }
					 
				}
			}
		};*/
		$scope.getPower = function() {
			
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("value"));
				}
			});
			if (checkVal.length != 1) {
				swal({
					title : "提示",
					text : "请选择用户",
					type : "error"
				});
				return;
			}
			$('#edit_power').modal('show');
			$.ajax({
				url : '../user/alluserpower.action',
				type : 'post',
				dataType : "json",
				data : {
					id : checkVal[0]
				},
				async : false,
				success : function(data) {
					var power = data.power[0];
					$scope.zone = data.zone[0];

					$scope.addUser = power.m_addUser;
					$scope.updateUser = power.m_updateUser;
					$scope.delUser = power.m_deleteUser;
					$scope.lockUser = power.m_lockUser;
					$scope.editPower = power.m_editPower;

					$scope.addGroup = power.m_addGroup;
					$scope.updateGroup = power.m_updateGroup;
					$scope.delGroup = power.m_deleteGroup;

					$scope.addSite = power.m_addSite;
					$scope.updateSite = power.m_updateSite;
					$scope.delSite = power.m_deleteSite;
					$scope.delLog = power.m_deleteLog;
					$scope.userid=power.userid;
					
		
					$scope.zone.provinceId = $scope.zone.provinceId.toString();
					$scope.zone.cityId = $scope.zone.cityId.toString();
					$scope.zone.countyId = $scope.zone.countyId.toString();
					$scope.zone.company = $scope.zone.company;

					
					$http.get("../zone/city?parentId="+$scope.zone.provinceId).success(function(response){
						$scope.cityData = response.items;
					});
					$http.get("../zone/district?parentId="+$scope.zone.cityId).success(function(response){
						$scope.countyData = response.items;
					});
					$http.get("../site/company?city="+$scope.zone.countyId).success(function(response){
						$scope.companyData=response.items;
					});
					
					
					
					
					
					/*$scope.getCity($scope.province);*/
				},
				error : function() {
				}
			});
			
			

		};
		$scope.getCity=function(){
			var provinceId=$("select[name='province']").val();
			
			$http.get("../zone/city?parentId="+provinceId).success(function(response){
				$scope.cityData = response.items;
				if(response.totals>0){
				$scope.cityId=$scope.cityData[0].ID.toString();			
				}else{
					$scope.zone.cityId="0";
				}
				$scope.getCounty();
			});
		};
		$scope.getCounty=function(){
				$http.get("../zone/district?parentId="+$scope.zone.cityId).success(function(response){
				$scope.countyData = response.items;
				if(response.totals>0){				
					$scope.zone.countyId=$scope.countyData[0].ID.toString();				
				}else{
					$scope.zone.countyId="0";
				}
				$scope.getCompany();
			});
		};
		$scope.getCompany=function(){
			$http.get("../site/company?city="+$scope.zone.countyId).success(function(response){
				$scope.companyData = response.items;
				if(response.total>0){				
					$scope.zone.company=$scope.companyData[0].company;
				}else{
					$scope.zone.company="全部";
				}
			});
		};
		// 修改用户
		$scope.updateUser = function(id) {
			console.log($scope.data[id]);
			$http.get("../user/data_group.action").success(function(response) {
				$scope.id = $scope.data[id].id;
				console.log("groupid==" + $scope.data[id].groupid);
				$scope.group = response.items;
				$scope.username = $scope.data[id].username;
				$scope.email = $scope.data[id].email;
				$scope.tel = $scope.data[id].tel;
				$scope.groupid = $scope.data[id].groupid.toString();
				$scope.show = 0;
			});

		};
		$scope.save = function() {

			xh.update($scope.id);
		};
		//保存权限
		$scope.savePower=function(){
			$.ajax({
				url : '../user/savepower.action',
				type : 'post',
				dataType : "json",
				async : false,
				data : {
					id : $scope.userid,
					addUser : $("#addUser").is(':checked')?1:0,
					updateUser : $("#updateUser").is(':checked')?1:0,
					delUser : $("#delUser").is(':checked')?1:0,
					lockUser:$("#lockUser").is(':checked')?1:0,
				    editPower:$("#editPower").is(':checked')?1:0,
					addGroup : $("#addGroup").is(':checked')?1:0,
					updateGroup : $("#updateGroup").is(':checked')?1:0,
					delGroup : $("#delGroup").is(':checked')?1:0,
					addSite : $("#addSite").is(':checked')?1:0,
					updateSite : $("#updateSite").is(':checked')?1:0,
					delSite : $("#delSite").is(':checked')?1:0,
					delLog : $("#delLog").is(':checked')?1:0,
					province : $("#mSelProvince").val(),
					city : $("#mSelCity").val(),
					county:$("#mSelCounty").val(),
					company : $("#mSelCompany").val()
				},
				success : function(data) {
					if (data.success) {
						toastr.success(data.message, '提示');
						$('#edit_power').modal('hide');
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
		//
		$scope.getGroupId = function() {
			$http.get("../user/data_group.action").success(function(response) {
				$scope.group = response.items;
				$scope.groupid = $scope.group[0].id.toString();
				console.log($scope.group);
			});
		};
		// 给用户加锁
		$scope.lockUser = function(id) {
			$.ajax({
				url : '../user/lock.action',
				type : 'post',
				dataType : "json",
				async : false,
				data : {
					id : id
				},
				success : function(data) {
					if (data.success) {
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
		// 给用户解锁
		$scope.unLockUser = function(id) {
			$.ajax({
				url : '../user/unlock.action',
				type : 'post',
				dataType : "json",
				async : false,
				data : {
					id : id
				},
				success : function(data) {
					if (data.success) {
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
		// 分页
		var padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.isNULL = 1;
				return;
			} else {
				$scope.isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$(".page-paging").html('<ul class="pagination"></ul>');
			$('.pagination').twbsPagination(
					{
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
									"../user/data.action?start=" + start
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
// 获取用户组
xh.group = function() {
	var appElement = document.querySelector('[ng-controller=member]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.getGroupId();
};
// 添加用户
xh.addUser = function() {
	$.ajax({
		url : '../user/add.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#addUserForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#add_user').modal('hide');
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
xh.update = function(id) {
	$.ajax({
		url : '../user/update.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : {
			id : id,
			username: $("#update_user").find("input[name='username']").val(),
			password : $("#update_user").find("input[name='password']").val(),
			email : $("#update_user").find("input[name='email']").val(),
			tel : $("#update_user").find("input[name='tel']").val(),
			groupid : $("#update_user").find("select[name='groupid']").val()
		},
		success : function(data) {
			if (data.success) {

				$('#update_user').modal('hide');
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

	
xh.Province = function() {
	$.getJSON("../zone/province", function(data) {
		provinceArray = data.items;
		document.all.selProvince.length = 0;
		document.all.selProvince.options[0] = new Option("全部","0");
		for (var i = 0; i < provinceArray.length; i++) {
			document.all.selProvince.options[i+1] = new Option(
					provinceArray[i].Name, provinceArray[i].ID);
		}
	});
	$("#selProvince").val(0);
	var value=$("#selProvince").val();
	xh.getCity(0)
};

xh.getCity = function(currProvince) {
	
	$.getJSON("../zone/city?parentId="+currProvince, function(dataArr) {
		var data = dataArr.items;
		document.all.selCity.length = 0;
		document.all.selCity.options[0] = new Option("全部","0");
		for (var i = 0; i < data.length; i++) {
			document.all.selCity.options[i+1] = new Option(
                data[i].Name, data[i].ID);
		}
		
	});
	/*$("#selCity").val(0);
	var value=$("#selCity").val();
	xh.getCounty(value)*/
};

xh.getCounty = function(currCity) {
	if(currCity==null){currCity=0;}
	
	$.getJSON("../zone/district?parentId="+currCity, function(dataArr) {
		var data = dataArr.items;
		document.all.selCounty.length = 0;
		document.all.selCounty.options[0] = new Option("全部","0");
		for (var i = 0; i < data.length; i++) {
			document.all.selCounty.options[i+1] = new Option(
                data[i].Name, data[i].ID);
		}
		
	});
	/*$("#selCounty").val(0);
	var value=$("#selCounty").val();
	xh.getCompany(value)*/
};





xh.getCompany = function(curCity) {
	document.all.selCompany.length = 0;
	document.all.selCompany.options[0] = new Option("全部", "全部")
	$.ajax({
		url : '../site/company.action',
		data : {
			city : curCity
		},
		type : 'post',
		dataType : "json",
		async : false,
		success : function(response) {
			var companys = response.items;
			for (var i = 0; i < companys.length; i++) {
				// 填充公司下拉菜单
				document.all.selCompany.options[i + 1] = new Option(
						companys[i].company, companys[i].company)
			}
		},
		failure : function(response) {
		}
	});
};
// 刷新数据
xh.refresh = function() {
	var appElement = document.querySelector('[ng-controller=member]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();

};
// 分页
xh.padding = function(totals, count) {
	var appElement = document.querySelector('[ng-controller=member]');
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
xh.register = function() {
	$.ajax({
		url : '../user/register.action',
		type : 'post',
		dataType : "json",
		data : $("#registerForm").serializeArray(),
		async : false,
		success : function(data) {
			if (data.success) {
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
/* 登录系统 */
xh.login = function() {
	$("#login-btn").val("登录中。。。");
	$("#login-btn").toggleClass("disabled");
	$.ajax({
		url : '../user/login.action',
		type : 'post',
		dataType : "json",
		data : $("#loginForm").serializeArray(),
		async : false,
		success : function(data) {
			if (data.success) {
				window.location.href = "../index.html";
			} else {
				swal({
					title : "提示",
					text : data.message,
					type : "error"
				});
				$("#login-btn").val("登录");
				$("#login-btn").toggleClass("disabled");
			}
		},
		error : function() {
			$("#login-btn").val("登录");
			$("#login-btn").toggleClass("disabled");

		}
	});
}