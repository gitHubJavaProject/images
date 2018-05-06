function searchFocus() {
    $(".search-result").css("display", "block");
    $.get('/front/selectKey', 'param='+ $("#exampleInputAmount").val(), function (data) {
        if(null==data.keys || data.keys.length == 0)  {
            $(".search-result").css("height","0px");
            $(".search-result").css("border-bottom","0px");
        }else{
            $(".search-result").css("height","auto");
        }
        $('.table-tbody').html("")
        for (var i = 0; i < data.keys.length; i++) {
            var html = '<tr class="table-tr" onclick="selecTable(this)">' +
                '<td style="width: 50px;text-align: center;">'+(i+1)+'</td><td>'+data.keys[i].key+'</td>' +
                '<td class="text-right">约'+data.keys[i].selectNumber+'个结果</td></tr>'
            if (i<3) {
                html =  '<tr class="table-tr" onclick="selecTable(this)">' +
                    '<td style="width: 50px;"><div class="number-div">'+(i+1)+'</div></td><td>'+data.keys[i].key+'</td>' +
                    '<td class="text-right">约'+data.keys[i].selectNumber+'个结果</td></tr>'
            }
            $('.table-tbody').append(html)
        }
    })
}

function selecTable(ths) {
    $("#exampleInputAmount").val($(ths).children(':first').next().html())
    $(".search-result").css("display", "none");
}

function headerSelectSubmit() {
    window.location.href = '/front/list?params='+encodeURIComponent($("#exampleInputAmount").val())
}

function searchBlur() {
    $(".search-result").css("display", "none");
}

$(function () {
    $("body").click(function (e) {
        if(!$(e.target).is('#exampleInputAmount') && !$(e.target).is('.table-tr')){
            searchBlur()
        }
    })
})

function login() {
    $("#loginModalId").modal('show')
}

function logout() {
    $.get('/front/logout',function (result) {
        if (result.code == 204) {
            window.open('/front/index')
        }
    })
}

var loginModal = new Vue({
    el: '#loginModalId',
    data: {
        userName:null,
        pwd:null,
        error:null
    },
    methods:{
        frontLogin: function () {
            if (!this.userName || !this.pwd) {
                this.error = '用户名或密码不能空！'
            } else {
                $.post('/front/login', 'userName=' + this.userName + '&pwd=' + this.pwd, function (result) {
                    if (result.code == 200) {
                        $("#loginModalId").modal('hide')
                        location.href = location.href
                    } else {
                        loginModal.error = result.msg
                    }
                })
            }
        }
    }
})

function regist() {
    window.location.href = '/front/register?' + window.location.href
}