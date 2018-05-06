var paginatorPage = new Vue({
    el: '#page',
    data: {
        page: page,
        size: size,
        countPage: countPage,
        pages: []
    },
    methods: {
        getSearchPreviousPage: function () {
            var p = this.page - 1
            var url = this.getSearchPage(p)
            return url
        },
        getSearchNextPage: function () {
            var p = this.page + 1
            var url = this.getSearchPage(p)
            return url
        },
        getSearchPage: function (p) {
            if (p > 0 && p < this.countPage+1) {
                var url = window.location.href
                if (url.indexOf('?') == -1) {
                    url += '?'
                }
                var index = url.lastIndexOf('&page=')
                return (index == -1 ? url : url.substring(0, index)) + '&page=' + p +"&size=" + this.size
            } else {
                return '#'
            }
        }
    },
    mounted: function () {
        var active
        if (this.countPage>10) {
            if (this.page < 6) {
                active = 1
            } else if (this.page > 5 && this.page < this.countPage - 4) {
                active = this.page - 4
            } else {
                active = this.countPage - 9
            }
            for (var i = 0; i < 10; i++) {
                this.pages.push(active)
                active++
            }
        } else {
            for (var i = 1; i < this.countPage + 1; i++) {
                this.pages.push(i)
            }
        }
    }
})
