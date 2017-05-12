/**
 * Created by vladimir_antin on 11.5.17..
 */
var nav = angular.module('nav', ['ngMaterial','ngRoute']);
nav.controller('nav', function nav ($scope, $mdSidenav,$http) {
    $scope.token = "jwt "+sessionStorage.getItem("jwt_token");
    if(!sessionStorage.getItem("jwt_token")){
        window.location.replace("login");
    }else{
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-type":"application/json",
                "Authorization":$scope.token
            }
        }).then(function success(response) {
        }, function error(error) {
            sessionStorage.removeItem("jwt_token");
            window.location.replace("login");
        });
    }
    $scope.openLeftMenu = function() {
        $mdSidenav('left').toggle();
    };

    $http({
        method: 'GET',
        url: '/api/nav_items',
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function success(response) {
        $scope.nav_items = response.data;
    }, function error(error) {
        sessionStorage.removeItem("jwt_token");
        window.location.replace("login");
    });
});

nav.config(function ($routeProvider) {
    $routeProvider.
    when('/logout', {
        template: '',
        controller: 'logout'
    })

    // otherwise({
    //     redirectTo: '/home'
    // });
});

nav.controller('logout', function ($scope) {
    sessionStorage.removeItem("jwt_token");
    window.location.replace("login")
});