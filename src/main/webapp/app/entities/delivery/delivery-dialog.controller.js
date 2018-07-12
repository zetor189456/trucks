(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('DeliveryDialogController', DeliveryDialogController);

    DeliveryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Delivery', 'Truck'];

    function DeliveryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Delivery, Truck) {
        var vm = this;

        vm.delivery = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trucks = Truck.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.delivery.id !== null) {
                Delivery.update(vm.delivery, onSaveSuccess, onSaveError);
            } else {
                Delivery.save(vm.delivery, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trucksApp:deliveryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateAdded = false;
        vm.datePickerOpenStatus.pickupDate = false;
        vm.datePickerOpenStatus.deliveryDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
