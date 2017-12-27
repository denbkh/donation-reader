"use strict";
var streamlabs = null;

function getAccessToken() {
    return Cookies.get('access_token');
}

function startRealTime(csrfTokenValue) {
    lockControls();

    var accessToken = getAccessToken();
    if (accessToken === undefined) {
        $.ajax({
            url: "/realtime",
            data: {
                _csrf_token: csrfTokenValue
            },
            type: "POST"
        })
            .done(function (result) {
                console.log(result);
                loadSocketToken(getAccessToken());
            })
            .fail(ajaxErrorHandler);

    } else {
        loadSocketToken(accessToken);
    }
}

function ajaxErrorHandler(xhr, status, errorThrown) {
    handleError("Status: " + status + ", Error: " + errorThrown)
}

function handleError(message) {
    $('#error').removeClass('hidden');
    $('#errorMessage').html(message);

    stopRealTime();
}

function lockControls() {
    $('#startRealTimeButton').button('loading');
    $("#moneyFilter").prop("disabled", true);
}

function unlockControls() {
    $('#startRealTimeButton').button('reset');
    $("#moneyFilter").prop("disabled", false);

    $('#startRealTimeButton').toggleClass('hidden', false);
    $('#stopRealTimeButton').toggleClass('hidden', true);
    $('#connectedAlert').toggleClass('hidden', true);
}

function loadSocketToken(accessToken) {
    console.log("accessToken = " + accessToken);

    $.ajax({
        url: "https://streamlabs.com/api/v1.0/socket/token",
        data: {
            access_token: accessToken
        },
        type: "GET"
        // xhrFields: {
        //     withCredentials: true
        // }
    })
        .done(function (result) {
            console.log(result);
            openWebSocket(result.socket_token);
        })
        .fail(ajaxErrorHandler);
}

function stopRealTime() {
    if (streamlabs !== null) {
        streamlabs.close();
    }

    unlockControls();
}

function openWebSocket(socketToked) {
    //Connect to socket
    streamlabs = io("https://sockets.streamlabs.com?token=" + socketToked);

    var table = document.getElementById("donationsTable");
    var rowTemplate = $.templates("#rowTemplate");
    var filterValue = Number($('#moneyFilter').val());

    streamlabs.on('connect', function () {
        $('#startRealTimeButton').toggleClass('hidden', true);
        $('#stopRealTimeButton').toggleClass('hidden', false);

        $('#connectedAlertMoneyVal').html(filterValue);
        $('#connectedAlert').toggleClass('hidden', false);
    });

    streamlabs.on('connect_error', function (error) {
        handleError("Socket error: " + error);
    });

    //Perform Action on event
    streamlabs.on('event', function (eventData) {
        if (!eventData.for && eventData.type === 'donation') {
            console.log(eventData.message);
            var arr = eventData.message;
            for (var i = 0; i < arr.length; i++) {

                if (arr[i].amount >= filterValue) {
                    var html = rowTemplate.render({
                        date: moment().format('HH:mm:ss'),
                        name: arr[i].from,
                        amount: arr[i].amount,
                        message: arr[i].message === "" ? "(нет коммента)" : arr[i].message
                    });

                    table.insertAdjacentHTML('beforeend', html);
                }

                console.log(arr[i].from + " " + arr[i].amount + " " + arr[i].message);
            }
        }
    });
}



