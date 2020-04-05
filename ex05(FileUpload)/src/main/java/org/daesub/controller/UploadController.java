package org.daesub.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.daesub.domain.AttachFileDTO;
import org.daesub.util.Constant;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

/*
 * 파일 업로드시 고려할 상황
 * 1.동일한 이름으로 파일 업로드 할 경우 기존 파일이 사라진다.           >> UUID 를 이용한다.
 * 2.이미지 파일의 경우에는 원본 파일의 용량이 큰 경우 섬네일 이미지 생성      >> pom.xml 에서 thumbnailator 추가
 * 3.이미지파일과 일반 파일을 분류해서 다운로드 혹은 조회하도록 처리
 * 4.첨부파일에 공격에 대한 확장자 제한
 */

@Controller
@Log4j
public class UploadController {
	//해당일 폴더 생성 (해당일자 생성)
	//한 폴더 많이 생성될 경우 속도의 저하와 개수 제한 문제 해결
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date= new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	//이미지 파일 확인
	private boolean checkImageType(File file) {
	
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");  //이미지 파일 체크
			
		} catch (Exception e) {
			log.info("-----------------------------------------");
			log.info("Not Image but no problems"); 
			log.info("-----------------------------------------");
			e.printStackTrace();
			

		}
		return false;
	}
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		
		log.info("업로드 Form");
	}
	
	 @PostMapping("/uploadFormAction")
	 public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
	
		 String uploadFolder=Constant.UploadPath;  //파일 저장 루트 
		 
		 for (MultipartFile multipartFile : uploadFile) {   //다중파일 업로드 
			
			 log.info("-------------------------------------");
			 log.info("Upload File Name: " +multipartFile.getOriginalFilename());
			 log.info("Upload File Size: " +multipartFile.getSize());
			 log.info("-------------------------------------");
			 
			 File saveFile =new File(uploadFolder, multipartFile.getOriginalFilename());  //파일 생성자를 만들고 (파일경로와 저장된 파일 이름을 같이 저장한다.)
			 
			 try {
				multipartFile.transferTo(saveFile); //파일 저장
				log.info("저장완료 :"+ multipartFile.getOriginalFilename());  //오류일 경우 에러 메시지 
			} catch (Exception e) {
				log.error(e.getMessage());  //오류일 경우 에러 메시지 
			}
		 }
	 }
	 
	 @GetMapping("/uploadAjax")
	 public void uploadAjax() {
		 
		 log.info("upload ajax");
	 }
	 
	 //TEST
	 @PostMapping("/uploadTestAjaxAction") 
	 public void uploadAjaxAction(MultipartFile[] uploadFile) {
		 
		 String uploadFolder=Constant.UploadPath ; //파일 저장 루트 
		 
		 //폴더 생성
		 File uploadPath = new File(uploadFolder,getFolder());
		 log.info("upload path : " + uploadPath);	 
		 //해당 폴더가 존재할 경우 생성 안하고 넘어감
		 if(uploadPath.exists()==false) {  
			 uploadPath.mkdirs();
		 }
		 
		 for (MultipartFile multipartFile : uploadFile) {
			 
			 log.info("-------------------------------------");
			 log.info("Upload File Name: " +multipartFile.getOriginalFilename());
			 log.info("Upload File Size: " +multipartFile.getSize());
			 
			 String uploadFileName =multipartFile.getOriginalFilename();
			 
			 //IE로 전송할 경우 전체 파일 경로가 저장 되기 떄문에 마지막 \ 기준으로 잘라내면  == 실제 파일이름 이 된다
			 uploadFileName =uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);  
			 
			 log.info("only file name: " + uploadFileName);
			 
			 //UUID 생성
			 UUID uuid=UUID.randomUUID();			 
			 //파일 이름과 구분 하기 위해서 "_" 추가  
			 uploadFileName=uuid.toString() + "_" +uploadFileName;
			 
			 log.info("file name overlap remove: " + uploadFileName);
			 
			 try {
				 
				 File saveFile =new File(uploadPath, uploadFileName);  //저장할 파일 생성자
				 multipartFile.transferTo(saveFile);   // 저장
				 
				 if(checkImageType(saveFile)) {
					 /*  이미지 파일일 경우에 썸네일 생성 */
					 //썸네일 생성자 생성 
					 FileOutputStream thumnail = new FileOutputStream(new File(uploadPath,"s_"+ uploadFileName));
					 //createThumbnail(실제파일,파일 확장자 명, 넓이 , 크기)
					 Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumnail,100,100);
					 thumnail.close();
				 }
				 
			} catch (Exception e) {
				log.error(e.getMessage()); 
			}
		 }
	 }
	 
		@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		@ResponseBody
		public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
			
			List<AttachFileDTO> list =new ArrayList<>();
			//파일 저장 루트 
			String uploadFolder=Constant.UploadPath;
			//파일 폴더 생성
			String uploadFolderPath= getFolder();
			//저장소 생성
			File uploadPath = new File(uploadFolder,uploadFolderPath);
			//해당일 파일 생성 (존재하면 pass)
			if(uploadPath.exists() ==false) {
				uploadPath.mkdirs();
			}
			
			for(MultipartFile multipartFile : uploadFile) {
				
				AttachFileDTO attachDTO =new AttachFileDTO();
				
				String uploadFileName = multipartFile.getOriginalFilename();
				
				 //IE로 전송할 경우 전체 파일 경로가 저장 되기 떄문에 마지막 \ 기준으로 잘라내면  == 실제 파일이름 이 된다
				 uploadFileName =uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);  
				 log.info("only file name: " + uploadFileName);
				 
				 //파일이릅을 넣어준다.
				 attachDTO.setFileName(uploadFileName);
				 
				 //UUID 생성
				 UUID uuid=UUID.randomUUID();			 
				 //파일 이름과 구분 하기 위해서 "_" 추가  
				 uploadFileName=uuid.toString() + "_" +uploadFileName;
				 
				 log.info("file name overlap remove: " + uploadFileName);
				 
				 //uuid 와 파일 경로를 넣어준다. (파일경로는 상수로 적었기에 폴더 이름만 적는다.)
				 attachDTO.setUuid(uuid.toString());
				 attachDTO.setUploadPath(uploadFolderPath);
				 
				 try {
					 File saveFile =new File(uploadPath, uploadFileName);  //저장할 파일 생성자
					 multipartFile.transferTo(saveFile);   // 저장
					 
					 if(checkImageType(saveFile)) {
						 
						 attachDTO.setImage(true); 
						 /*  이미지 파일일 경우에 썸네일 생성 */
						 //썸네일 생성자 생성 
						 FileOutputStream thumnail = new FileOutputStream(new File(uploadPath,"s_"+ uploadFileName));
						 //createThumbnail(실제파일,파일 확장자 명, 넓이 , 크기)
						 Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumnail,100,100);
						 thumnail.close();
					 }
				
					 list.add(attachDTO); 
					 
					 log.info("addtachDTO :" +list);
					 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return new ResponseEntity<>(list, HttpStatus.OK);  
		}
	 
		//썸네일 보여주기
		@GetMapping("/display")
		@ResponseBody
		public ResponseEntity<byte[]>getFile(String fileName){
			
			log.info("fileName :"+fileName);

			//FileName을 파라미터로 byte[] 받고 전송 
			File file= new File(Constant.UploadPath+"//"+fileName); 
			
			log.info("file :" +file);
			
			ResponseEntity<byte[]> result =null;
			
			try {
				HttpHeaders headers= new HttpHeaders();
				headers.add("Content-Type", Files.probeContentType(file.toPath())); //파일의 종류에 따라 MIME 타입이 달라지기에  헤더 메시지에 포함 할 수 있도록 처리
				result= new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers,HttpStatus.OK);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}

		@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
		@ResponseBody
		public ResponseEntity<Resource>downloadFile(@RequestHeader("User-Agent")String userAgent ,String fileName){
			
			log.info("download file:"+ fileName);
			
			//해당 경로에는 리소스(파일) 을 읽어온다.
			Resource resource = new FileSystemResource(Constant.UploadPath+'\\' +fileName); 
			
			log.info("resouce : "+resource);
			
			if(resource.exists() ==false) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			String resourceName= resource.getFilename();
			// remove UUID
			String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
			
			HttpHeaders headers=new HttpHeaders();
			
			try {
			
				String downloadName= null;
				
				//User-Agent 를 이용한 브라우저 처리
				if ( userAgent.contains("Trident")) {
					log.info("IE browser");
					downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
				}else if(userAgent.contains("Edge")) {
					log.info("Edge browser");
					downloadName =  URLEncoder.encode(resourceOriginalName,"UTF-8");
				}else {
					log.info("Chrome browser");
					downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
				}
				log.info("downloadName: " + downloadName);
				
				//MIME 타입은 다운로드 할수 있는 "APPLICATION_OCTET_STREAM_VALUE" 
				//다운로드 시 저장되는 이름은 "Content-Disposition" 지정 파일이름이 깨지지 않기위해 UTF-8
				headers.add("Content-Disposition", "attachment; filename=" + downloadName);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
			return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
		}
		 
		@PostMapping("/deleteFile")
		@ResponseBody
		public ResponseEntity<String> deleteFile(String fileName,String type){
			
			log.info("deleteFile :" + fileName);
			
			File file;
			
			try {
				file = new File(Constant.UploadPath+"\\"+URLDecoder.decode(fileName, "UTF-8"));
				log.info("file :" + file);
				//image 썸네일과 파일 삭제
				file.delete();
				
				if(type.equals("image")) {
					//실제파일은 썸네일은 s_ 삭제
					String largeFileName = file.getAbsolutePath().replace("s_", "");
					
					log.info("largeFileName :" + largeFileName);
					
					file=new File(largeFileName);
					//실제파일 삭제 
					file.delete();
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<String>("delete",HttpStatus.OK);
		}
}
