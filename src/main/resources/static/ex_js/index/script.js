/**
 * Created by vladimir_antin on 11.5.17..
 */
var nav = angular.module('nav', ['ngMaterial','ngRoute']);
nav.controller('nav', function nav ($scope, $mdSidenav,$http) {
    if(!sessionStorage.getItem("jwt_token")){
        window.location.replace("login");
    }else{
        var token = "jwt "+sessionStorage.getItem("jwt_token");
        console.log(token);
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-typ":"application/json",
                "Authorization":token
            }
        }).then(function success(response) {
            console.log(JSON.stringify(response));
        }, function error(error) {
            console.log(JSON.stringify(error));
            sessionStorage.removeItem("jwt_token");
            window.location.replace("login");
        });
    }
    $scope.openLeftMenu = function() {
        $mdSidenav('left').toggle();
    };
    $scope.navItems = [
        {href:"#/home", name:"Home", icon:"home"},
        {href:"#/users", name:"Users", icon:"group"},
        {href:"#/profile", name:"Profile", icon:"person"},
        {href:"#/logout", name:"Logout", icon:"input"}
    ]
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