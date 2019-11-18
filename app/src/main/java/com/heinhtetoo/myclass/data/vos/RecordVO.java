package com.heinhtetoo.myclass.data.vos;

public class RecordVO {

    String recordId;

    Boolean recordValue;

    public RecordVO(String recordId, Boolean recordValue) {
        this.recordId = recordId;
        this.recordValue = recordValue;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Boolean getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(Boolean recordValue) {
        this.recordValue = recordValue;
    }
}
