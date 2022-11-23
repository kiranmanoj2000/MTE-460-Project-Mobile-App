package com.example.mte460project;

public class DroppedPackage {
    private String companyId;
    private String conveyorBeltId;
    private Long createdDate;

    public DroppedPackage(){}

    public DroppedPackage(String cid, String bid, Long cd)
    {
        this.companyId = cid;
        this.conveyorBeltId = bid;
        this.createdDate = cd;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public String getConveyorBeltId()
    {
        return conveyorBeltId;
    }
    public Long getCreatedDate()
    {
        return createdDate;
    }

    public void setCompanyId(String company)
    {
        this.companyId = company;
    }

    public void setConveyorBeltId(String belt)
    {
        this.conveyorBeltId = belt;
    }

    public void setCreatedDate(Long dateCreated)
    {
        this.createdDate = dateCreated;
    }
}
