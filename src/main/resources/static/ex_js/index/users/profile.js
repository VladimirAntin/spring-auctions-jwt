/**
 * Created by vladimir_antin on 14.5.17..
 */
function Profile($scope,$http,$routeParams,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        show:{
            disable_input:true,
            btn_edit:false,
            btn_delete:false,
            btn_password:false,
            disable_change_role:true
        },
        btn_edit:{
            icon:"edit_mode",
            tooltip:"Upadate user"
        },
        btn_password:{
            icon:"lock",
            tooltip:"Change password"
        }
    };
    $http({
        method: 'GET',
        url: '/api/users/'+$routeParams.userId,
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.user = response.data;
            $scope.me_service();
        }
    });
    $scope.openDeleteMode = function (user) {
        if($scope.me.email==user.email){
            toast_message("Unauthorized, it is your account","Ok",$mdToast);
        }else{
            var confirm = $mdDialog.confirm()
                .title('Do you sure?')
                .textContent('user with a email: "'+user.email+'" and id: "'+user.id+'" will be deleted')
                .ok('Ok')
                .cancel('Cancel');
            $mdDialog.show(confirm).then(function() {
                $http({
                    method : "DELETE",
                    url : "/api/users/"+user.id,
                    headers:{
                        "Content-type":"application/json",
                        "Authorization":$scope.token
                    }
                }).then(function (response) {
                    if(response.status ==204){
                        window.location.replace("#/users")
                    }
                },function error(response) {
                    if(response.status==401){
                        toast_message("Unauthorized, it is your account","Ok",$mdToast);
                    }else if(response.status>=500){
                        toast_message("Server Error","Ok",$mdToast);
                    }
                });
            });
        }
    };

    $scope.me_service = function () {
        $http({
            method: 'GET',
            url: '/api/me',
            headers: {
                "Content-type":"application/json",
                "Authorization":$scope.token
            }
        }).then(function(response) {
            if(response.status==200){
                $scope.me = response.data;
                if($scope.me!=null){
                    if($scope.me.email==$scope.user.email){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_password=true;
                        $scope.data.show.btn_delete=false;
                    }else if($scope.me.role=="admin"){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_password=true;
                        if($scope.me.email==$scope.user.email){
                            $scope.data.show.btn_delete=false;
                        }else{
                            $scope.data.show.btn_delete=true;
                        }
                    }else{
                        $scope.data.show.btn_edit=false;
                        $scope.data.show.btn_password=false;
                        $scope.data.show.btn_delete=false;
                    }
                }
            }
        });
    };

    $scope.edit_mode = function (edit_forum) {
        if($scope.data.show.disable_input){
            if($scope.me.role=="admin"){
                $scope.data.show.disable_change_role = false;
            }
            $scope.data.show.disable_input = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_forum.$valid){
            $http({
                method: 'PUT',
                url: '/api/users/'+$routeParams.userId,
                headers: {
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                },
                data:$scope.user
            }).then(function done(response) {
                if(response.status==200){
                    toast_message("User updated!","Ok",$mdToast);
                    $scope.data.show.disable_input = true;
                    $scope.data.show.disable_change_role = true;
                    $scope.data.btn_edit.icon = "edit_mode";
                    $scope.data.btn_edit.tooltip = "Update user";
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized","Ok",$mdToast);
                }
            });
        }else{
            toast_message("Form is not valid","Ok",$mdToast);
        }
    };
    
    $scope.update_password = function () {
        $mdDialog.show({
            templateUrl: 'views/users/change_password_dialog.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true,
            locals:{user:$scope.user},
            controller: ChangePasswordController
        });
        function ChangePasswordController($scope,data,$mdDialog,user,$routeParams,$mdToast) {
            $scope.user =user;
            $scope.btn_save_password = false;
            $scope.save_password = function() {
                $http({
                    method: 'PATCH',
                    url: '/api/users/'+$routeParams.userId+"/password",
                    headers: {
                        "Content-type":"application/json",
                        "Authorization":$scope.token
                    },
                    data:$scope.user
                }).then(function(response) {
                    if(response.status==200){
                        toast_message("Password was changed!","Ok",$mdToast);
                        $mdDialog.hide();
                    }
                },function (response) {
                    if(response.status==401){
                        toast_message("Unauthorized","Ok",$mdToast);
                    }else if(response.status>=500){
                        toast_message("Server error","Ok",$mdToast);
                    }
                });
            };
            $scope.cancel = function() {
                $mdDialog.hide();
            };
            $scope.check_password = function(change_password) {
                if(change_password.$valid){
                    $scope.btn_save_password = true;
                }else{
                    $scope.btn_save_password = false;
                }
            };
        }
    };

    $scope.change_photo = function () {
        $.ajax({
            url: 'api/users/'+$scope.user.id+'/upload',
            type: 'POST',
            headers:{
                "Authorization":$scope.token
            },
            data: new FormData($('form')[0]),
            cache: false,
            contentType: false,
            processData: false,
            statusCode:{
                200:function (response) {
                    window.location.reload();
                }
            }
        });
    };
}