package com.example.account.validation;

public interface Validator<T> {

    ValidationResponse validate(T t);
}
