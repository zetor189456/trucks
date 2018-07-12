(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('TruckDetailController', TruckDetailController);

    TruckDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Truck', 'Delivery', 'Cost'];

    function TruckDetailController($scope, $rootScope, $stateParams, previousState, entity, Truck, Delivery, Cost) {
        var vm = this;

        vm.truck = entity;
        vm.previousState = previousState.name;
        vm.deliveries = []

        function isForThisTruck(value) {
            return value.truck.id == vm.truck.id;
        }


        function loadDeliveries () {
            Delivery.query({
                page: 0,
                size: 100,
                sort: 'id'
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.deliveries = data.filter(isForThisTruck);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        loadDeliveries();

        function loadCosts () {
            Cost.query({
                page: 0,
                size: 100,
                sort: 'id'
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.costs = data.filter(isForThisTruck);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        loadCosts();


        var unsubscribe = $rootScope.$on('trucksApp:truckUpdate', function(event, result) {
            vm.truck = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
