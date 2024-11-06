package com.example.account.model;

import java.time.Instant;
import java.util.UUID;

public record Transaction(UUID id, Instant createdAt, UUID accountId, long amount) {}
