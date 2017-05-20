/**
 * Created by vladimir_antin on 11.5.17..
 */
var nav = angular.module('nav', ['ngMaterial','ngRoute']);
nav.controller('nav', function nav ($scope, $mdSidenav,$http) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    if(!localStorage.getItem("jwt_token")){
        window.location.replace("login");
    }else{
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-type":"application/json",
                "Authorization":$scope.token
            }
        }).then(function done(response) {
        },function error(response) {
            if (response.status >= 400) {
                localStorage.removeItem("jwt_token");
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
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.nav_items = response.data;
        }
    },function error(response) {
        if (response.status >= 400) {
            localStorage.removeItem("jwt_token");
            window.location.replace("login");
        }
    });
});

nav.config(function ($routeProvider) {
    $routeProvider.
    when('/', {
        templateUrl: 'views/items.html',
        controller: 'items'
    }).
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
    }).
    when('/users/:userId', {
        templateUrl: 'views/users/profile.html',
        controller: 'profile'
    }).
    when('/items', {
        templateUrl: 'views/items.html',
        controller: 'items'
    }).
    when('/items/add', {
        templateUrl: 'views/items/add_item.html',
        controller: 'add_item'
    }).
    when('/items/:itemId', {
        templateUrl: 'views/items/item.html',
        controller: 'item'
    }).
    when('/auctions', {
        templateUrl: 'views/auctions.html',
        controller: 'auctions'
    }).
    otherwise({
        redirectTo: '/'
    });
});

nav.controller('logout', function ($scope) {
    localStorage.removeItem("jwt_token");
    window.location.replace("login")
});

nav.controller('users', Users);
nav.controller('add_user', Add_users);
nav.controller('profile', Profile);
nav.controller('items', Items);
nav.controller('add_item', Add_item);
nav.controller('item', Item);
nav.controller('auctions', Auctions);
