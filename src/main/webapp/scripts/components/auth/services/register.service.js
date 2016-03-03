'use strict';

angular.module('myseekApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


