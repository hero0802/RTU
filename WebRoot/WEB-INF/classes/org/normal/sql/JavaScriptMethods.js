//返回对硬件的操作命令
function execute(typeid, addr,regaddr,index){
	var s ;
	if(typeid == 1){
		s=addr+"03";
	}
	else if(typeid == 2 && index ==0 ){
		s=addr+"10"+regaddr+"0004080001000100010001";
	}
	else if (typeid == 2 && index!=0){
		s=addr+"03"+regaddr+"0004"+"0001000100010001";
	}
	s+="0000";
  return s;
};

function ToModbusCRC16(s){
	return CRC.toString(CRC.CRC16(CRC.strToHex(str)));
};
function CRC16 (data) {
    var len = data.length;
    if (len > 0) {
        var crc = 0xFFFF;

        for (var i = 0; i < len; i++) {
            crc = (crc ^ (data[i]));
            for (var j = 0; j < 8; j++) {
                crc = (crc & 1) != 0 ? ((crc >> 1) ^ 0xA001) : (crc >> 1);
            }
        }
        var hi = ((crc & 0xFF00) >> 8);  //高位置
        var lo = (crc & 0x00FF);         //低位置

        return [hi, lo];
    }
    return [0, 0];
};
  function strToHex(hex) {
    //清除所有空格
    hex = hex.replace(/\s/g, "");
    //若字符个数为奇数，补一个空格
    hex += hex.length % 2 != 0 ? " " : "";

    var c = hex.length / 2, arr = [];
    for (var i = 0; i < c; i++) {
        arr.push(parseInt(hex.substr(i * 2, 2), 16));
    }
    return arr;
};
  function toString (arr) {
    var hi = arr[0], lo = arr[1];
    return CRC.padLeft((hi + lo * 0x100).toString(16).toUpperCase(), 4, '0');
};
 function padLeft(s, w, pc) {
    for (var i = 0, c = w - s.length; i < c; i++) {
        s = pc + s;
    }
    return s;
};