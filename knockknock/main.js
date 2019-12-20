$(document).ready(function () {
    navigator.serviceWorker.register('firebase-messaging-sw.js')
    registerClientToken()
    naverLogin()
    $('#googleLogin').on('click',googleLogin)
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

    }

    firebase.initializeApp(config)

    const messaging = firebase.messaging()

    messaging.requestPermission()
        .then(() => messaging.getToken())
        .then(token =>
            // $.post("http://localhost:8080/kk/token", decodeURIComponent(token), msg => console.log(msg))
            $.ajax({contentType: "application/json", url:"http://localhost:8080/kk/token", type:'post', data:decodeURIComponent(token), success:msg=>console.log(msg)})
        )
        .catch(err => console.log("Error Occured" + err))
}

function naverLogin() {
    let naverLogin = new naver.LoginWithNaverId(
        {
            clientId: "Up1NzGvqP7IQqVSndn2l",
            callbackUrl: "http://localhost:8080/kk/naverLogin",
            isPopup: false,
            loginButton: {color: "white", type: 1, height: 30}
        }
    )
    naverLogin.init()
}

function googleLogin() {
    window.open('http://localhost:8080/oauth2/authorization/google', '_black', 'width=600, height=400')
    return false
}