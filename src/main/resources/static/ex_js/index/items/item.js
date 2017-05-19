/**
 * Created by vladimir_antin on 19.5.17..
 */
function Item($scope,$http,$routeParams,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.data = {
        show:{
            disable_input:true,
            btn_edit:false,
            btn_delete:false
        },
        btn_edit:{
            icon:"edit_mode",
            tooltip:"Upadate item"
        }

    };
    $http({
        method: 'GET',
        url: '/api/items/'+$routeParams.itemId,
        headers: {
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200){
            $scope.item = response.data;
            $scope.me_service();
        }
    });
    $scope.openDeleteMode = function (item) {
        var confirm = $mdDialog.confirm()
            .title('Do you sure?')
            .textContent('item with id: "'+item.id+'" will be deleted')
            .ok('Ok')
            .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
            $http({
                method : "DELETE",
                url : "/api/items/"+item.id,
                headers:{
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                }
            }).then(function (response) {
                if(response.status ==204){
                    window.location.replace("#/items");
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized, you can not delete item","Ok",$mdToast);
                }else if(response.status>=500){
                    toast_message("Server Error","Ok",$mdToast);
                }
            });
        });
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
                    if($scope.me.role=="admin"){
                        $scope.data.show.btn_edit=true;
                        $scope.data.show.btn_delete=true;
                    }else{
                        $scope.data.show.btn_edit=false;
                        $scope.data.show.btn_delete=false;
                    }
                }
            }
        });
    };

    $scope.edit_mode = function (edit_forum) {
        if($scope.data.show.disable_input){
            $scope.data.show.disable_input = false;
            $scope.data.btn_edit.icon = "save";
            $scope.data.btn_edit.tooltip = "Save";
        }else if (edit_forum.$valid){
            $http({
                method: 'PUT',
                url: '/api/items/'+$routeParams.itemId,
                headers: {
                    "Content-type":"application/json",
                    "Authorization":$scope.token
                },
                data:$scope.item
            }).then(function done(response) {
                if(response.status==200){
                    toast_message("Item updated!","Ok",$mdToast);
                    $scope.data.show.disable_input = true;
                    $scope.data.btn_edit.icon = "edit_mode";
                    $scope.data.btn_edit.tooltip = "Update item";
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

    $scope.change_photo = function () {
        $.ajax({
            url: 'api/items/'+$scope.item.id+'/upload',
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