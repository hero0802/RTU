/**
 * 
 */
if (! ("xh" in window)) {
    window.xh = {}
}
xh.config = {
    cookie_expiry: 604800,
    storage_method: 2
};
xh.settings = {
    is: function(b, a) {
        return (xh.data.get("settings", b + "-" + a) == 1)
    },
    exists: function(b, a) {
        return (xh.data.get("settings", b + "-" + a) !== null)
    },
    set: function(b, a) {
        xh.data.set("settings", b + "-" + a, 1)
    },
    unset: function(b, a) {
        xh.data.set("settings", b + "-" + a, -1)
    },
    remove: function(b, a) {
        xh.data.remove("settings", b + "-" + a)
    },
    navbar_fixed: function(a) {
        a = a || false;
        if (!a && xh.settings.is("sidebar", "fixed")) {
            xh.settings.sidebar_fixed(false)
        }
        var b = document.getElementById("navbar");
        if (a) {
            if (!xh.hasClass(b, "navbar-fixed-top")) {
                xh.addClass(b, "navbar-fixed-top")
            }
            if (!xh.hasClass(document.body, "navbar-fixed")) {
                xh.addClass(document.body, "navbar-fixed")
            }
            xh.settings.set("navbar", "fixed")
        } else {
            xh.removeClass(b, "navbar-fixed-top");
            xh.removeClass(document.body, "navbar-fixed");
            xh.settings.unset("navbar", "fixed")
        }
        document.getElementById("xh-settings-navbar").checked = a
    },
    breadcrumbs_fixed: function(a) {
        a = a || false;
        if (a && !xh.settings.is("sidebar", "fixed")) {
            xh.settings.sidebar_fixed(true)
        }
        var b = document.getElementById("breadcrumbs");
        if (a) {
            if (!xh.hasClass(b, "breadcrumbs-fixed")) {
                xh.addClass(b, "breadcrumbs-fixed")
            }
            if (!xh.hasClass(document.body, "breadcrumbs-fixed")) {
                xh.addClass(document.body, "breadcrumbs-fixed")
            }
            xh.settings.set("breadcrumbs", "fixed")
        } else {
            xh.removeClass(b, "breadcrumbs-fixed");
            xh.removeClass(document.body, "breadcrumbs-fixed");
            xh.settings.unset("breadcrumbs", "fixed")
        }
        document.getElementById("xh-settings-breadcrumbs").checked = a
    },
    sidebar_fixed: function(a) {
        a = a || false;
        if (!a && xh.settings.is("breadcrumbs", "fixed")) {
            xh.settings.breadcrumbs_fixed(false)
        }
        if (a && !xh.settings.is("navbar", "fixed")) {
            xh.settings.navbar_fixed(true)
        }
        var b = document.getElementById("sidebar");
        if (a) {
            if (!xh.hasClass(b, "sidebar-fixed")) {
                xh.addClass(b, "sidebar-fixed")
            }
            xh.settings.set("sidebar", "fixed")
        } else {
            xh.removeClass(b, "sidebar-fixed");
            xh.settings.unset("sidebar", "fixed")
        }
        document.getElementById("xh-settings-sidebar").checked = a
    },
    main_container_fixed: function(a) {
        a = a || false;
        var c = document.getElementById("main-container");
        var b = document.getElementById("navbar-container");
        if (a) {
            if (!xh.hasClass(c, "container")) {
                xh.addClass(c, "container")
            }
            if (!xh.hasClass(b, "container")) {
                xh.addClass(b, "container")
            }
            xh.settings.set("main-container", "fixed")
        } else {
            xh.removeClass(c, "container");
            xh.removeClass(b, "container");
            xh.settings.unset("main-container", "fixed")
        }
        document.getElementById("xh-settings-add-container").checked = a;
        if (navigator.userAgent.match(/webkit/i)) {
            var d = document.getElementById("sidebar");
            xh.toggleClass(d, "menu-min");
            setTimeout(function() {
                xh.toggleClass(d, "menu-min")
            },
            0)
        }
    },
    sidebar_collapsed: function(c) {
        c = c || false;
        var e = document.getElementById("sidebar");
        var d = document.getElementById("sidebar-collapse").querySelector('[class*="fa-"]');
        var b = d.getAttribute("data-icon1");
        var a = d.getAttribute("data-icon2");
        var w=document.getElementById("wrapper");
        var bool=$("wrapper").hasClass("wrapper");
        if (c) {
            xh.addClass(e, "menu-min");
            xh.removeClass(d, b);
            xh.addClass(d, a);
            xh.settings.set("sidebar", "collapsed");
            $("#wrapper").removeClass("wrapper");
            $("#wrapper").toggleClass("wrapper-collapse");
           /* if(bool){
            	$("#wrapper").removeClass("wrapper");
            	$("#wrapper").addClass("wrapper-collapse");
            }else{
            	$("#wrapper").toggleClass("wrapper-collapse");
            }*/
           
        } else {
            xh.removeClass(e, "menu-min");
            xh.removeClass(d, a);
            xh.addClass(d, b);
            xh.settings.unset("sidebar", "collapsed");
            $("#wrapper").removeClass("wrapper-collapse");
            $("#wrapper").toggleClass("wrapper");
            
        }
    },
};
xh.settings.check = function(c, e) {
    if (!xh.settings.exists(c, e)) {
        return
    }
    var a = xh.settings.is(c, e);
    var b = {
        "navbar-fixed": "navbar-fixed-top",
        "sidebar-fixed": "sidebar-fixed",
        "breadcrumbs-fixed": "breadcrumbs-fixed",
        "sidebar-collapsed": "menu-min",
        "main-container-fixed": "container"
    };
    var d = document.getElementById(c);
    if (a != xh.hasClass(d, b[c + "-" + e])) {
        xh.settings[c.replxh("-", "_") + "_" + e](a)
    }
};
xh.data_storage = function(e, c) {
    var b = "xh.";
    var d = null;
    var a = 0;
    if ((e == 1 || e === c) && "localStorage" in window && window.localStorage !== null) {
        d = xh.storage;
        a = 1
    } else {
        if (d == null && (e == 2 || e === c) && "cookie" in document && document.cookie !== null) {
            d = xh.cookie;
            a = 2
        }
    }
    this.set = function(h, g, i, k) {
        if (!d) {
            return
        }
        if (i === k) {
            i = g;
            g = h;
            if (i == null) {
                d.remove(b + g)
            } else {
                if (a == 1) {
                    d.set(b + g, i)
                } else {
                    if (a == 2) {
                        d.set(b + g, i, xh.config.cookie_expiry)
                    }
                }
            }
        } else {
            if (a == 1) {
                if (i == null) {
                    d.remove(b + h + "." + g)
                } else {
                    d.set(b + h + "." + g, i)
                }
            } else {
                if (a == 2) {
                    var j = d.get(b + h);
                    var f = j ? JSON.parse(j) : {};
                    if (i == null) {
                        delete f[g];
                        if (xh.sizeof(f) == 0) {
                            d.remove(b + h);
                            return
                        }
                    } else {
                        f[g] = i
                    }
                    d.set(b + h, JSON.stringify(f), xh.config.cookie_expiry)
                }
            }
        }
    };
    this.get = function(h, g, j) {
        if (!d) {
            return null
        }
        if (g === j) {
            g = h;
            return d.get(b + g)
        } else {
            if (a == 1) {
                return d.get(b + h + "." + g)
            } else {
                if (a == 2) {
                    var i = d.get(b + h);
                    var f = i ? JSON.parse(i) : {};
                    return g in f ? f[g] : null
                }
            }
        }
    };
    this.remove = function(g, f, h) {
        if (!d) {
            return
        }
        if (f === h) {
            f = g;
            this.set(f, null)
        } else {
            this.set(g, f, null)
        }
    }
};
xh.cookie = {
    get: function(c) {
        var d = document.cookie,
        g, f = c + "=",
        a;
        if (!d) {
            return
        }
        a = d.indexOf("; " + f);
        if (a == -1) {
            a = d.indexOf(f);
            if (a != 0) {
                return null
            }
        } else {
            a += 2
        }
        g = d.indexOf(";", a);
        if (g == -1) {
            g = d.length
        }
        return decodeURIComponent(d.substring(a + f.length, g))
    },
    set: function(b, e, a, g, c, f) {
        var h = new Date();
        if (typeof(a) == "object" && a.toGMTString) {
            a = a.toGMTString()
        } else {
            if (parseInt(a, 10)) {
                h.setTime(h.getTime() + (parseInt(a, 10) * 1000));
                a = h.toGMTString()
            } else {
                a = ""
            }
        }
        document.cookie = b + "=" + encodeURIComponent(e) + ((a) ? "; expires=" + a: "") + ((g) ? "; path=" + g: "") + ((c) ? "; domain=" + c: "") + ((f) ? "; secure": "")
    },
    remove: function(a, b) {
        this.set(a, "", -1000, b)
    }
};
xh.storage = {
    get: function(a) {
        return window.localStorage.getItem(a)
    },
    set: function(a, b) {
        window.localStorage.setItem(a, b)
    },
    remove: function(a) {
        window.localStorage.removeItem(a)
    }
};
xh.sizeof = function(c) {
    var b = 0;
    for (var a in c) {
        if (c.hasOwnProperty(a)) {
            b++
        }
    }
    return b
};
xh.hasClass = function(b, a) {
    return (" " + b.className + " ").indexOf(" " + a + " ") > -1
};
xh.addClass = function(c, b) {
    if (!xh.hasClass(c, b)) {
        var a = c.className;
        c.className = a + (a.length ? " ": "") + b
    }
};
xh.removeClass = function(b, a) {
    xh.replaceClass(b, a)
};
xh.replaceClass = function(c, b, d) {
    var a = new RegExp(("(^|\\s)" + b + "(\\s|$)"), "i");
    c.className = c.className.replace(a,
    function(e, g, f) {
        return d ? (g + d + f) : " "
    }).replace(/^\s+|\s+$/g, "")
};
xh.toggleClass = function(b, a) {
    if (xh.hasClass(b, a)) {
        xh.removeClass(b, a)
    } else {
        xh.addClass(b, a)
    }
};
xh.data = new xh.data_storage(xh.config.storage_method);