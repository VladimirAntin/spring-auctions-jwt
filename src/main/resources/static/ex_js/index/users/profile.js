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
            btn_delete_auction:false,
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
            me_service($http,$scope, function (me) {
                $scope.me=me;
                if($scope.me!=null){
                    if($scope.me.email==$scope.user.email){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_password=true;
                        $scope.data.show.btn_delete=false;
                        $scope.data.show.btn_delete_auction=true;
                    }else if($scope.me.role=="admin"){
                        $scope.data.show.btn_delete_auction=true;
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_password=true;
                        if($scope.me.email==$scope.user.email){
                            $scope.data.show.btn_delete=false;
                        }else{
                            $scope.data.show.btn_delete=true;
                        }
                    }else{
                        $scope.data.show.btn_delete_auction=false;
                        $scope.data.show.btn_edit=false;
                        $scope.data.show.btn_password=false;
                        $scope.data.show.btn_delete=false;
                    }
                }

            });
        }
    },function error(response) {
        if(response.status==404){
            window.location.replace("/404/users/"+$routeParams.userId);
        }
    });
    $http({
        method: 'GET',
        url: '/api/users/'+$routeParams.userId+"/auctions",
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.auctions = response.data;
        }
    });
    $scope.openDeleteMode = function (user) {
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    };

    $scope.edit_mode = function (edit_form) {
        if($scope.data.show.disable_inputs){
            if($scope.me.role=="admin"){
                $scope.data.show.disable_change_role = false;
            }
            $scope.data.show.disable_inputs = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_form.$valid){
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
                    $scope.data.show.disable_inputs = true;
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

    $scope.auctions_head_items = [
        {title:"id",icon:"arrow_drop_down", name:"id"},
        {title:"item name",icon:"arrow_drop_down", name:"item.id"},
        {title:"start date",icon:"arrow_drop_down", name:"startDate"},
        {title:"end date",icon:"arrow_drop_down", name:"endDate"},
        {title:"start price",icon:"arrow_drop_down", name:"startPrice"},
        {title:"sold",icon:"arrow_drop_down", name:"item.sold"}
    ];
    $scope.items_sold = [
        {name:"All auctions",value:""},
        {name:"Sold",value:"true"},
        {name:"Not sold",value:"false"}
    ];
    $scope.itemSold;
    $scope.isSold = function() {
        if ($scope.itemSold !== undefined) {
            return $scope.itemSold.name;
        }
    };
    $scope.sort =function (name,sort_items){
        sort($scope,name,sort_items);
    };

    $scope.delete_auction = function (auction) {
        delete_auction(auction,$scope,$http,$mdDialog,$mdToast);
    }

}