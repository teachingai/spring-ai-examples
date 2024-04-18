package com.github.hiwepy.huaweiai.pangu.functions;
import com.huaweicloud.pangu.dev.sdk.api.annotation.AgentTool;
import com.huaweicloud.pangu.dev.sdk.api.annotation.AgentToolParam;
import com.huaweicloud.pangu.dev.sdk.api.tool.StaticTool;
import lombok.Data;

@AgentTool(toolId = "reserve_meeting_room", toolDesc = "预订会议室", toolPrinciple = "请在需要预订会议室时调用此工具",
        inputDesc = "会议开始结束时间，会议室", outPutDesc = "预订会议室的结果")
public class ReserveMeetingRoom extends StaticTool<ReserveMeetingRoom.InputParam, String> {
    @Override
    public String run(InputParam input) {
        return String.format("%s到%s的%s已预订成功", input.start, input.end, input.meetingRoom);
    }
    @Data
    public static class InputParam {
        @AgentToolParam(description = "会议开始时间，格式为HH:mm")
        private String start;
        @AgentToolParam(description = "会议结束时间，格式为HH:mm")
        private String end;
        @AgentToolParam(description = "会议室")
        private String meetingRoom;
    }
}
