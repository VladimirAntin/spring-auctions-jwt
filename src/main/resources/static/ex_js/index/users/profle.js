/**
 * Created by vladimir_antin on 14.5.17..
 */
function Profile($scope,data,$http,$routeParams,$mdDialog,$mdToast) {

    $scope.data = {
        show:{
            disable_input:true,
            btn_edit:false,
            btn_delete:false
        },
        btn_edit:{
            icon:"edit_mode",
            tooltip:"Upadate user"
        }
    };
    $http({
        method: 'GET',
        url: '/api/users/'+$routeParams.userId,
        headers: {
            "Content-type":"application/json",
            "Authorization":data.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.user = response.data;
        }
    });
    $http({
        method: 'GET',
        url: '/api/me',
        headers: {
            "Content-type":"application/json",
            "Authorization":data.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.me = response.data;
            if($scope.me!=null){
                if($scope.me.email==$scope.user.email){
                    $scope.data.show.btn_edit=true;
                    $scope.data.show.btn_delete=false;
                }else if($scope.me.role=="admin"){
                    $scope.data.show.btn_edit=true;
                    if($scope.me.email==$scope.user.email){
                        $scope.data.show.btn_delete=false;
                    }else{
                        $scope.data.show.btn_delete=true;
                    }
                }else{
                    $scope.data.show.btn_edit=false;
                    $scope.data.show.btn_delete=false;
                }
            }
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
                        "Authorization":data.token
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

    $scope.edit_mode = function (edit_forum) {
        if($scope.data.show.disable_input){
            $scope.data.show.disable_input = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_forum.$valid){
            $http({
                method: 'PUT',
                url: '/api/users/'+$routeParams.userId,
                headers: {
                    "Content-type":"application/json",
                    "Authorization":data.token
                },
                data:$scope.user
            }).then(function done(response) {
                if(response.status==200){
                    toast_message("User updated!","Ok",$mdToast);
                    $scope.data.show.disable_input = true;
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
}