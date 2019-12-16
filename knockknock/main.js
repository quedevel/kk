$(document).ready(function () {
    navigator.serviceWorker.register('firebase-messaging-sw.js')
    registerClientToken()
    $.getJSON("http://localhost:8080/kk/list", "", list => Array.from(list).forEach(item => item.no % 2 == 0 ? appendRightContent(item) : appendLeftContent(item)))
    textFit(document.getElementsByClassName('productTitle'), {multiLine: true})
})

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function content(data) {
    let fee = data.fee === 0 ? '무료' : data.fee
    let tag = "<div class='container'>\n" +
        "            <div style='display: flex'>" +
        "                <img class='productImg' src='" + data.image + "'>" +
        "                <div style='width: 100px'>" +
        "                    <a target='_blank' href='" + data.link + "'><span class='productTitle'>" + data.title + "</span></a>" +
        "                    <span class='productPrice'>" + comma(data.price) + "/" + fee + "</span>" +
        "                </div>" +
        "            </div>" +
        "        </div>"
    return tag
}

function appendLeftContent(data) {
    $(".leftContent").append(content(data))
}

function appendRightContent(data) {
    $(".rightContent").append(content(data))
}

function registerClientToken() {
    let config = {
        apiKey: "AIzaSyB5h1dtyUXyYroO4ZengUlSKCa93-WoRnU",
        authDomain: "jarvis-77f82.firebaseapp.com",
        databaseURL: "https://jarvis-77f82.firebaseio.com",
        projectId: "jarvis-77f82",
        storageBucket: "jarvis-77f82.appspot.com",
        messagingSenderId: "172501072688",
        appId: "1:172501072688:web:17b57e04673ab8351ba6f5",
        measurementId: "G-WJ48JZCEDP"
    }

    firebase.initializeApp(config)

    const messaging = firebase.messaging()

    messaging.requestPermission()
        .then(() => messaging.getToken())
        .then(token => $.post("http://localhost:8080/kk/token", token, msg => console.log(msg)))
        .catch(err => console.log("Error Occured" + err))
}