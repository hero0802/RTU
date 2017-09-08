var app = angular.module('app', [ 'ngRoute' ])
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		controller : 'alarm',
		templateUrl : 'index.html'
	}).when('/test2', {
		controller : 'conb',
		templateUrl : 'test2.html'
	}).otherwise({
		redirectTo : '/'
	});
} ]);