'use strict';


angular.module('myApp.view1', ['ngRoute', 'ngStomp'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', ['$scope', '$http', '$stomp', '$log', function($scope, $http, $stomp, $log) {

  $scope.graphData = [];

  $scope.$watch('graphData', function() {
    $scope.chart = c3.generate({
      bindto: '#chart',
      data: {
        json: $scope.graphData,
        keys: { value: ['price'] }
      }
    });
  });

  $http.get('http://localhost:8080/ticks').then(
      function(res){
        $scope.graphData = res.data;
      }
  );

  // push notifications
  function appendPrice(payload) {
    var jsonData = $scope.graphData;
    jsonData = jsonData.concat(payload);
    if (jsonData.length > 30) jsonData.shift();
    $scope.graphData = jsonData;
  }

  $stomp.connect("http://localhost:8080/ticks-ws")
      .then(function(frame) {
        $stomp.subscribe('/topic/ticks', function(payload, headers, res) {
          $scope.$apply(function () {
            appendPrice(payload);
          });
        })
      })

}]);

