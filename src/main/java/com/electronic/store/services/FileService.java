package com.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.electronic.store.dtos.CreateOrderRequest;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String uploadFile(MultipartFile file, String path) throws IOException;

	InputStream getResourcse(String path, String name) throws FileNotFoundException;


}
