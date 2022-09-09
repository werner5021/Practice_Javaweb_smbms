var oldpassword = null;
var newpassword = null;
var rnewpassword = null;
var saveBtn = null;

$(function(){
	oldpassword = $("#oldpassword");
	newpassword = $("#newpassword");
	rnewpassword = $("#rnewpassword");
	saveBtn = $("#save");

	oldpassword.next().html("*");
	newpassword.next().html("*");
	rnewpassword.next().html("*");

	oldpassword.on("blur",function(){
		$.ajax({
			type:"GET",
			url:path+"/jsp/user.do",
			data:{method:"pwdmodify",oldpassword:oldpassword.val()},  //ajax傳遞的參數
			//相當於 path+"/jsp/user.do?method=pwdmodify&oldpassword=oldpassword.val()
			dataType:"json",  //主流的開發都使用json實現前後端交互
			success:function(data){
				if(data.result == "true"){//旧密码正确
					validateTip(oldpassword.next(),{"color":"green"},imgYes,true);
				}else if(data.result == "false"){//旧密码输入不正确
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 原密碼輸入不正確",false);
				}else if(data.result == "sessionerror"){//当前用户session过期，请重新登录
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 當前用戶session過期，請重新登入",false);
				}else if(data.result == "error"){//旧密码输入为空
					validateTip(oldpassword.next(),{"color":"red"},imgNo + " 請輸入舊密碼",false);
				}
			},
			error:function(data){
				//请求出错
				validateTip(oldpassword.next(),{"color":"red"},imgNo + "請求錯誤",false);
			}
		});


	}).on("focus",function(){
		validateTip(oldpassword.next(),{"color":"#666666"},"* 請輸入原密碼",false);
	});

	newpassword.on("focus",function(){
		validateTip(newpassword.next(),{"color":"#666666"},"* 密碼長度需大於6且小於20",false);
	}).on("blur",function(){
		if(newpassword.val() != null && newpassword.val().length > 5
			&& newpassword.val().length < 20 ){
			validateTip(newpassword.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(newpassword.next(),{"color":"red"},imgNo + " 密碼輸入格式有誤，請重新輸入",false);
		}
	});


	rnewpassword.on("focus",function(){
		validateTip(rnewpassword.next(),{"color":"#666666"},"* 請輸入與上面一致的密碼",false);
	}).on("blur",function(){
		if(rnewpassword.val() != null && rnewpassword.val().length > 5
			&& rnewpassword.val().length < 20 && newpassword.val() == rnewpassword.val()){
			validateTip(rnewpassword.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(rnewpassword.next(),{"color":"red"},imgNo + " 確認密碼需與新密碼一致，請重新輸入",false);
		}
	});


	saveBtn.on("click",function(){
		oldpassword.blur();
		newpassword.blur();
		rnewpassword.blur();
		if(oldpassword.attr("validateStatus") == "true"
			&& newpassword.attr("validateStatus") == "true"
			&& rnewpassword.attr("validateStatus") == "true"){
			if(confirm("确定要修改密码？")){
				$("#userForm").submit();
			}
		}

	});
});