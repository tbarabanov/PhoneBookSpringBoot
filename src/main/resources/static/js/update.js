$(function () {

    var table = $('#phoneNumbers');
    var contactKeys = ['firstName', 'lastName'];
    var phoneNumberKeys = ['code', 'number'];

    var contactId = new URLSearchParams(window.location.search).get('contactId');

    this.drop = function (contactId, phoneNumberId) {
        $.ajax({
            url: apiPath + contactId + '/phoneNumbers/' + phoneNumberId,
            contentType: 'application/json',
            type: 'DELETE',
            success: function () {
                fetch();
            },
            error: handleError
        });
    };

    function inflate(data) {

        $('#details').text(contactKeys.map(function (value) {
            return data[value];
        }).join(' '));

        var content = [];
        $.each(data.phoneNumbers, function (i, value) {
            content.push('<tr>');
            $.each(phoneNumberKeys, function (i, key) {
                content.push('<td>' + value[key] + '</td>');
            });
            content.push('<td><button type="button" onclick="drop(' + data.id + ', ' + value.id + ')">Delete</button></td>');
            content.push('</tr>');
        });
        table.html(content.join(''));
    }

    function fetch() {
        return $.ajax({
            url: apiPath + contactId,
            type: 'GET',
            contentType: 'application/json',
            success: function (data) {
                inflate(data);
            },
            error: handleError
        });
    }

    $('#new').validate({
        submitHandler: function (form) {
            $.ajax({
                url: apiPath + contactId + '/phoneNumbers',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    code: $('#code').val(),
                    number: $('#number').val()
                }),
                success: function (data) {
                    $(form).get(0).reset();
                    table.empty();
                    inflate(data);
                },
                error: handleError
            });
        }
    });
    ajaxSetup();
    fetch();
});
