$(function () {

    var table = $('#phoneNumbers');
    var contactKeys = ['firstName', 'lastName'];
    var phoneNumberKeys = ['code', 'number'];

    this.drop = function (element) {
        $(element).closest('tr').remove();
    };

    var uniqueId = (function () {
        var i = 0;
        return function () {
            return i++;
        }
    })();

    $('#new').click(function (event) {
        event.preventDefault();
        var content = [];
        content.push('<tr>');
        $.each(phoneNumberKeys, function (i, key) {
            content.push('<td><input type="text" name="' + key + '_' + uniqueId() + '" class="required digits"/></td>');
        });
        content.push('<td><button type="button" onclick="drop(this)">Delete</button></td>');
        content.push('</tr>');
        table.append(content.join(''));
    });

    $('form').validate({
        submitHandler: function (form) {
            var data = {};

            $.each(contactKeys, function (i, key) {
                data[key] = $('#' + key).val();
            });

            var phoneNumbers = [];
            table.find('tr').each(function (i, row) {
                var phoneNumber = {};
                $.each(phoneNumberKeys, function (i, key) {
                    phoneNumber[key] = $(row).find('input[name^="' + key + '"]').val();
                });
                phoneNumbers.push(phoneNumber);
            });

            if (phoneNumbers.length > 0) {
                data.phoneNumbers = phoneNumbers;
            }

            $.ajax({
                url: apiPath,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function () {
                    document.location.replace('list.html');
                },
                error: handleError
            });
        }
    });

    ajaxSetup();
});
