const $login = $('#login')
const $interest = $('#interest')
const $leftContent = $(".leftContent")
const $rightContent = $(".rightContent")
const Social = Object.freeze({
    google: 'http://localhost:8080/oauth2/authorization/google',
    naver: 'http://localhost:8080/oauth2/authorization/naver',
    kakao: 'http://localhost:8080/oauth2/authorization/kakao',
    facebook: 'http://localhost:8080/oauth2/authorization/facebook'
})
const Method = Object.freeze({
    GET: 'GET', POST: 'POST', PUT: 'PUT', DELETE: 'DELETE'
})

const HttpStatus = Object.freeze({
    OK: 200, MOVED_PERMANENTLY: 301
})

const messaging = (function () {
    const config = Object.freeze({

    })

    firebase.initializeApp(config)

    return firebase.messaging()
})()

const basieUrl = "http://localhost:8080/kk/"

var prevAjax = null

$(document).ready(function () {

    // $(".socialLogin").on('click', function (event) {
    //     socialLogin(Social[this.getAttribute('data-site')])
    // })

    // $(".content").on('click', '.productTitle', function (event) {
    //     event.preventDefault()
    //     new AjaxBuilder().method(Method.GET).url("click").data({no: this.getAttribute("data-no")}).build()
    // })

    // $("#setInterestBtn").on('click', event => $interest.css('display', 'block'))


    // $("#saveInterestBtn").on('click', event => {
    //     let interests = new Array();
    //     $("input:checkbox[name=category]:checked").each( (index, item) => interests.push(item.value) )
    //     new AjaxBuilder().method(Method.PUT).url("updateInterest").data(JSON.stringify(interests)).success(status=>$interest.css('display', 'none')).build()
    // })

    new AjaxBuilder().method(Method.GET).url("list").success(list => Array.from(list).forEach(item => item.no % 2 == 0 ? appendRightContent(item) : appendLeftContent(item))).build()
})

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}


function content(data) {
    let product = data.product
    product.no = data.no
    product.fee = product.fee === 0 ? '무료' : comma(product.fee)
    product.price = comma(product.price)

    let template = $('#template').html();
    Mustache.parse(template);
    let rendered = Mustache.render(template, product);

    return rendered
}

function appendLeftContent(data) {
    $leftContent.append(content(data))
}

function appendRightContent(data) {
    $rightContent.append(content(data))
}

function registerClientToken() {
    messaging.requestPermission()
        .then(() => messaging.getToken())
        .then(token =>
            new AjaxBuilder().method(Method.POST).url("token").data(decodeURIComponent(token)).build()
        )
        .catch(err => console.log("Error Occured" + err))
}

// function socialLogin(url) {
//     const loginPopup = window.open(url, '_black', 'width=auto, height=auto')

//     let interval = window.setInterval(() => {
//         if (loginPopup.closed) {
//             registerClientToken()
//             window.clearInterval(interval)
//             //301일시 이동
//             new AjaxBuilder().method(Method.GET).url("interestChecking")
//                 .success(status => status === HttpStatus.MOVED_PERMANENTLY ? $interest.css('display', 'block') : executeAjax(prevAjax)).build()

//             $login.css('display', 'none')
//         }
//     }, 1000);
// }

function executeAjax(ajax) {
    $.ajax({
        url: ajax.url,
        type: ajax.method,
        data: ajax.data,
        contentType: "application/json",
        success: function (response) {
            ajax.success(response)
        },
        error: function (error) {
            let status = error.responseJSON.status
            prevAjax = ajax
            status === 401 ? $login.css('display', 'block') : console.log("Error :" + status)
        }
    })
}

class Ajax {
    constructor(url, method, data, success) {
        this.url = url;
        this.method = method;
        this.data = data;
        this.success = success;
    }
}

class AjaxBuilder {

    constructor() {
        this.ajax = new Ajax()
    }

    url(url) {
        this.ajax.url = basieUrl+url
        return this
    }

    method(method) {
        this.ajax.method = method
        return this
    }

    data(data) {
        this.ajax.data = data
        return this
    }

    success(success) {
        this.ajax.success = success
        return this
    }

    build() {
        executeAjax(this.ajax)
        return this.ajax
    }
}
