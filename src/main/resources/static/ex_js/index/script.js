/**
 * Created by vladimir_antin on 11.5.17..
 */
var nav = angular.module('nav', ['ngMaterial','ngRoute']);
nav.factory("data",function () {
   return {token:""}
});
nav.controller('nav', function nav ($scope,data, $mdSidenav,$http) {
    data.token = "jwt "+sessionStorage.getItem("jwt_token");
    if(!sessionStorage.getItem("jwt_token")){
        window.location.replace("login");
    }else{
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-type":"application/json",
                "Authorization":data.token
            }
        }).then(function done(response) {
        },function error(response) {
            if (response.status >= 400) {
                sessionStorage.removeItem("jwt_token");
                window.location.replace("login");
            }
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
            "Authorization":data.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.nav_items = response.data;
        }
    },function error(response) {
        if (response.status >= 400) {
            sessionStorage.removeItem("jwt_token");
            window.location.replace("login");
        }
    });
});

nav.config(function ($routeProvider) {
    $routeProvider.
    when('/logout', {
        template: '',
        controller: 'logout'
    }).
    when('/users', {
        templateUrl: 'views/users.html',
        controller: 'users'
    }).
    when('/users/add', {
        templateUrl: 'views/users/add_user.html',
        controller: 'add_user'
    })
    // otherwise({
    //     redirectTo: '/home'
    // });
});

nav.controller('logout', function ($scope) {
    sessionStorage.removeItem("jwt_token");
    window.location.replace("login")
});

nav.controller('users', Users);
nav.controller('add_user', Add_users);