$(document).ready(function () {
    navigator.serviceWorker.register('firebase-messaging-sw.js')
    getClientToken()

    $.ajax({
        url: "http://localhost:8080/kk/list",
        type: "get",
        dataType: "json",
        contentType: "application/json"
    }).done(function (list) {
        Array.from(list).forEach(item=>
            // console.log(item)
            item.no%2 == 0 ? appendRightContent(item) : appendLeftContent(item)
        )


    })

})
// var source = $("#product").html();
// var tmp = Handlebars.compile(source);
//
// function appendLeftContent(data) {
//     $(".leftContent").append(tmp(data));
// }
//
// function appendRightContent(data) {
//     $(".rightContent").append(tmp(data));
// }


function appendLeftContent(data) {
    let tag = "<div class=\"container\">\n" +
    "            <div style=\"display: flex\">\n" +
    "                <img class=\"productImg\" src=\""+data.image+"\">\n" +
    "                <div style='width: 100px'>\n" +
    "                    <span class=\"productTitle\">"+data.title+"</span>\n" +
    "                    <span class=\"productPrice\">"+data.price+"</span>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>"
    $(".leftContent").append(tag)
}

function appendRightContent(data) {
    let tag = "<div class=\"container\">\n" +
        "            <div style=\"display: flex\">\n" +
        "                <img class=\"productImg\" src=\""+data.image+"\">\n" +
        "                <div style='width: 100px'>\n" +
        "                    <span class=\"productTitle\">"+data.title+"</span>\n" +
        "                    <span class=\"productPrice\">"+data.price+"</span>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>"
    $(".rightContent").append(tag)
}

function getClientToken() {
    var result = null
    // Initialize Firebase
    var config = {
        apiKey: "AIzaSyB5h1dtyUXyYroO4ZengUlSKCa93-WoRnU",
        authDomain: "jarvis-77f82.firebaseapp.com",
        databaseURL: "https://jarvis-77f82.firebaseio.com",
        projectId: "jarvis-77f82",
        storageBucket: "jarvis-77f82.appspot.com",
        messagingSenderId: "172501072688",
        appId: "1:172501072688:web:e1d67ee6f83f43711ba6f5",
        measurementId: "G-GRFQ9P114V"
    }

    firebase.initializeApp(config)
    const messaging = firebase.messaging()

    messaging.requestPermission()
        .then(function () {
            console.log("Have permission")
            return messaging.getToken()
        })
        .then(function (token) {
            console.log(token)
            result = token
        })
        .catch(function (arr) {
            console.log("Error Occured" + arr)
        })

    return result
}