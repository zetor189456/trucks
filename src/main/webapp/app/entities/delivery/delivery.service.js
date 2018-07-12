(function() {
    'use strict';
    angular
        .module('trucksApp')
        .factory('Delivery', Delivery);

    Delivery.$inject = ['$resource', 'DateUtils'];

    function Delivery ($resource, DateUtils) {
        var resourceUrl =  'api/deliveries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateAdded = DateUtils.convertLocalDateFromServer(data.dateAdded);
                        data.pickupDate = DateUtils.convertDateTimeFromServer(data.pickupDate);
                        data.deliveryDate = DateUtils.convertDateTimeFromServer(data.deliveryDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateAdded = DateUtils.convertLocalDateToServer(copy.dateAdded);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateAdded = DateUtils.convertLocalDateToServer(copy.dateAdded);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
