package edu.gsu.eventplanner.common.response;

public record EventPlannerResponse<T> (
        T data,
        Object meta
){
}
