package ru.aquamarina.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.data.annotation.Id;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public class ProductDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private UUID id;

    private String name;

    // cost should be in copeika(копейка)
    private Long cost;

    private String description;

    private String path;

    private Long itemCode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getItemCode() {
        return itemCode;
    }

    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
    }
}
