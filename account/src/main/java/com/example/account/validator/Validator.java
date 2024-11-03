package com.example.account.validator;

public interface Validator<T> {

    boolean isValid(T t);
}
