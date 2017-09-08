if (!("xh" in window)) {
	window.xh = {};
};
require.config({
	paths : {
		echarts : '../resources/static/echarts'
	}
});
var chart;
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
	/* onCheck : onCheck */
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
	/* onCheck : onCheck */
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
		/* onCheck : onCheck */
		}
	};
var app = angular.module("app", []);
app.controller("IChart", function($scope, $http) {
	$("#select-btn").click(function(e) {
		$("#tree").show();
		e.stopPropagation(); // 阻止事件冒泡，否则事件会冒泡到下面的文档点击事件
	});
	$("#r-select-btn").click(function(e) {
		$("#rTree").show();
		e.stopPropagation(); // 阻止事件冒泡，否则事件会冒泡到下面的文档点击事件
	});
	$("#model-select-btn").click(function(e) {
		$("#mTree").show();
		e.stopPropagation(); // 阻止事件冒泡，否则事件会冒泡到下面的文档点击事件
	});
	xh.hideTree("tree");
	// xh.hideTree("rTree");
	
	$scope.endTime = xh.nowDateTime(0);
	$http.get("../site/menu.action").success(function(response) {
		var zNodes = response.items;
		var t = $("#tree");
		t = $.fn.zTree.init(t, setting, zNodes);
	});
});
xh.loadKLine = function() {
	if (chart != null) {
		chart.clear();
		chart.dispose();
	}
	require([ 'echarts', 'echarts/chart/line' ], function(ec) {
		chart = ec.init(document.getElementById('kLine'));
		chart.showLoading({
			text : '正在努力的读取数据中...'
		});
		var startTime = $("#clockS").val();
		var endTime = $("#clockE").val();
		var option = {
			title : {
				text : '地阻测试值统计图',
				textStyle : {
					color : '#fff'
				}
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					lineStyle : {
						color : '#48b',
						width : 2,
						type : 'solid'
					},
					crossStyle : {
						color : '#000',
						width : 1,
						type : 'dashed'
					},
					shadowStyle : {
						color : 'rgba(150,150,150,0.3)',
						width : 'auto',
						type : 'default'
					}
				}
			},
			legend : {
				data : [ '地阻测试值' ],
				textStyle : {
					color : '#fff'
				}
			},
			calculable : false,
			backgroundColor : 'green',
			dataZoom : {
				show : true,
				realtime : true,
				backgroundColor : '#fff',
				handlerColor : 'red',
				fillColor : 'yellow',
				start : 0,
				end : 100
			},
			xAxis : [ {
				type : 'category',// 类目型
				boundaryGap : false, // 两端空白,
				axisLabel : {
					// X轴刻度配置
					interval : 'auto', // 0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
					textStyle : {
						color : '#fff'
					}
				},

				data : [ 1, 2 ]
			} ],
			yAxis : [ {
				type : 'value',
				scale : false,
				scale : true,// 脱离0值比例，放大聚焦到最终_min，_max区间
				axisLabel : {
					formatter : '{value} Ω',
					textStyle : {
						color : '#fff'
					}
				},
				splitArea : {
					show : true
				// 分隔区域
				}
			} ],
			series : [ {
				name : '地阻测试值',
				type : 'line',// 直线类型
				itemStyle : {
					normal : {
						color : 'red'
					}
				},

				data : [ 0, 1 ]
			} ]
		};

		$.ajax({
			url : '../site/r_chart.action',
			data : {
				siteId : $("input[name='siteId']").val(),
				r_deviceId : $("input[name='deviceId']").val(),
				r_model:$("input[name='model']").val(),
				timeS : startTime,
				timeE : endTime
			},
			type : 'get',
			dataType : "json",
			async : false,
			success : function(response) {
				var data = response.chartData;
				var xAxisData = [], seriesData = [];
				for (var i = 0; i < data.length; i++) {
					xAxisData.push(data[i].date);
					seriesData.push(data[i].value);
				}
				option.xAxis[0].data = xAxisData;
				option.series[0].data = seriesData;
				chart.hideLoading();
				console.log(xAxisData);
				console.log(seriesData);
				chart.setOption(option);

			},
			failure : function(response) {
			}
		});

	});
};
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
function onTreeClick(event, treeId, treeNode) {
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
			/*console.log(zNodes);*/
			var t = $("#rTree");
			t = $.fn.zTree.init(t, setting_r, zNodes);
			$("#r-select-btn").removeAttr("disabled");// 将按钮可用

		},
		failure : function(response) {
		}
	});
}

function onMTreeClick(event, treeId, treeNode){
	$("input[name='model']").val(treeNode.model);
	/*xh.hideTree("tree");*/
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
				newNode.name = '地阻监测点'+zNodes[i].model;
				node.push(newNode);
			}
			/*console.log(zNodes);*/
			var t = $("#mTree");
			t = $.fn.zTree.init(t, setting_m, node);
			$("#model-select-btn").removeAttr("disabled");//将按钮可用
			
		},
		failure : function(response) {
		}
	});
	
}
