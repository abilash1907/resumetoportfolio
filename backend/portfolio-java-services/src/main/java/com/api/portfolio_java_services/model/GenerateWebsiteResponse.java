package com.api.portfolio_java_services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateWebsiteResponse {
    String htmlContent;
    String siteId;
}
