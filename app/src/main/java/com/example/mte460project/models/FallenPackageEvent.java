package com.example.mte460project.models;

public class FallenPackageEvent {
    private String fallenPackageEventId;
    private String companyId;
    private String conveyorBeltId;
    private Long createdDate;

    public FallenPackageEvent(){}

    public FallenPackageEvent(String fallenPackageEventId, String companyId, String conveyorBeltId, Long createdDate) {
        this.fallenPackageEventId = fallenPackageEventId;
        this.companyId = companyId;
        this.conveyorBeltId = conveyorBeltId;
        this.createdDate = createdDate;
    }

    public String getFallenPackageEventId() {
        return fallenPackageEventId;
    }

    public void setFallenPackageEventId(String fallenPackageEventId) {
        this.fallenPackageEventId = fallenPackageEventId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getConveyorBeltId() {
        return conveyorBeltId;
    }

    public void setConveyorBeltId(String conveyorBeltId) {
        this.conveyorBeltId = conveyorBeltId;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "FallenPackageEvent{" +
                "fallenPackageEventId='" + fallenPackageEventId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", conveyorBeltId='" + conveyorBeltId + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
