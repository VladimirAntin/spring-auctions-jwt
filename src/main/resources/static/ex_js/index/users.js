/**
 * Created by vladimir_antin on 12.5.17..
 */
function Users($scope,$http,$mdDialog,$mdToast) {
    $scope.token = "jwt "+localStorage.getItem("jwt_token");
    $scope.users_head_items = [
        {name:"id",icon:"arrow_drop_down"},
        {name:"name",icon:"arrow_drop_down"},
        {name:"email",icon:"arrow_drop_down"},
        {name:"address",icon:"arrow_drop_down"},
        {name:"phone",icon:"arrow_drop_down"},
        {name:"role",icon:"arrow_drop_down"}
    ];

    $scope.user_roles = [
        {name:"All users",value:""},
        {name:"Administrators",value:"admin"},
        {name:"Owners",value:"owner"},
        {name:"Bidder",value:"bidder"}
    ];
    $scope.selectedRole;
    $scope.getSelectedRole = function() {
        if ($scope.selectedRole !== undefined) {
            return $scope.selectedRole.name;
        }
    };

    $scope.sort =function (name,sort_items){
        sort($scope,name,sort_items);
    };
    $scope.users = [];

    $http({
        method : "GET",
        url : "/api/users",
        headers:{
            "Content-type":"application/json",
            "Authorization":$scope.token
        }
    }).then(function(response) {
        if(response.status==200) {
            $scope.users = response.data;
        }
    },function error(response) {
        if(response.status==401){
            toast_message("Unauthorized, you do not have access","Ok",$mdToast);
            window.location.replace("/home")
        }else if(response.status>=500){
            toast_message("Server Error","Ok",$mdToast);
        }
    });

    $scope.delete_user = function (user){
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
                    indexUser = $scope.users.indexOf(user);
                    $scope.users.splice(indexUser,1);
                    toast_message("User deleted!","Ok",$mdToast);
                }
            },function error(response) {
                if(response.status==401){
                    toast_message("Unauthorized, it is your account","Ok",$mdToast);
                }else if(response.status>=500){
                    toast_message("Server Error","Ok",$mdToast);
                }
            });
        });
    };
}

