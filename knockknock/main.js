var $modal=$('.modal')

$(document).ready(function () {
    navigator.serviceWorker.register('firebase-messaging-sw.js')
    $('#googleLogin').on('click', event => socialLogin(social.google))
    try {
        registerClientToken()
        getRestAPI("http://localhost:8080/kk/list", "", list => Array.from(list).forEach(item => item.no % 2 == 0 ? appendRightContent(item) : appendLeftContent(item)))
    } catch (e) {
        console.log(e)
    }
})

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function content(data) {
    let product = data.product
    let fee = product.fee === 0 ? '무료' : product.fee
    let tag = "<div class='container'>\n" +
        "            <div style='display: flex'>" +
        "                <img class='productImg' src='" + product.img + "'>" +
        "                <div style='width: 100px'>" +
        "                    <a target='_blank' href='" + product.link + "'><span class='productTitle'>" + product.title + "</span></a>" +
        "                    <span class='productPrice'>" + comma(product.price) + "/" + fee + "</span>" +
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
            postRestAPI("http://localhost:8080/kk/token", decodeURIComponent(token), msg=>console.log(msg))
        )
        .catch(err => console.log("Error Occured" + err))
}

var social = {google:'http://localhost:8080/oauth2/authorization/google', naver:'naver', kakao:'kakao'}

function socialLogin(url) {
    // $("#login").attr('src', url)
    // getRestAPI(url, '', html => console.log(html))
    window.open(url, '_black', 'width=600, height=400')
}

function getRestAPI(url, data, success) {
    executeAjax(url, 'get', data, success)
}

function postRestAPI(url, data, success) {
    executeAjax(url, 'post', data, success)
}

function putRestAPI(url, data, success) {
    executeAjax(url, 'post', data, success)
}

function executeAjax(url, type, data, success) {
    $.ajax({
        url: url,
        type: type,
        data: data,
        contentType: "application/json"
    }).done(response => success(response)).fail(error => error.responseJSON.status === 401 ? loginModal() : console.log('에러'))
}

function loginModal() {
    $modal.css('display', 'block')
    throw new Error('권한이 없습니다.')
}