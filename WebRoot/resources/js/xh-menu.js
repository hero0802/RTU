/**
 * 
 */

if(!("xh" in window)){window.xh={}}

jQuery(function(a){
	window.xh.click_event=a.fn.tap?"tap":"click"
})
jQuery(function(a) {
    xh.handle_side_menu(jQuery);
});


xh.handle_side_menu = function(a) {
    a("#menu-toggler").on(xh.click_event,
    function() {
        a("#sidebar").toggleClass("display");
        a(this).toggleClass("display");
        return false
    });
    var c = a("#sidebar").hasClass("menu-min");
    a("#sidebar-collapse").on(xh.click_event,
    function() {
        c = a("#sidebar").hasClass("menu-min");
        xh.settings.sidebar_collapsed(!c)
    });
    var b = navigator.userAgent.match(/OS (5|6|7)(_\d)+ like Mac OS X/i);
    a(".nav-list").on(xh.click_event,
    function(h) {
        var g = a(h.target).closest("a");
        if (!g || g.length == 0) {
            return
        }
        c = a("#sidebar").hasClass("menu-min");
        if (!g.hasClass("dropdown-toggle")) {
            if (c && xh.click_event == "tap" && g.get(0).parentNode.parentNode == this) {
                var i = g.find(".menu-text").get(0);
                if (h.target != i && !a.contains(i, h.target)) {
                    return false
                }
            }
            if (b) {
                document.location = g.attr("href");
                return false
            }
            return
        }
        var f = g.next().get(0);
        if (!a(f).is(":visible")) {
            var d = a(f.parentNode).closest("ul");
            if (c && d.hasClass("nav-list")) {
                return
            }
            d.find("> .open > .submenu").each(function() {
                if (this != f && !a(this.parentNode).hasClass("active")) {
                    a(this).slideUp(200).parent().removeClass("open")
                }
            })
        } else {}
        if (c && a(f.parentNode.parentNode).hasClass("nav-list")) {
            return false
        }
        a(f).slideToggle(200).parent().toggleClass("open");
        return false
    })
};
