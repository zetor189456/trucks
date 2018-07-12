(function() {
    'use strict';
    angular
        .module('trucksApp')
        .factory('Cost', Cost);

    Cost.$inject = ['$resource', 'DateUtils'];

    function Cost ($resource, DateUtils) {
        var resourceUrl =  'api/costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
