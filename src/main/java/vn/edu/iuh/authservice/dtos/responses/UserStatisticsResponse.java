package vn.edu.iuh.authservice.dtos.responses;


import java.util.Map;

public record UserStatisticsResponse(
        int totalUsers,
        int newUsers,
        int returningUsers,
        Map<String, Integer> usersByAgeGroup,
        Map<String, Integer> usersByGender
) {}