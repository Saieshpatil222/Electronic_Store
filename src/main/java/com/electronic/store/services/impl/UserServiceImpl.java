package com.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Value("${normal.role.id}")
	private String normalRoleId;
	
	@Autowired
	private RoleRepository roleReposiotry;
	
	@Autowired
	private ModelMapper mapper;

	@Value("${user.profile.image.path}")
	private String imagePath;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDto createUser(UserDto userDto) {

		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user = dtoToEntity(userDto);
		
		Role role = roleReposiotry.findById(normalRoleId).get();
		user.getRoles().add(role);
		User savedUser = userRepository.save(user);
		UserDto updatedDto = entityToDto(savedUser);
		return updatedDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));

		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());

		User updatedUser = userRepository.save(user);
		UserDto updatedDto = entityToDto(updatedUser);
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
		String fullPath = imagePath + user.getImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
			userRepository.delete(user);
		} catch (NoSuchFileException e) {
			logger.info("User image not found in folder");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy)).ascending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> page = userRepository.findAll(pageable);

		PageableResponse<UserDto> response = Helper.getPagableResponse(page, UserDto.class);
		return response;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email and id "));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findByNameContaining(keyword);
		List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
		return dtoList;
	}

	private UserDto entityToDto(User savedUser) {
		/*
		 * UserDto.builder().userId(savedUser.getUserId()).name(savedUser.getName()).
		 * email(savedUser.getEmail())
		 * .password(savedUser.getPassword()).about(savedUser.getAbout()).gender(
		 * savedUser.getGender()) .imageName(savedUser.getImageName()).build();
		 */
		return mapper.map(savedUser, UserDto.class);
	}

	private User dtoToEntity(UserDto userDto) {
		/*
		 * User user =
		 * User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(
		 * userDto.getEmail())
		 * .password(userDto.getPassword()).about(userDto.getAbout()).gender(userDto.
		 * getGender()) .imageName(userDto.getImageName()).build();
		 */

		return mapper.map(userDto, User.class);
	}

	@Override
	public Optional<User> findUserByEmailOptional(String email) {
		
		return userRepository.findByEmail(email);
	}
}
