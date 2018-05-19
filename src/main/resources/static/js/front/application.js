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
        if(!$(e.target).is('#getHeaderCategories') && !$(e.target).is('#viewHeaderCatsDivId')){
            outHeaderCategories()
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

var getHeaderCategories = new Vue({
    el: '#getHeaderCategories',
    data: {
        categories: [],
        filterCatsByLastLevel: []
    },
    computed: {
        filterCats: function () {
            var filter = []
            for (key in this.categories) {
                if (this.categories[key].level == 1) {
                    filter.push(this.categories[key])
                }
            }
            return filter
        }
    },
    methods:{
        getCategoriesByLaseLevel: function (cat) {
            var self = this
            var filter = []
            self.filterCatsByLastLevel = []
            for (key in self.categories) {
                if (self.categories[key].parent == cat.id) {
                    filter = []
                    for (key1 in self.categories) {
                        if (self.categories[key1].parent == self.categories[key].id) {
                            filter.push(self.categories[key1])
                        }
                    }
                    self.categories[key].sub = filter
                    self.filterCatsByLastLevel.push(self.categories[key])
                }
            }
        }
    },
    mounted: function () {
        var self = this
        $.get('/front/categories', function (result) {
            //console.log(result)
            self.categories = result
        })
    }
})

function viewHeaderCategories() {
    $("#getHeaderCategories").css("display", "block");
}

function outHeaderCategories() {
    $("#getHeaderCategories").css("display", "none");
}