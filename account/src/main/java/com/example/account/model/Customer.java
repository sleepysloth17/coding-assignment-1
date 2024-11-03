package com.example.account.model;

import java.util.UUID;

public record Customer(
        UUID id,
        String name,
        String surname
) {}
