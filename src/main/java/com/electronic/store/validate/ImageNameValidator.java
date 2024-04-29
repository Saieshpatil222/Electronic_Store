package com.electronic.store.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		logger.info("Message form isValid():{}",value);
		if (value.isBlank()) {
			return false;
		}
		else {
			return true;
		}
	}

}

