app.controller('user', function ($scope, $http, $location) {

    $scope.save_user = function () {
        var data = {

            firstName: $scope.firstName,
            last_Name: $scope.last_Name,
            username: $scope.username,
            email: $scope.email,
            contact: $scope.contact,
            department: $scope.department,
            created_date: $scope.created_date,
            created_by: $scope.created_by,
            password: $scope.password,
            mobile: 'false'
        };
        console.log(data);


        $http({
            method: "POST",
            // url: "../vts_apis/session.php",
            url: "../crm_test/index.php/admin/login",
            // url: ".../login",
            data: data,
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then(function (response) {

            // console.log(response.data.errorCode);

            if (response.data.data[0].user_type == 'user') {
                // sessionStorage.setItem("id", response.data.data[0].id);
                // sessionStorage.setItem("username", response.data.data[0].username);
                $location.path("/userDashboard");
            } else {
                $location.path("/adminDashboard");
            }
        }, function (response) {
            alert('server error occured')
        })
    }
    $scope.logout = function () {
        localStorage.removeItem('id', 'username');
        $location.path("/crm");
    }


})