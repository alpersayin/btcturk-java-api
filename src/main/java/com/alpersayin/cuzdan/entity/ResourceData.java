package com.alpersayin.cuzdan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceData {

    public String pair;
    public String pairNormalized;
    public Timestamp timestamp;
    public Long last;
    public Long high;
    public Long low;
    public Long bid;
    public Long ask;
    public Double volume;
    public Double average;
    public Long daily;
    public Double dailyPercent;
    public String denominatorSymbol;
    public String numeratorSymbol;

}
