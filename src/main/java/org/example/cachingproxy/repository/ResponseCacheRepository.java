package org.example.cachingproxy.repository;

import org.example.cachingproxy.model.ResponseCache;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResponseCacheRepository extends JpaRepository<ResponseCache, String> {
}
