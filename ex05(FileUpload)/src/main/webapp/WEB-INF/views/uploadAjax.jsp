<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
}

.uploadResult ul li img {
	width: 100px;
}
</style>
<body>

<style>
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
}

.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
</style>

<div class='bigPictureWrapper'>
  <div class='bigPicture'>
  </div>
</div>

	<h1>Upload with Ajax</h1>
	
	<div class='uploadDiv'>
		<input type="file" name="uploadFile" multiple> 
	</div>
	
	<div class='uploadResult'>
		<ul>

		</ul>
	</div>
	
	
	<button id='uploadBtn'>upload</button>
	
		<script src="https://code.jquery.com/jquery-3.3.1.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>



<script> <!--Ajax 를 이용한 업로드 처리-->
var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
var maxSize = 5242880; //5MB
var cloneObj = $(".uploadDiv").clone();

function showImage(fileCallPath){
	  
	  alert(fileCallPath);
	
	  $(".bigPictureWrapper").css("display","flex").show();
	  
	  $(".bigPicture")
	  .html("<img src='/display?fileName="+ encodeURI(fileCallPath)+"'>")
	  .animate({width:'100%', height: '100%'}, 1000);

	}

//파일 확장자 or 크기 확인
function checkExtension(fileName, fileSize) {
	if (fileSize >= maxSize) {
		alert("파일 사이즈 초과");
		return false;
	}

	if (regex.test(fileName)) {
		alert("해당 종류의 파일은 업로드할 수 없습니다.");
		return false;
	}
	return true;
}

var uploadResult = $(".uploadResult ul");

function showUploadedFile(uploadResultArr) {

	 var str = "";
	
	 $(uploadResultArr).each(function(i, obj) {
	
	 str += "<li>" + obj.fileName + "</li>";
	
	 });
	
	 uploadResult.append(str);
 } 

$(document).ready(function(){
	$("#uploadBtn").on("click", function(e){
	
		 var formData = new FormData();
		
		 var inputFile = $("input[name='uploadFile']");
		
		 var files = inputFile[0].files;
		
		 console.log(files);
		 
		 for(var i = 0; i < files.length; i++){
			 
			if (!checkExtension(files[i].name, files[i].size)) {   //위배되는것이 있으면 false
				return false;
			}
				
			 formData.append("uploadFile", files[i]);
		 }
		 
		 $.ajax({
			 url: '/uploadAjaxAction',
			 processData: false,
			 contentType: false,
			 data: formData,
			 type:'POST',
			 dataType : 'json',
			 success : function(result) {

				console.log(result);

				showUploadedFile(result);

				$(".uploadDiv").html(cloneObj.html());

			}
		 }); 
		 
	});
});	

function showUploadedFile(uploadResultArr){
    
    var str = "";
    
    $(uploadResultArr).each(function(i, obj){
      
      if(!obj.image){
    	  
        var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);
          
        var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
          
        str += "<li><div><a href='/download?fileName="+fileCallPath+"'>"+
              "<img src='/resources/img/attach.png'>"+obj.fileName+"</a>"+
              "<span data-file=\'"+fileCallPath+"\' data-type='file'> x </span>"+
              "<div></li>"
        console.log("이미지가 아니다");
      }else{
        //str += "<li>"+ obj.fileName+"</li>";
        
       var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
       
       var originPath = obj.uploadPath+ "\\"+obj.uuid +"_"+obj.fileName;
       
       originPath = originPath.replace(new RegExp(/\\/g),"/");
       
       str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+
              "<img src='display?fileName="+fileCallPath+"'></a>"+
              "<span data-file=\'"+fileCallPath+"\' data-type='image'> x </span>"+
              "<li>";
              
        console.log("이미지다");
      }
    });
    
    uploadResult.append(str);
  }

$(".uploadResult").on("click","span", function(e){
	   
	  var targetFile = $(this).data("file");
	  var type = $(this).data("type");
	  console.log(targetFile);
	  
	  $.ajax({
	    url: '/deleteFile',
	    data: {fileName: targetFile, type:type},
	    dataType:'text',
	    type: 'POST',
	      success: function(result){
	         alert(result);
	         window.reload;
	       }
	  });
});

</script>



	
</body>
</html>