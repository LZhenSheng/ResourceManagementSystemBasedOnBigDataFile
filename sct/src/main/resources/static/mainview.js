// layer弹窗
var v;
var s;
$('#mkdir').on('click',function(){
	s = $('#path').val();
	layer.prompt({title: '请输入文件夹名称'},function(val,index){
		var conf = true;//确认移动是否成功
		//post请求
		var targetpath = val;
		if (conf==true){
			layer.close(index);
			layer.msg('文件夹'+targetpath+'已经成功创建在'+s+'文件夹下');
		}
		else{
			layer.close(index);
		}
	});

});

$('.download').on('click', function(){ //下载文件
	v = $(this).val()
	console.log('下载按钮'+v);
	var downloadApi = v;

	var downloadPath = "https://down.qq.com/qqweb/PCQQ/PCQQ_EXE/PCQQ2021.exe";

	$(this).attr("href",downloadPath);

});
$('.move').on('click',function(){
	console.log('移动按钮'+$(this).val());
	v = $(this).val(); //
	layer.prompt({title: '输入目标路径并确认'},function(val,index){
		var conf = true;//确认移动是否成功
		//post请求
		var targrtpath = val;
		var orinpath = v; //起始路径



		if (conf==true){
			this.layer.close(index);
			this.layer.msg('文件'+orinpath+'已经成功移动到 '+val);
		}
		else{
			this.layer.close(index);
			this.layer.msg('移动失败');
		}
	});

});

//删除
$('.delete').on('click',function(){
	v = $(this).val();
	console.log('删除按钮'+$(this).val());
	layer.confirm('执行操作后将不可更改，您确定要删除该文件吗？',{
		btn:['确定','取消']
	},function(){
		//post请求

		layer.msg('文件'+v+'已经成功删除');

	},function(){
		layer.msg('已取消');
	});
});

$('.copy').on('click',function(){
	console.log('复制按钮'+$(this).val());
	v = $(this).val();
	layer.prompt({title: '输入目标路径并确认'},function(val,index){
		var conf = true;//确认复制是否成功
		//post请求
		var targrtpath = val;
		var orinpath = v; //起始路径

		if (conf==true){
			layer.close(index);
			layer.msg('文件'+orinpath+'已经成功复制到 '+val);
		}
		else{
			layer.close(index);
			layer.msg('复制失败');
		}
	});
});

$('.rename').on('click',function(){
	v = $(this).val();
	console.log('重命名按钮'+$(this).val());
	layer.prompt({title:'请输入'},function(val,index){
		var conf = true;//确认重命名是否成功
		var targrtpath = val; //要修改成的名字
		var orinpath = v;  //原名称 (绝对路径)
		//post请求 (要修改成的名字)

		if (conf==true){
			layer.close(index);
			layer.msg('文件'+orinpath+'已重命名为'+targrtpath);
		}
		else{
			layer.close(index);
			layer.msg('重命名失败');
		}
	});
});

var upload = layui.upload; //得到 upload 对象
var uploadApi='/api';
layui.use('upload', function(){
	var upload = layui.upload;

	//执行实例
	var uploadInst = upload.render({
		elem: '#upload' //绑定元素
		,url: uploadApi //上传接口
		//	,method:		//HTTP请求类型 默认post
		//	,headers:        //请求头
		,accept:'file'
		,before: function(obj){

		}
		,choose: function(obj){

			console.log(obj);
			console.log(obj.pushFile());

			var filename= $('#upload').val();
			console.log(filename)
			console.log(obj);
			obj.preview(function(index,file,result){
				console.log(file);
			});


		}
		,done: function(res){
			console.log(uploadApi);
			//上传完毕回调
		}
		,error: function(){
			console.log(uploadApi);
			//请求异常回调
		}
	});

});