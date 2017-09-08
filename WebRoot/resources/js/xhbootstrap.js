if (!("xh" in window)) {
	window.xh = {}
}
(function(){
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/jquery-2.2.3.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/angular.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/static/bootstrap-3.3.6/js/bootstrap.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/bootstrapValidator.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/jquery.maskedinput.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/paging.js"></script>');
/*document.write('<script type="text/javascript" src="'+getRootPath()+'/dwr/util.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/dwr/engine.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/dwr/interface/TcpDwr.js"></script>');*/
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/xh.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/js/xh-controller.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/static/sweetalert/lib/sweet-alert.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/static/toastr/build/toastr.min.js"></script>');
document.write('<script type="text/javascript" src="'+getRootPath()+'/resources/static/My97DatePicker/WdatePicker.js"></script>');


document.write('<link rel="stylesheet" type="text/css" href="'+getRootPath()+'/resources/css/style.css">');
document.write('<link rel="stylesheet" type="text/css" href="'+getRootPath()+'/resources/static/sweetalert/lib/sweet-alert.css">');
document.write('<link rel="stylesheet" type="text/css" href="'+getRootPath()+'/resources/static/toastr/build/toastr.min.css">');
document.write('<link rel="stylesheet" type="text/css" href="'+getRootPath()+'/resources/css/style.css">');
document.write('<link rel="stylesheet" type="text/css" href="'+getRootPath()+'/resources/css/style.css">');
})()



function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}