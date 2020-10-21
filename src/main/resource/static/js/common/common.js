function exitLogin(){
	var content = "确定退出吗？";
	
	layer.open({
	    content: content,
	    btn: ['确定', '取消'],
	    yes: function(index){
			$.ajax({
					type: 'GET',
			    	dataType: "json",
			        url: "/user/loginout.json",
					async: false,
					timeout: 300000,
					beforeSend: function(){
						layer.open({
						type: 2,
						shadeClose: false,
					    content: '<span style=color:#fff>退出中</span>'
					});
					},
			        success: function(data){
			        	layer.closeAll();
			        	var msgcode = data.code,
			        		message = data.message;
			        	if(msgcode==1){
			        		window.location.href="/page/login.html";
			        	}else if(msgcode==2){
			        		layerMsg("退出失败！");
			        	}
			        },
					error: function(){
						layer.closeAll();
						layerMsg("网络出错！");
					}
				});
	    }
    });
}

/**
*得到题目的参数
**/
function GetRequest(child) {  
   var url = child.location.search; //获取url中"?"符后的字串  
   var theRequest = new Object();  
   if (url.indexOf("?") != -1) {  
      var str = url.substr(1);  
      strs = str.split("&");  
      for(var i = 0; i < strs.length; i ++) {  
         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);  
      }  
   }  
   return theRequest;  
}

function qiehuanTable(fromTpe){
	if(fromTpe == 1){
		window.location.href="../pagenew/grabListNew.html";
	}else if(fromTpe == 2){
		window.location.href="../page/orderRecordsList.html";
	}
}

