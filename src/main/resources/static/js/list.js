$(function () {

    var contactKeys = ['id', 'firstName', 'lastName'];
    var table = $('#contacts');

    $('#new').click(function (event) {
        event.preventDefault();
        document.location.href = 'create.docx';
    });

    this.drop = function (contactId) {
        $.ajax({
            url: apiPath + contactId,
            type: 'DELETE',
            contentType: 'application/json',
            success: function () {
                fetch();
            },
            error: handleError
        });
    };

    function fetch() {
        return $.ajax({
            url: apiPath,
            type: 'GET',
            contentType: 'application/json',
            success: function (data) {
                inflate(data);
            },
            error: handleError
        });
    }

    function inflate(data) {
        var content = [];
        $.each(data.contacts, function (i, value) {
            content.push('<tr>');
            $.each(contactKeys, function (i, key) {
                content.push('<td>' + value[key] + '</td>');
            });
            content.push('<td><button type="button" onclick="drop(' + value.id + ')">Delete</button><button type="button" onclick="document.location.href=\'update.html?contactId=' + value.id + '\'">Update</button></td>');
            content.push('</tr>');
        });
        table.html(content.join(''));
    }

    ajaxSetup();
    fetch();
});
