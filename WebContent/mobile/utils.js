var _marsutils = {
	isEmptyString : function(str){
		if(null == str){
			return true;
		}
		if(0 == str.length){
			return true;
		}
		return false;
	}
}