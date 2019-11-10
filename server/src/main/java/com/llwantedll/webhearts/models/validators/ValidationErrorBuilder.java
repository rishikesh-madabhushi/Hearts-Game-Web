package com.llwantedll.webhearts.models.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public interface ValidationErrorBuilder {
    List<String> getErrors(BindingResult bindingResult);
    ErrorWrapper getErrorsText(BindingResult bindingResult);
}
