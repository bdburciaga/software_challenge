package com.burciaga.plutotv.softwarechallenge.edit;

import com.burciaga.plutotv.softwarechallenge.utils.validation.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class EditResult {
    private ValidationResult validationResult;
    private String updatedContent = "";
    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public String getUpdatedContent() {
        return updatedContent;
    }

    public void setUpdatedContent(String updatedContent) {
        this.updatedContent = updatedContent;
    }
}
