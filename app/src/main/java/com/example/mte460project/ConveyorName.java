package com.example.mte460project;

public class ConveyorName {
    private String companyId;
    private Long createddate;
    private String name;

    public ConveyorName() {};

    public ConveyorName(String comid, Long cdate, String cname)
    {
        this.companyId = comid;
        this.createddate = cdate;
        this.name = cname;
    };

    public String getCompanyId() {
        return companyId;
    }

    public Long getCreateddate() {
        return createddate;
    }

    public String getName() {
        return name;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCreateddate(Long createddate) {
        this.createddate = createddate;
    }

    public void setName(String name) {
        this.name = name;
    }
}
