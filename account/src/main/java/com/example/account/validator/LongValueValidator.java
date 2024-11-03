package com.example.account.validator;

public class LongValueValidator implements Validator<Long> {

    private final long min;

    private final long max;

    public LongValueValidator(Long min, Long max) {
        this.min = min == null ? Long.MIN_VALUE : min;
        this.max = max == null ? Long.MAX_VALUE : max;
    }

    @Override
    public boolean isValid(Long aLong) {
        return aLong != null && aLong >= min && aLong <= max;
    }
}
