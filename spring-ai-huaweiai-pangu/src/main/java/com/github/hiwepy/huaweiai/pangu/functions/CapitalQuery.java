package com.github.hiwepy.huaweiai.pangu.functions;

import com.huaweicloud.pangu.dev.sdk.api.annotation.AgentTool;
import com.huaweicloud.pangu.dev.sdk.api.tool.StaticTool;

@AgentTool(toolId = "capital", toolDesc = "资产注册查询", toolPrinciple = "请在需要查询各个公司的资产注册情况时调用此工具",
        inputDesc = "需要查询的公司名称，一次只支持查询一家公司", outPutDesc = "公司的资产注册规模")
public class CapitalQuery extends StaticTool<String, String> {
    @Override
    public String run(String input) {
        switch (input) {
            case "x公司":
                return "x亿人民币";
            case "y公司":
                return "y亿人民币";
            case "z公司":
                return "z亿人民币";
            default:
                return "未知";
        }
    }
}
