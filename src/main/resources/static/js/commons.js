var apiPath = '/api/v1/contacts/';

function ajaxSetup() {
    $.ajaxSetup({
        headers: {
            Authorization: 'Basic ' + btoa('user:$ecret')
        }
    });
}

function handleError(jqXHR, textStatus, errorThrown) {
    $('#error').html('<h1>Status Code: ' + jqXHR.status + '</h1><h2>errorThrown: ' + errorThrown + '</h2><h2>jqXHR.responseText:</h2><div>' + jqXHR.responseText + '</div>');
}
