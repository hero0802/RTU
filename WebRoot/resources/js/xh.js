$(document).ready(function() {
	
	

	/* 初始化页面加载动画 */
	$(window).on('load', function() {
		$('.splash').css('display', 'none');
	})
	
	setBodySmall();
	
	 // Handle minimalize sidebar menu
    $('.hide-menu').on('click', function(event){
        event.preventDefault();
        if ($(window).width() < 769) {
            $("body").toggleClass("show-sidebar");
        } else {
            $("body").toggleClass("hide-sidebar");
        }
    });
    /*返回到顶部*/
    $(window).scroll(function(){
        //获取窗口的滚动条的垂直位置
        var s = $(window).scrollTop();
        //当窗口的滚动条的垂直位置大于页面的最小高度时，让返回顶部元素渐现，否则渐隐
        if( s >100){
            $(".scroll-top").fadeIn(100);
        }else{
            $(".scroll-top").fadeOut(200);
        };
    });
    $(".scroll-top").click(function(){
    	$('html,body').animate({scrollTop:0},300);
    });

	// MASKED INPUT
	// =================================================================
	// Require Masked Input
	// http://digitalbush.com/projects/masked-input-plugin/
	// =================================================================


	// Initialize Masked Inputs
	// a - Represents an alpha character (A-Z,a-z)
	// 9 - Represents a numeric character (0-9)
	// * - Represents an alphanumeric character (A-Z,a-z,0-9)
/*	$('#demo-msk-date').mask('99/99/9999');
	$('#demo-msk-date2').mask('99-99-9999');
	$('#demo-msk-phone').mask('(999) 999-9999');
	$('#demo-msk-phone-ext').mask('(999) 999-9999? x99999');
	$('#demo-msk-taxid').mask('99-9999999');
	$('#demo-msk-ssn').mask('999-99-9999');
	$('#demo-msk-pkey').mask('a*-999-a999');*/
	
	   // Initialize animate panel function
    $('.animate-panel').animatePanel();

	// Function for collapse hpanel
	$('.showhide').on('click', function(event) {
		event.preventDefault();
		var hpanel = $(this).closest('div.hpanel');
		var icon = $(this).find('i:first');
		var body = hpanel.find('div.panel-body');
		var footer = hpanel.find('div.panel-footer');
		body.slideToggle(200);
		footer.slideToggle(200);

		// Toggle icon from up to down
		icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
		hpanel.toggleClass('').toggleClass('panel-collapse');
		setTimeout(function() {
			hpanel.resize();
			hpanel.find('[id^=map-]').resize();
		}, 50);
	});

	// Function for close hpanel
	$('.closebox').on('click', function(event) {
		event.preventDefault();
		var hpanel = $(this).closest('div.hpanel');
		hpanel.remove();
		if ($('body').hasClass('fullscreen-panel-mode')) {
			$('body').removeClass('fullscreen-panel-mode');
		}
	});
	 // Fullscreen for fullscreen hpanel
    $('.fullscreen').on('click', function() {
        var hpanel = $(this).closest('div.hpanel');
        var icon = $(this).find('i:first');
        $('body').toggleClass('fullscreen-panel-mode');
        icon.toggleClass('fa-expand').toggleClass('fa-compress');
        hpanel.toggleClass('fullscreen');
        setTimeout(function() {
            $(window).trigger('resize');
        }, 100);
    });
	 // Function for small header
    $('.small-header-action').on('click', function(event){
        event.preventDefault();
        var icon = $(this).find('i:first');
        var breadcrumb  = $(this).parent().find('#hbreadcrumb');
        $(this).parent().parent().parent().toggleClass('small-header');
        $(this).parent().parent().parent().toggleClass('normalheader');
        breadcrumb.toggleClass('m-t-lg');
        icon.toggleClass('fa-arrow-up').toggleClass('fa-arrow-down');
    });
    // Set minimal height of #wrapper to fit the window
    setTimeout(function () {
        fixWrapperHeight();
    });
    // Handle minimalize sidebar menu
/*    $('.hide-menu').on('click', function(event){
        event.preventDefault();
        if ($(window).width() < 769) {
            $("body").toggleClass("show-sidebar");
        } else {
            $("body").toggleClass("hide-sidebar");
       
        }
    });*/
    //tooltip
    $("[data-toggle='tooltip']").tooltip();
    $("[data-toggle='popover']").popover();
   /* dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setAsync(false);//同步步
	//设置在页面关闭时，通知服务端销毁会话
    dwr.engine.setNotifyServerOnPageUnload( true);
	xh.dwr();
	dwr.engine.setErrorHandler(function(){
		window.location.href="/RTU/index.html"
		
	})*/
});

function fixWrapperHeight() {

    // Get and set current height
    var headerH = 62;
    var navigationH = $("#navigation").height();
    var contentH = $(".content").height();

    // Set new height when contnet height is less then navigation
    if (contentH < navigationH) {
        $("#wrapper").css("min-height", navigationH + 'px');
    }

    // Set new height when contnet height is less then navigation and navigation is less then window
    if (contentH < navigationH && navigationH < $(window).height()) {
        $("#wrapper").css("min-height", $(window).height() - headerH  + 'px');
    }

    // Set new height when contnet is higher then navigation but less then window
    if (contentH > navigationH && contentH < $(window).height()) {
        $("#wrapper").css("min-height", $(window).height() - headerH + 'px');
    }
}

//隐藏左侧菜单

function setBodySmall() {
    if ($(this).width() < 769) {
        $('body').addClass('page-small');
    } else {
        $('body').removeClass('page-small');
        $('body').removeClass('show-sidebar');
    }
}
//Animate panel function
$.fn['animatePanel'] = function() {

    var element = $(this);
    var effect = $(this).data('effect');
    var delay = $(this).data('delay');
    var child = $(this).data('child');

    // Set default values for attrs
    if(!effect) { effect = 'zoomIn'}/*fadeInLeftBig ,fadeInUpBig*/
    if(!delay) { delay = 0.06 } else { delay = delay / 10 }
    if(!child) { child = '.row > div'} else {child = "." + child}

    //Set defaul values for start animation and delay
    var startAnimation = 0;
    var start = Math.abs(delay) + startAnimation;

    // Get all visible element and set opacity to 0
    var panel = element.find(child);
    panel.addClass('opacity-0');

    // Get all elements and add effect class
    panel = element.find(child);
    panel.addClass('stagger').addClass('animated-panel').addClass(effect);

    var panelsCount = panel.length + 10;
    var animateTime = (panelsCount * delay * 10000) / 10;

    // Add delay for each child elements
    panel.each(function (i, elm) {
        start += delay;
        var rounded = Math.round(start * 10) / 10;
        $(elm).css('animation-delay', rounded + 's');
        // Remove opacity 0 after finish
        $(elm).removeClass('opacity-0');
    });

    // Clear animation after finish
    setTimeout(function(){
        $('.stagger').css('animation', '');
        $('.stagger').removeClass(effect).removeClass('animated-panel').removeClass('stagger');
    }, animateTime)

};
