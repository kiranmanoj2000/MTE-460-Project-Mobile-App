package com.example.mte460project.models;

public class ConveyorBelt {
    private String conveyorBeltId;
    private String companyId;
    private Long createdDate;
    private String name;

    @Override
    public String toString() {
        return this.name;
    }

    public ConveyorBelt(String conveyorBeltId, String companyId, Long createdDate, String name){
        this.conveyorBeltId = conveyorBeltId;
        this.companyId = companyId;
        this.createdDate = createdDate;
        this.name = name;
    }

    public String getConveyorBeltId() {
        return conveyorBeltId;
    }

    public void setConveyorBeltId(String conveyorBeltId) {
        this.conveyorBeltId = conveyorBeltId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
