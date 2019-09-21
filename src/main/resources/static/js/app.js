var app = angular.module('app', ['ngRoute', 'ngResource']);
app.config(function ($routeProvider) {
    $routeProvider
        .when('/users', {
            templateUrl: '/views/users.html',
            controller: 'usersController'
        })
        .when('/roles', {
            templateUrl: '/views/roles.html',
            controller: 'rolesController'
        })
        .when('/login', {
            templateUrl: '/views/login.html',
            controller: 'login'
        })
        .when('/lead', {
            templateUrl: '/views/user_dashboard.html',
            controller: 'userDashboard'
        })
        .otherwise(
            {redirectTo: '/'}
        );
});

