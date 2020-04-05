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
 * ���� ���ε�� ����� ��Ȳ
 * 1.������ �̸����� ���� ���ε� �� ��� ���� ������ �������.           >> UUID �� �̿��Ѵ�.
 * 2.�̹��� ������ ��쿡�� ���� ������ �뷮�� ū ��� ������ �̹��� ����      >> pom.xml ���� thumbnailator �߰�
 * 3.�̹������ϰ� �Ϲ� ������ �з��ؼ� �ٿ�ε� Ȥ�� ��ȸ�ϵ��� ó��
 * 4.÷�����Ͽ� ���ݿ� ���� Ȯ���� ����
 */

@Controller
@Log4j
public class UploadController {
	//�ش��� ���� ���� (�ش����� ����)
	//�� ���� ���� ������ ��� �ӵ��� ���Ͽ� ���� ���� ���� �ذ�
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date= new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	//�̹��� ���� Ȯ��
	private boolean checkImageType(File file) {
	
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");  //�̹��� ���� üũ
			
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
		
		log.info("���ε� Form");
	}
	
	 @PostMapping("/uploadFormAction")
	 public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
	
		 String uploadFolder=Constant.UploadPath;  //���� ���� ��Ʈ 
		 
		 for (MultipartFile multipartFile : uploadFile) {   //�������� ���ε� 
			
			 log.info("-------------------------------------");
			 log.info("Upload File Name: " +multipartFile.getOriginalFilename());
			 log.info("Upload File Size: " +multipartFile.getSize());
			 log.info("-------------------------------------");
			 
			 File saveFile =new File(uploadFolder, multipartFile.getOriginalFilename());  //���� �����ڸ� ����� (���ϰ�ο� ����� ���� �̸��� ���� �����Ѵ�.)
			 
			 try {
				multipartFile.transferTo(saveFile); //���� ����
				log.info("����Ϸ� :"+ multipartFile.getOriginalFilename());  //������ ��� ���� �޽��� 
			} catch (Exception e) {
				log.error(e.getMessage());  //������ ��� ���� �޽��� 
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
		 
		 String uploadFolder=Constant.UploadPath ; //���� ���� ��Ʈ 
		 
		 //���� ����
		 File uploadPath = new File(uploadFolder,getFolder());
		 log.info("upload path : " + uploadPath);	 
		 //�ش� ������ ������ ��� ���� ���ϰ� �Ѿ
		 if(uploadPath.exists()==false) {  
			 uploadPath.mkdirs();
		 }
		 
		 for (MultipartFile multipartFile : uploadFile) {
			 
			 log.info("-------------------------------------");
			 log.info("Upload File Name: " +multipartFile.getOriginalFilename());
			 log.info("Upload File Size: " +multipartFile.getSize());
			 
			 String uploadFileName =multipartFile.getOriginalFilename();
			 
			 //IE�� ������ ��� ��ü ���� ��ΰ� ���� �Ǳ� ������ ������ \ �������� �߶󳻸�  == ���� �����̸� �� �ȴ�
			 uploadFileName =uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);  
			 
			 log.info("only file name: " + uploadFileName);
			 
			 //UUID ����
			 UUID uuid=UUID.randomUUID();			 
			 //���� �̸��� ���� �ϱ� ���ؼ� "_" �߰�  
			 uploadFileName=uuid.toString() + "_" +uploadFileName;
			 
			 log.info("file name overlap remove: " + uploadFileName);
			 
			 try {
				 
				 File saveFile =new File(uploadPath, uploadFileName);  //������ ���� ������
				 multipartFile.transferTo(saveFile);   // ����
				 
				 if(checkImageType(saveFile)) {
					 /*  �̹��� ������ ��쿡 ����� ���� */
					 //����� ������ ���� 
					 FileOutputStream thumnail = new FileOutputStream(new File(uploadPath,"s_"+ uploadFileName));
					 //createThumbnail(��������,���� Ȯ���� ��, ���� , ũ��)
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
			//���� ���� ��Ʈ 
			String uploadFolder=Constant.UploadPath;
			//���� ���� ����
			String uploadFolderPath= getFolder();
			//����� ����
			File uploadPath = new File(uploadFolder,uploadFolderPath);
			//�ش��� ���� ���� (�����ϸ� pass)
			if(uploadPath.exists() ==false) {
				uploadPath.mkdirs();
			}
			
			for(MultipartFile multipartFile : uploadFile) {
				
				AttachFileDTO attachDTO =new AttachFileDTO();
				
				String uploadFileName = multipartFile.getOriginalFilename();
				
				 //IE�� ������ ��� ��ü ���� ��ΰ� ���� �Ǳ� ������ ������ \ �������� �߶󳻸�  == ���� �����̸� �� �ȴ�
				 uploadFileName =uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);  
				 log.info("only file name: " + uploadFileName);
				 
				 //�����̸��� �־��ش�.
				 attachDTO.setFileName(uploadFileName);
				 
				 //UUID ����
				 UUID uuid=UUID.randomUUID();			 
				 //���� �̸��� ���� �ϱ� ���ؼ� "_" �߰�  
				 uploadFileName=uuid.toString() + "_" +uploadFileName;
				 
				 log.info("file name overlap remove: " + uploadFileName);
				 
				 //uuid �� ���� ��θ� �־��ش�. (���ϰ�δ� ����� �����⿡ ���� �̸��� ���´�.)
				 attachDTO.setUuid(uuid.toString());
				 attachDTO.setUploadPath(uploadFolderPath);
				 
				 try {
					 File saveFile =new File(uploadPath, uploadFileName);  //������ ���� ������
					 multipartFile.transferTo(saveFile);   // ����
					 
					 if(checkImageType(saveFile)) {
						 
						 attachDTO.setImage(true); 
						 /*  �̹��� ������ ��쿡 ����� ���� */
						 //����� ������ ���� 
						 FileOutputStream thumnail = new FileOutputStream(new File(uploadPath,"s_"+ uploadFileName));
						 //createThumbnail(��������,���� Ȯ���� ��, ���� , ũ��)
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
	 
		//����� �����ֱ�
		@GetMapping("/display")
		@ResponseBody
		public ResponseEntity<byte[]>getFile(String fileName){
			
			log.info("fileName :"+fileName);

			//FileName�� �Ķ���ͷ� byte[] �ް� ���� 
			File file= new File(Constant.UploadPath+"//"+fileName); 
			
			log.info("file :" +file);
			
			ResponseEntity<byte[]> result =null;
			
			try {
				HttpHeaders headers= new HttpHeaders();
				headers.add("Content-Type", Files.probeContentType(file.toPath())); //������ ������ ���� MIME Ÿ���� �޶����⿡  ��� �޽����� ���� �� �� �ֵ��� ó��
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
			
			//�ش� ��ο��� ���ҽ�(����) �� �о�´�.
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
				
				//User-Agent �� �̿��� ������ ó��
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
				
				//MIME Ÿ���� �ٿ�ε� �Ҽ� �ִ� "APPLICATION_OCTET_STREAM_VALUE" 
				//�ٿ�ε� �� ����Ǵ� �̸��� "Content-Disposition" ���� �����̸��� ������ �ʱ����� UTF-8
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
				//image ����ϰ� ���� ����
				file.delete();
				
				if(type.equals("image")) {
					//���������� ������� s_ ����
					String largeFileName = file.getAbsolutePath().replace("s_", "");
					
					log.info("largeFileName :" + largeFileName);
					
					file=new File(largeFileName);
					//�������� ���� 
					file.delete();
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<String>("delete",HttpStatus.OK);
		}
}
