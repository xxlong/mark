 <?
   $url=$_GET['url'];
　　$ext=pathinfo($url,PATHINFO_EXTENSION);
　　if($ext!='jpg' && $ext!='gif'){ // 只支持jpg和gif
　　readfile('/upload/20081209130557536.gif');
　　exit;
　　}
　　$file=md5($url).'.'.$ext;
　　if(file_exists($file)){
　　readfile($file);
　　exit;
　　}else{
　　$data=file_get_contents($url);
　　if(!$data){ // 读取失败
　　readfile('/upload/20081209130557536.gif');
　　exit;
　　}
　　$handle=fopen($file,'w');
　　fwrite($handle,$data);
　　fclose($handle);
　　echo $data;
?>