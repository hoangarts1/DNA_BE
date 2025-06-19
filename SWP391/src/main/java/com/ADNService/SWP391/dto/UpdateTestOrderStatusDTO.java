package com.ADNService.SWP391.dto;

import lombok.Data;

public class UpdateTestOrderStatusDTO {
    private Long staffId;
    private String orderStatus;

    public UpdateTestOrderStatusDTO() {
    }

    public UpdateTestOrderStatusDTO(String orderStatus, Long staffId) {
        this.orderStatus = orderStatus;
        this.staffId = staffId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
