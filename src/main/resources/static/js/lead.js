app.controller('lead', function ($scope, $http, $location) {

    $scope.save = function () {
        var data = {

            client_name: $scope.clientName,
            client_number: $scope.clientNumber,
            client_email: $scope.clientEmail,
            client_address: $scope.clientAddress,
            client_title: $scope.clientTitle,
            datemodify: $scope.clientdate_modify,
            client_type: $scope.client_type,
            client_status: $scope.clientStatus,
            sizeofbusiness: $scope.sizeofbusiness,
            sizeof_stages: $scope.sizeof_stages,
            dateofcreation: $scope.dateofcreation,
            next_action: $scope.next_action,
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