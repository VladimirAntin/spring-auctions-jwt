/**
 * Created by vladimir_antin on 10.5.17..
 */
var app = angular.module('app', ['ngMaterial']);

var EMAIL_ERROR = "Your email must be between 6 and 30 characters long and look like an e-mail address.";
var PASSWORD_ERROR = "Your password be between 1 and 10 characters long.";

app.controller('login', LoginPost);

function LoginPost($scope,$http) {
    if(sessionStorage.getItem("jwt_token")){
        window.location.replace("/home")
    }

    $scope.user = {username:"",password:""}

    $scope.data = {
        error_login:"",
        error_username:"",
        error_password:"",
        btn_login:true,
        show_pass:"password"
    }
    $scope.view_pass = function () {
        if($scope.data.show_pass=="text"){
            $scope.data.show_pass="password";
        }else{
            $scope.data.show_pass="text";
        }
    }

    $scope.change_inputs = function (login_form) {
        if(login_form.$valid){
            $scope.data.error_username = "";
            $scope.data.error_password = "";
            $scope.data.btn_login = false;
        }else{
            $scope.data.error_username = EMAIL_ERROR;
            $scope.data.error_password = PASSWORD_ERROR;
            $scope.data.btn_login = true;
        }
    }
    $scope.send = function (login_form) {

        $http({
            method: 'POST',
            url: '/auth/login',
            headers: {
                'Content-Type': "application/json"
            },
            data: $scope.user
        }).then(function success(response) {
            sessionStorage.setItem("jwt_token",response.data.token)
            window.location.replace("/home")
        }, function error(response) {
            $scope.data.error_login = response.data.message;
        });
    }
};