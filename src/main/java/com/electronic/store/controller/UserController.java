package com.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.ImageResponse;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.services.FileService;
import com.electronic.store.services.UserService;

/*import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;*/
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.responses.ApiResponse;
@RestController
@RequestMapping("/users")
@Tag(name = "UserController", description = "REST APIS releated to perform user operations!!")
//@CrossOrigin("*")
@SecurityRequirement(name = "scheme1")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "create a new user!!", description = "this is a user api")
	/* @ApiResponses(value = {
	            @Api(code = 200,message = "Success | OK"),
	            @ApiResponse(code = 401, message = "not authorized !!"),
	            @ApiResponse(code = 201, message = "new user created !!")
	    })*/
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @PathVariable String userId, @RequestBody UserDto userDto) {
		UserDto updatedUserDto = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("User is deleted successfully !!")
				.success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "get All users", tags = { "user-controller" })
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
		return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	@Operation(summary = "Get Single user by ID")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> serachUser(@PathVariable String keywords) {
		return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
	}

	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("image") MultipartFile image,
			@PathVariable String userId) throws IOException {
		String imageName = fileService.uploadFile(image, imageUploadPath);

		UserDto userDto = userService.getUserById(userId);

		userDto.setImageName(imageName);

		UserDto updatedUser = userService.updateUser(userDto, userId);
		System.out.println(updatedUser);
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true)
				.status(HttpStatus.CREATED).message("Image upload successfully").build();
		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}

	@GetMapping("image/{userId}")
	public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
		UserDto user = userService.getUserById(userId);
		logger.info("User image name: {}", user.getImageName());
		InputStream resource = fileService.getResourcse(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());

	}
}
