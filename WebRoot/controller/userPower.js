if (!("xh" in window)) {
	window.xh = {};
};
xh.load=function(){
	var app = angular.module("app", []);
	app.controller("power", function($scope, $http) {
		$scope.username="admin";
		
	});
};