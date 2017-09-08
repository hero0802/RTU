/**
 * 
 */
(function($){
	var defaults={
			module:'app',
			controller:'',
			limit:10,
			url:'',
			pageLimitClick:null
	};
	var app=angular.module(defaults.module,[]);
	$.fn.table=function(m){
		m=$.extend(defaults,m);		
		load("?limit="+m.limit);
		tableCheck();
		 if (m.pageLimitClick instanceof Function) {
             $(".page-limit").bind('change', m.pageLimitClick);
         }
		function load(preUrl){
			app.controller(m.controller, function($scope, $http) {
			    $http.get(m.url+preUrl)
			    .success(function (response){
			    	$scope.data = response.items;
			    	padding(parseInt(response.total));
			    });
			});
		}
		//分页
		function padding(items){
			var pageCount=Math.ceil(items/m.limit);
			
			
			$(".page-paging").html('<ul class="pagination"></ul>');
			$('.pagination').twbsPagination({
			       totalPages: pageCount,
			       visiblePages: 7,
			       version: '1.1',
			       onPageClick: function (event, page) {
			       var limit=(page)*m.limit;
			       if(limit>items){
			    	   limit=items;
			       }
				   preUrl="?start="+(page*m.limit)
				   load(preUrl);
				   $(".page-info").html('<span>显示 ('+(page-1)*m.limit+'-'+limit+')条,总计 ('+items+')条</span>');
			       }
			   });
		}
		function refresh(url){
			
		}
		//checkbox
		function tableCheck(){
			$(".table-check").bind("click", function () {
	            var checkVal=[];
	            if($(".table-check").is(':checked')){
					$("[name='tb-check']").attr("checked",true);//全选
					$("[name='tb-check']:checkbox").each(function(){
					    if($(this).is(':checked')){
					       checkVal.push($(this).attr("value"));
					    }  
					});
					//alert(checkVal.join(','))
				}else{
					$("[name='tb-check']").attr("checked",false);//反选
				}
	        });
			
		}
		
		
	};
	
})(jQuery)