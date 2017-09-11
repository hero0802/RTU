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

var provinceArray = [];
var changeProvince = '';
var changeCity = '';

/* 加载站点呢数据 */
xh.loadData = function() {
	//xh.maskShow();
	xh.tableCheck();
	var app = angular.module("app", []);
	var start = 0;
	
	app.controller("site", function($scope, $http) {
		var data=xh.loginUserPower();
		
		
		$scope.add=data==null?0:data.m_addSite;
		$scope.dele=data==null?0:data.m_deleteSite;
		$scope.update_btn=data==null?0:data.m_updateSite;
		$http.get("../zone/province").success(function(response){
			$scope.provincesData = response.items;
		});
		

		$http.get("../site/site_trade.action")
		.success(function(response) {
			$scope.in_data = response.items;
		});
		
		var num = $("#page-limit").val();
		$scope.count = "10";
		$scope.menuTag = "site";
//		$scope.provinces = provinceArray;
		$scope.system_isActive=true;
		$scope.site_isActive=true;
	
		$http.get("../site/data.action?start=" + start + "&limit=" + num)
				.success(function(response) {
					$scope.data = response.items;
					padding(parseInt(response.total), num);
				});
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
		//刷新spd
		$scope.refresh_spd=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			$http.get(
					"../site/site_spd_data.action?siteId=" +$scope.siteIdVal).success(
					function(response) {
						$scope.spd_data = response.items;
						if(response.items.length<1){
							$scope.spd_isNULL = 1;
						}else{
							$scope.spd_isNULL = 0;
						}
					});
		};

		//刷新雷电流I
		$scope.refresh_i=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			$http.get(
					"../site/site_li_data.action?siteId=" +$scope.siteIdVal).success(
					function(response) {
						$scope.i_data = response.items;
						if(response.items.length<1){
							$scope.i_isNULL = 1;
						}else{
							$scope.i_isNULL = 0;
						}
					});
		};
		//刷新地阻检测仪数据
		$scope.refresh_r=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			$http.get(
					"../site/site_lr_data.action?siteId=" +$scope.siteIdVal).success(
					function(response) {
						$scope.r_data = response.items;
						if(response.items.length<1){
							$scope.r_isNULL = 1;
						}else{
							$scope.r_isNULL = 0;
						}

					});
		};
		$scope.flagChange=function(){
			var flag=$("#rForm").find("select[name='flag']").val();
			 if(flag==0){
				 $(".r").removeClass("r-show");
			 }else{
				 $(".r").addClass("r-show")
			 }
		}

		// 删除用户
		$scope.del = function(id) {
			swal({
				title : "提示",
				text : "确定要删除该站点吗？",
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
						url : '../site/del.action',
						type : 'post',
						dataType : "json",
						data : {
							deleteids : id
						},
						async : false,
						success : function(data) {
							if (data.success) {
								toastr.success("删除站点成功", '提示');
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
				url : '../site/del.action',
				type : 'post',
				dataType : "json",
				data : {
					deleteids : checkVal.join(",")
				},
				async : false,
				success : function(data) {
					if (data.success) {
						toastr.success("删除站点成功", '提示');
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
		
		
		
		
		$scope.In = function(){
			$("#add").modal('show');
		};
		$scope.Up = function(id){
			$scope.UpdateData=$scope.data[id];
			$scope.selInstr=$scope.UpdateData.trade;
		};
		
		/*
		 * 改变省
		 */
		
		
		
		
	/*	$scope.getCounty = function(countyArray,currCounty){
			
			document.all.mSelCounty.length = 0;
			
			for(var i=0;i<countyArray.length;i++){
				document.all.mSelCounty.options[i] = new Option(countyArray[i].name,countyArray[i].name);
				if(currCounty == countyArray[i].name){
					document.all.mSelCounty.options[i].setAttribute("selected","selected");
				}
			}
		};
		
		$scope.getCity=function(cityArray,currCity,currCounty)  
		{  
		
			document.all.mSelCity.length = 0;
			var index = 0;
			for (var j = 0; j < cityArray.length; j++) {
				document.all.mSelCity.options[j] = new Option(cityArray[j].name,cityArray[j].name);
					if(cityArray[j].name.match(currCity)){
						document.all.mSelCity.options[j].setAttribute("selected","selected");
						index = j;			
					}
				}
			$scope.getCounty(cityArray[index].children,currCounty);
		} ;*/
		
	/*	$scope.getProvince = function(currProvince,currCity,currCounty){
			var provinceArray = $scope.provinces;
			var index = provinceArray.length - 1;
				document.all.mSelProvince.length = 0;
				for (i = 0; i < provinceArray.length; i++) {
					document.all.mSelProvince.options[i] = new Option(
							provinceArray[i].name, provinceArray[i].name);
					if(currProvince == provinceArray[i].name){
						document.all.mSelProvince.options[i].setAttribute("selected","selected");
						index = i;
					}
				}
				$scope.getCity(provinceArray[index].children,currCity,currCounty);
		};*/
		// 修改站点
		$scope.update = function(id) {
			$scope.editData=$scope.data[id];
			$scope.editData.provinceId=$scope.editData.provinceId.toString();
			$scope.editData.cityId=$scope.editData.cityId.toString();
			$scope.editData.countyId=$scope.editData.countyId.toString();
	
			
				console.log($scope.data[id]);
				$scope.id = $scope.data[id].id;
				$scope.siteId = $scope.data[id].siteId;
				$scope.name = $scope.data[id].name;
				
				$scope.editData.trade = $scope.data[id].trade;
				$scope.editData.province = $scope.data[id].province.toString();
				$scope.editData.city = $scope.data[id].city.toString();
				$scope.editData.county = $scope.data[id].county.toString(); 
				$scope.editData.company = $scope.data[id].company;
				$scope.lon = $scope.data[id].lon;
				$scope.lat = $scope.data[id].lat;
				
				
				$scope.spd_type = $scope.data[id].SPD_type;
				$scope.spd_name = $scope.data[id].SPD_name;
				$scope.spd_model = $scope.data[id].SPD_model;
				$scope.spd_address = $scope.data[id].SPD_address;
				$scope.spd_level = $scope.data[id].SPD_level;

				$scope.r_name = $scope.data[id].R_name;
				$scope.r_model = $scope.data[id].R_model;
				$scope.r_modelName = $scope.data[id].R_modelName;
				$scope.r_type = $scope.data[id].R_type;
				$scope.r_address = $scope.data[id].R_address;
				$scope.r_maxValues= $scope.data[id].R_maxValues;

				$scope.i_name = $scope.data[id].I_name;
				$scope.i_model = $scope.data[id].I_model;
				$scope.i_address = $scope.data[id].I_address;
				$scope.i_address = $scope.data[id].I_address;
				$scope.i_maxValues = $scope.data[id].I_maxValues;
				$scope.i_maxNum = $scope.data[id].I_maxNum;
				$http.get("../zone/city?parentId="+$scope.editData.provinceId).success(function(response){
					$scope.cityData = response.items;
				});
				$http.get("../zone/district?parentId="+$scope.editData.cityId).success(function(response){
					$scope.countyData = response.items;
				});
				$http.get("../site/company?city="+$scope.editData.countyId).success(function(response){
					$scope.companyData=response.items;
				});
				
				
				/*changeProvince = $scope.province;*/
				
				/*$scope.getProvince($scope.province,$scope.city,$scope.county);*/
		};

		$scope.getCity=function(){
			var provinceId=$("#updateSiteForm").find("select[name='province']").val();
				$http.get("../zone/city?parentId="+provinceId).success(function(response){
				$scope.cityData = response.items;
				if(response.totals>0){
					$scope.editData.cityId=$scope.cityData[0].ID.toString();			
				}else{
					$scope.cityId="0";
				}
				$scope.getCounty();
			});
		};
		$scope.getCounty=function(){
				$http.get("../zone/district?parentId="+$scope.editData.cityId).success(function(response){
				$scope.countyData = response.items;
				if(response.totals>0){				
					$scope.editData.countyId=$scope.countyData[0].ID.toString();				
				}else{
					$scope.countyId="0";
				}
				//$scope.getCompany();
			});
		};
		$scope.getCompany=function(){
			$http.get("../site/company?city="+$scope.editData.countyId).success(function(response){
				$scope.companyData = response.items;
				if(response.total>0){				
					$scope.editData.company=$scope.companyData[0].company;
				}else{
					$scope.company="全部";
				}
			});
		};
		$scope.showMd44Modal=function(){
		  
		}
		
		$scope.save = function() {

			xh.update($scope.id);
		};
		//获取站点下的spd检测仪
		$scope.spd=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			if (checkVal.length !=1) {
				swal({
					title : "提示",
					text : "请选择一个站点",
					type : "error"
				});
				return;
			}
			$scope.add_btn=true;
			$scope.spdAStr="添加spd";
			$("#spd").modal('show');
			
			/*$("#spdFormAdd").*/
			
			$http.get(
					"../site/site_spd_data.action?siteId=" +checkVal[0]).success(
					function(response) {
						$scope.spd_data = response.items;
						$scope.siteIdVal=checkVal[0];
//						$scope.md44_deviceId=
						if(response.items.length<1){
							$scope.spd_isNULL = 1;
						}else{
							$scope.spd_isNULL = 0;
						}
					});
		};

		//获取站点下的雷电流检测仪
		$scope.I=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			if (checkVal.length !=1) {
				swal({
					title : "提示",
					text : "请选择一个站点",
					type : "error"
				});
				return;
			}
			$scope.add_btn=true;
			$("#site_i").modal('show');
			
			/*$("#spdFormAdd").*/
			
			$http.get(
					"../site/site_li_data.action?siteId=" +checkVal[0]).success(
					function(response) {
						$scope.i_data = response.items;
						$scope.siteIdVal=checkVal[0];
						if(response.items.length<1){
							$scope.i_isNULL = 1;
						}else{
							$scope.i_isNULL = 0;
						}
					});
		};
		
	
		
		$scope.Md = function(type){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			if (checkVal.length !=1) {
				swal({
					title : "提示",
					text : "请选择一个站点",
					type : "error"
				});
				return;
			}
			$scope.add_btn=true;
			$scope.siteIdVal=checkVal[0];
			if(type == 0)
				$("#md44").modal('show');
			$http.get(
					"../site/site_md44_data.action?siteId="+checkVal[0]).success(
					function(response){
						$scope.m_isNULL = response.totals;
						$scope.m_data = response.items;
						$scope.siteIdVal=checkVal[0];
						$scope.md44_deviceId = $scope.m_data[0].deviceId;
						$scope.md44id = $scope.m_data[0].id;					
					})
		}
		
		//获取站点下的地阻检测仪
		$scope.R=function(){
			var checkVal = [];
			$("[name='tb-check']:checkbox").each(function() {
				if ($(this).is(':checked')) {
					checkVal.push($(this).attr("siteIdVal"));
				}
			});
			if (checkVal.length !=1) {
				swal({
					title : "提示",
					text : "请选择一个站点",
					type : "error"
				});
				return;
			}
			$scope.add_btn=true;
			$("#site_r").modal('show');
			
			/*$("#spdFormAdd").*/
			
			$http.get(
					"../site/site_lr_data.action?siteId=" +checkVal[0]).success(
					function(response) {
						console.log(response);
						$scope.r_data = response.items;
						$scope.siteIdVal=checkVal[0];
						if(response.items.length<1){
							$scope.r_isNULL = 1;
						}else{
							$scope.r_isNULL = 0;
						}
					});
		};

	
		
		
		
		
		//添加站点下的spd
		$scope.showAddSpdWindow=function(){
			//$("#spdFormAdd")[0].reset();
			
			//$('#spd-add-form').bootstrapValidator('resetForm', true);

			$scope.spd_comType=1;
			$scope.add_btn=true;
		};
		
		//添加站点下的雷电流检测仪
		$scope.showAddIWindow=function(){
			//$("#i-window")[0].reset();
			//$('#i-window').bootstrapValidator('resetForm', true);
			$scope.i_comType=3;
			$scope.add_btn=true;
		}
		//添加站点下的地阻检测仪
		$scope.showAddRWindow=function(){
			//$("#r-window")[0].reset();
			//$('#r-window').bootstrapValidator('resetForm', true);
			$scope.r_comType=2;
			$scope.add_btn=true;
		}
		//修改md44
		$scope.showUpdateMd44Window=function(id){
			$scope.md44_deviceId=$scope.m_data[id].deviceId;
			$scope.md44_name = $scope.m_data[id].name;
			$scope.md44_type = $scope.m_data[id].type;
			$scope.md44_address = $scope.m_data[id].address;
			$scope.add_btn=true;
			$("#md44-update-window").modal('show');
		};
		//删除md44
		$scope.del_md44=function(id){
			$.ajax({
				url : '../site/del_md44.action',
				type : 'post',
				dataType : "json",
				async : false,
				data :{
					siteId:$scope.m_data[id].siteId,
					md44_deviceId:$scope.m_data[id].deviceId
				},
				success : function(data) {
					if (data.success) {

						//$('#add').modal('hide');
						toastr.success(data.message, '提示');
				        $scope.Md();

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
		
		//修改站点下的spd
		$scope.showUpdateSpdWindow=function(id){
			/*$('#spd-update-form').bootstrapValidator('resetForm', true);*/
			$("#spd-update-window").modal('show');
			$scope.spd_deviceId=$scope.spd_data[id].deviceId;
			$scope.spd_type = $scope.spd_data [id].type;
			$scope.spd_name = $scope.spd_data [id].name;
			$scope.spd_model = $scope.spd_data [id].model;
			$scope.spd_address = $scope.spd_data [id].address;
			$scope.spd_level = $scope.spd_data [id].level;
			
			
			
			$scope.add_btn=false;
			
			
		};

		//修改站点下的雷电流在线检测仪
		$scope.showUpdateIWindow=function(id){
			//$('#i-window').data('bootstrapValidator').resetForm(true);
			$scope.i_deviceId=$scope.i_data[id].deviceId;
			$scope.i_name = $scope.i_data [id].name;
			$scope.i_model = $scope.i_data [id].model;
			$scope.i_address = $scope.i_data [id].address;
			$scope.i_maxValues = $scope.i_data [id].maxValues;
			$scope.i_maxNum = $scope.i_data [id].maxNum;
			
			$scope.add_btn=false;
			//$('#i-window').bootstrapValidator('resetForm', true);
			
		};
		//修改站点下的地阻在线检测仪
		$scope.showUpdateRWindow=function(id){
			$scope.rData=$scope.r_data[id];
			$scope.rData.flag=$scope.rData.flag.toString();
			
			$("#rForm").find("select[name='flag']").val($scope.rData.flag);
			var flag=$scope.rData.flag;
			 if(flag==0){
				 $(".r").removeClass("r-show");
			 }else{
				 $(".r").addClass("r-show");
			 }
			$scope.add_btn=false;
		};
		//删除站点下的spd
		$scope.del_spd=function(id){
			$.ajax({
				url : '../site/del_spd.action',
				type : 'post',
				dataType : "json",
				async : false,
				data :{
					spd_deviceId:$scope.spd_data[id].deviceId,
					siteId:$scope.spd_data[id].siteId,
					spd_model:$scope.spd_data[id].model
				},
				success : function(data) {
					if (data.success) {

						//$('#add').modal('hide');
						toastr.success(data.message, '提示');
				        $scope.refresh_spd();

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

		//删除站点下的雷电流在线检测仪
		$scope.del_i=function(id){
			$.ajax({
				url : '../site/del_li.action',
				type : 'post',
				dataType : "json",
				async : false,
				data :{
					siteId:$scope.i_data[id].siteId,
					i_deviceId:$scope.i_data[id].deviceId
				},
				success : function(data) {
					if (data.success) {						
						toastr.success(data.message, '提示');
				        $scope.refresh_i();

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
		//删除站点下的地阻在线检测仪
		$scope.del_r=function(id){
			$.ajax({
				url : '../site/del_lr.action',
				type : 'post',
				dataType : "json",
				async : false,
				data :{
					siteId:$scope.r_data[id].siteId,
					r_deviceId:$scope.r_data[id].deviceId,
					r_md44id:$scope.r_data[id].md44id,
					r_model:$scope.r_data[id].model
				},
				success : function(data) {
					if (data.success) {						
						toastr.success(data.message, '提示');
				        $scope.refresh_r();

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
		
		$scope.showSelect = function(){
			alert(4231);
			$("#md44-window").modal('show');
		}
		
		// 分页
		var padding = function(totals, one_count) {
			if (totals == 0) {
				$scope.isNULL = 1;
				return;
			} else {
				$scope.isNULL = 0;
			}
			var pageCount = Math.ceil(totals / one_count);
			$(".page-paging").html('<ul class="pagination" style="margin:0"></ul>');
			$('.pagination').twbsPagination(
					{
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
									"../site/data.action?start=" + start
											+ "&limit=" + showTotal).success(
									function(response) {
										$scope.data = response.items;
										xh.maskHide();
									});
						}
					});

			};

		});
//	});
};
xh.flagChange=function(){
	var appElement = document.querySelector('[ng-controller=site]');
	
	var $scope = angular.element(appElement).scope();
	$scope.flagChange();
}

xh.ChangeCity = function(){
	
	var index = document.all.mSelCity.selectedIndex;
	
	for (var i = 0; i < provinceArray.length; i++) {
		if(changeProvince == provinceArray[i].name){
			for(var j=0;j<provinceArray[i].children.length;j++){
				if(index == j)
					xh.newCounty(provinceArray[i].children[j].children);
			}
		}
	}
};
xh.ChangeCounty = function(){
	
	var index = document.all.mSelCounty.selectedIndex;
	
	for (var i = 0; i < CityArray.length; i++) {
		if(changeCity == CityArray[i].name){
			for(var j=0;j<CityArray[i].children.length;j++){
				if(index == j)
					xh.newCounty(provinceArray[i].children[j].children);
			}
		}
	}
};
xh.newCounty = function(county){

	document.all.mSelCounty.length = 0;
	
	for(var i=0;i<county.length;i++){
		document.all.mSelCounty.options[i] = new Option(county[i].name,county[i].name);
	}
}

xh.newCity = function(city){
	
	document.all.mSelCity.length = 0;
	
	for(var i=0;i<city.length;i++){
		document.all.mSelCity.options[i] = new Option(city[i].name,city[i].name);
	}
	changeCity = city[0].name;
	xh.newCounty(city[0].children);
}

xh.ChangeArea = function(){
	
	var index = document.all.mSelProvince.selectedIndex;
	
	for (var i = 0; i < provinceArray.length; i++) {
		if (i == index) {
			changeProvince = provinceArray[i].name;
			xh.newCity(provinceArray[i].children);
		}
	}
};

xh.newBtn = function(){
	swal({
		title : "提示",
		text : "未配置相关设备",
		type : "error"
	});
};

xh.add_md44 = function(){
	$.ajax({
		url : '../site/add_md44.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#md44-add-form").serializeArray(),
		success : function(data) {
			if (data.success) {
				$("#md44-add-window").modal('hide');
				$('#add').modal('hide');
				toastr.success(data.message, '提示');
		        xh.refreshMd44();

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
//修改md
xh.update_md44=function(){
	$.ajax({
		url : '../site/update_md44.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#md44-update-form").serializeArray(),
		success : function(data) {
			if (data.success) {
				$('#md44-update-window').modal('hide');
				toastr.success(data.message, '提示');
		       xh.refreshMd44();

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
//添加spd检测仪
xh.add_spd = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	
	var $scope = angular.element(appElement).scope();
	$.ajax({
		url : '../site/add_spd.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#spd-add-form").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#spd-add-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_spd();
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

//添加雷电流检测仪
xh.add_i = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	$.ajax({
		url : '../site/add_li.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#iForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#i-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_i();
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
//添加地阻检测仪
xh.add_r= function() {
//	alert(JSON.stringify($("#rForm").serializeArray()));
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	$.ajax({
		url : '../site/add_lr.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#rForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#r-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_r();
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
//修改spd
xh.update_spd = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	$.ajax({
		url : '../site/update_spd.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#spd-update-form").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#spd-update-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_spd();
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

//修改雷电流检测仪
xh.update_i = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	$.ajax({
		url : '../site/update_li.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#iForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#i-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_i();
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
//修改地阻检测仪
xh.update_r= function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	
	$.ajax({
		url : '../site/update_lr.action',
		type : 'post',
		dataType : "json",
		async : false,
		data : $("#rForm").serializeArray(),
		success : function(data) {
			if (data.success) {

				$('#r-window').modal('hide');
				toastr.success(data.message, '提示');
				$scope.refresh_r();
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
// 修改站点
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
// 刷新数据
xh.refresh = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.refresh();

};
//刷新MD44模块
xh.refreshMd44 = function() {
	var appElement = document.querySelector('[ng-controller=site]');
	var $scope = angular.element(appElement).scope();
	// 调用$scope中的方法
	$scope.Md();

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