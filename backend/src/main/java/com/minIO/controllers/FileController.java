package com.minIO.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minIO.dao.DownloadLogDao;
import com.minIO.dao.SearchLogDao;
import com.minIO.entity.SearchLog;
import com.minIO.model.APIResponse;
import com.minIO.model.FileDetails;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/minio")
public class FileController {

	@Autowired
	private MinioClient minioClient;

	@Autowired
	public DownloadLogDao downloadLogDao;

	@Autowired
	public SearchLogDao searchLogDao;

	@Value("${minio.bucketName}")
	private String bucketName;

	@Value("${minio.downloadDirectory}")
	private String downloadDirectory; 

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@GetMapping("/files")
	public APIResponse listFilesInBucket() {
		APIResponse apiResponse = new APIResponse();
		//		SearchLog searchLog = new SearchLog();
		try {
			List<Map<String, String>> objectsList = new ArrayList<>();
			Iterable<Result<Item>> results = minioClient.listObjects(
					ListObjectsArgs.builder().bucket(bucketName).build());
			for (Result<Item> result : results) {
				Item item = result.get();
				if(null!=item.userMetadata()) {
					item.userMetadata().entrySet().stream().forEach(f->{
						System.out.println(f);
					});
				}
				Map<String, String> objectMap = new HashMap<>();
				objectMap.put("id", item.etag().replace("\"", ""));
				objectMap.put("fileName", item.objectName());
				objectMap.put("lastModified", item.lastModified().toString());
				objectMap.put("size", String.valueOf(item.size()));
				objectsList.add(objectMap);
			}
			if(!objectsList.isEmpty()) {
				apiResponse.setObject(objectsList);
				apiResponse.setStatus(true);
				apiResponse.setSenderCount( (long) objectsList.size());
				apiResponse.setMessage("Successfully fetch the files from the " + bucketName + " bucket.");
				log.info("Successfully fetch the files from the " + bucketName + " bucket.");
			} else {
				apiResponse.setStatus(false);
				apiResponse.setMessage("No data available in the " + bucketName + " bucket.");
				log.info("No data available in the " + bucketName + " bucket.");
			}
			//			searchLog.setTotalFiles((long) objectsList.size());
			//			searchLog.setSearchResult(objectsList.toString());
			//			searchLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
			//			searchLog.setUserId((long) 101);
			//			searchLogDao.save(searchLog);
			return apiResponse;
		} catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException e) {
			log.error("Problem in geting file: ",e);
			apiResponse.setMessage(e.getMessage());
			apiResponse.setObject(null);
			apiResponse.setStatus(false);
			return apiResponse;
		}
	}

//	@GetMapping("/download/{fileName}")
//	public APIResponse downloadObject(@PathVariable String fileName) {
//		APIResponse apiResponse = new APIResponse();
//		try {
//			InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
//					.bucket(bucketName)
//					.object(fileName)
//					.build());
//			
//			byte[] fileContent = convert(inputStream);
//			System.out.println(fileContent.length);
//			apiResponse.setObject(fileContent);
//			apiResponse.setMessage("File downloaded successfully.");
//			apiResponse.setStatus(true);
//			return apiResponse;
//		} catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException e) {
//			log.error("There is something worng ", e);
//			apiResponse.setMessage(HttpStatus.INTERNAL_SERVER_ERROR+" Error downloading file: " + e.getMessage());
//			apiResponse.setStatus(false);
//			apiResponse.setObject(null);
//			return apiResponse;
//		}
//	}
	
	@GetMapping("/download/{fileName}")
	public void downloadObject(@PathVariable String fileName, HttpServletResponse response) {
		APIResponse apiResponse = new APIResponse();
		try {
			System.out.println("Filename: "+fileName);
			InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.build());
			
			if (fileName.toLowerCase().endsWith("pdf")) {
				response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			} else if (fileName.toLowerCase().endsWith("txt")) {
				response.setContentType(MediaType.TEXT_PLAIN_VALUE);
			} else if (fileName.toLowerCase().endsWith("xls")) {
				response.setContentType("application/ms-excel");
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			} else if (fileName.toLowerCase().endsWith("png")) {
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
			} else if (fileName.toLowerCase().endsWith("jpeg")) {
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			}
			byte[] fileContent = convert(inputStream);
			response.getOutputStream().write(fileContent);
			
			System.out.println(fileContent.length);
			apiResponse.setObject(fileContent);
			apiResponse.setMessage("File downloaded successfully.");
			apiResponse.setStatus(true);
		} catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException e) {
			log.error("There is something worng ", e);
			apiResponse.setMessage(HttpStatus.INTERNAL_SERVER_ERROR+" Error downloading file: " + e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setObject(null);
		}
	}

	@GetMapping("/search/{fileName}")
	public APIResponse searchFiles(@PathVariable String fileName) {
		APIResponse apiResponse = new APIResponse();
		SearchLog searchLog = new SearchLog();
		try {
			List<FileDetails> matchedFiles = new ArrayList<>();
			Iterable<Result<Item>> results = minioClient.listObjects(
					ListObjectsArgs.builder().bucket(bucketName).build());
			long id = 1;
			for (Result<Item> result : results) {
				Item item = result.get();
				String filefullName = item.objectName();
				if (filefullName.toLowerCase().equals(fileName.toLowerCase())) {
					FileDetails fileDetails= new FileDetails();
					fileDetails.setId(id);
					fileDetails.setFileName(fileName);
					fileDetails.setLastModified(item.lastModified().toString());
					fileDetails.setSize(item.size());
					matchedFiles.add(fileDetails);
				}
			}
			if(!matchedFiles.isEmpty()) {
				apiResponse.setMessage("File found successfully.");
				apiResponse.setStatus(true);
				apiResponse.setObject(matchedFiles);
				searchLog.setTotalFiles((long) matchedFiles.size());
				searchLog.setSearchResult(matchedFiles.get(0).getFileName());
				searchLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
				searchLog.setSearchquery("Searched "+ fileName);
				searchLog.setUserId((long) 101);
				searchLogDao.save(searchLog);
			} else {
				apiResponse.setMessage("File not found in the "+bucketName+" bucket.");
				apiResponse.setStatus(false);
				apiResponse.setObject(null);
				searchLog.setTotalFiles((long) matchedFiles.size());
				searchLog.setSearchResult("Not found "+fileName);
				searchLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
				searchLog.setSearchquery("Searched "+ fileName);
				searchLog.setUserId((long) 101);
				searchLogDao.save(searchLog);
			}
			return apiResponse;
		} catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException | IOException e) {
			log.error("There is something worng ", e);
			apiResponse.setMessage(HttpStatus.INTERNAL_SERVER_ERROR+" Error downloading file: " + e.getMessage());
			apiResponse.setStatus(false);
			apiResponse.setObject(null);
			return apiResponse;
		}
	}
	
	public static byte[] convert(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            inputStream.transferTo(outputStream);
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return outputStream.toByteArray();
    }

}

