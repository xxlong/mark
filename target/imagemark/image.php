<?
$url=$_GET["url"];
$downfile=str_replace("http://","",$url);//Remove the http://
$urlarr=explode("/",$downfile);//With "/" decomposition of the domain name
$domain=$urlarr[0];//Domain name
$getfile=str_replace($urlarr[0],'',$downfile);//The header part of GET
$content = @fsockopen("$domain", 80, $errno, $errstr, 12);//To connect to the target host
if (!$content){//The link is not on the error
die("Sorry, unable to connect $domain . ");
} 
fputs($content, "GET $getfile HTTP/1.0rn");  
fputs($content, "Host: $domain\r\n");  
if($domain=="img3.laibafile.cn"){
echo("图片来自天涯");
fputs($content, "Referer: $domain\r\n");
}else{
echo("图片来自其他");
fputs($content, "Referer:  \r\n");
}
//The forged part
fputs($content, "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)rnrn");  
while (!feof($content)) {  
echo the fgets($content, 128);  
//if (strstr($tp,"200 OK")){ 
//header("Location:$url");  
//die();  
//}  
}  
//$arr=explode("n",$tp);  
//$arr1=explode("Location: ",$tp);//The decomposition of Location behind the true
//$arr2=explode("n",$arr1[1]);  
//header('Content-Type:image/jpeg');//Forced to download
//header("location:".$arr2[0]);//Towards the target
//die();
?>  