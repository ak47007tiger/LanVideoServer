var fileListController = {
	dir : '',
	curListData : null,
	host : 'http://${serverIp}:8080/VideoServer/file/files?dir=',
	type : {
		dir : 0,
		normalFile : 1,
		video : 2
	},
	list : function() {
		var dir = encodeURI(fileListController.dir);
		console.log("list is run------------------");
		/*$.ajax({
				url : fileListController.host + dir,
				dataType: 'json',
				async:false,
				cache:false,
				success : function(data) {
					alert('get data');
					if (1 == data.code) {
						var div = $('#listContainer');
						div.empty();
						var ul = $('<ul  data-role="listview" data-inset="true"></ul>');
						fileListController.curListData = data.data;
						console.log(fileListController.curListData);
						fileListController.buildList(
								fileListController.curListData, ul);
						div.append(ul);
					} else {
						console.log('error');
					}
				},
				error : function() {
					alert('ajax error');
				}
			});*/
		$.getJSON(
				fileListController.host + dir,
				function(data) {
					if (1 == data.code) {
						var div = $('#listContainer');
						div.empty();
						var ul = $('<ul  data-role="listview" data-inset="true"></ul>');
						fileListController.curListData = data.data;
						fileListController.buildList(
								fileListController.curListData, ul);
						div.append(ul);
					} else {
						console.log('error');
					}
				});
	},
	buildList : function(listData, ul) {
		for (var i = 0; i < listData.length; i++) {
			var liItem = fileListController.buildItem(listData[i]);
			if (null != liItem) {
				liItem.on('click', fileListController.onLiClick);
				ul.append(liItem);
			}
		}
	},
	buildItem : function(itemData) {
		var type = fileListController.type;
		switch (itemData.type) {
		case type.dir:
			return fileListController.buildDirItem(itemData);
		case type.normalFile:
			return fileListController.buildNornamFileItem(itemData);
		case type.video:
			return fileListController.buildVideoItem(itemData);
		}
		return null;
	},
	buildDirItem : function(itemData) {
		var li_item = $('<li></li>');
		var a_link = $('<a href="javascript:void(0)" class="ui-link-inherit"></a>');
		var p_name = $('<p></p>');
		var p_attr = $('<p></p>');

		a_link.append(p_name);
		a_link.append(p_attr);
		li_item.append(a_link);

		p_name.text(itemData.name);
		p_attr.text('dir');
		return li_item;
	},
	buildVideoItem : function(itemData) {
		var li_item = $('<li></li>');
		var p_name = $('<p></p>');
		var a_link = $('<a href="javascript:void(0)"></a>');
		var p_size = $('<p></p>');
		var img_thumb = $('<img/>');

		li_item.append(a_link);
		a_link.append(img_thumb);
		a_link.append(p_name);
		a_link.append(p_size);

		var name = itemData.name;
		var size = itemData.size;
		var thumb = itemData.thumb;
		p_name.text(name);
		p_size.text(size);
		img_thumb.attr('src', thumb);
		return li_item;
	},
	buildNornamFileItem : function(itemData) {
		var li_item = $('<li></li>');
		var a_link = $('<a href="javascript:void(0)"></a>');
		var p_name = $('<p></p>');
		var p_size = $('<p></p>');
		a_link.append(p_name);
		a_link.append(p_size);
		li_item.append(a_link);
		p_name.text(itemData.name);
		p_size.text(itemData.size);
		return li_item;
	},
	onLiClick : function onLiClick() {
		var li = this;
		var ul = $('#listContainer ul');
		var lis = ul.children();
		for (var i = 0; i < lis.length; i++) {
			if (lis[i] == li) {
				break;
			}
		}
		var dataItem = fileListController.curListData[i];
		var type = fileListController.type;
		switch (dataItem.type) {
		case type.dir:
			fileListController.dir = dataItem.path;
			fileListController.list();
			break;
		case type.normalFile:
			console.log('normal file' + dataItem.path);
			break;
		case type.video:
			// call android to show video
			console.log(dataItem.path);
			if(window.appVideo){
				window.appVideo.PlayVideo(dataItem.path);
			}else{
				console.log('--------------can not use');
			}
			break;
		}
	},
	back : function(){
		var dir = fileListController.dir;
		if(fileListController.canBack()){
			dir = dir.substring(0, dir.lastIndexOf('/', dir.length));
		}else{
			dir = '';
		}
		console.log(dir);
		fileListController.dir = dir;
		fileListController.list();
	},
	canBack : function(){
		var dir = fileListController.dir;
		var index = dir.indexOf('/'); 
		var result;
		result = index < dir.length - 1;
		//有父目录并且可能有子目录
		if(0 < index && index < dir.length - 1){
			var childDir = dir.substring(index + 1, dir.length);
			var i;
			for(i = 0; i < dir.length; i++){
				//确定有子目录
				if(dir.charAt(i) != ''){
					return true;
				}
			}
		}
		return false;;
	}
}
$(document).ready(function() {
	fileListController.list();
	var backBtn = $('#backBtn');
	backBtn.on('click',function(){
		fileListController.back();
	});
});