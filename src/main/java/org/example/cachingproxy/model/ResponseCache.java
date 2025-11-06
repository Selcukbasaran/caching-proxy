package org.example.cachingproxy.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cache_storage")
public class ResponseCache {
    @Id
    @Column(length = 1024)
    private String path;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String responseHeaders;

    private int statusCode;


}
