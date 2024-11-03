package com.example.account.model;

import java.util.UUID;

public record Account(
        UUID id,
        UUID customerId
) {}
