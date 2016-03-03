 'use strict';

angular.module('myseekApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-myseekApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-myseekApp-params')});
                }
                return response;
            }
        };
    });
