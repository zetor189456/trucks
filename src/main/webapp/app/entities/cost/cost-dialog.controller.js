(function() {
    'use strict';

    angular
        .module('trucksApp')
        .controller('CostDialogController', CostDialogController);

    CostDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cost', 'Truck'];

    function CostDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cost, Truck) {
        var vm = this;

        vm.cost = entity;
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
            if (vm.cost.id !== null) {
                Cost.update(vm.cost, onSaveSuccess, onSaveError);
            } else {
                Cost.save(vm.cost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trucksApp:costUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
