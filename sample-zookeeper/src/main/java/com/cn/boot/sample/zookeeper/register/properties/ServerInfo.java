package com.cn.boot.sample.zookeeper.register.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    private List<AppTagInfo> appTags;
    private int maxConnect = 50000;
    private double maxCpu = 80;
    private String serverUri;
}
