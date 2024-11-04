package com.example.account.validation;

public interface CombinedValidator<T, U> {

    ValidationResponse validate(T t, U u);
}
