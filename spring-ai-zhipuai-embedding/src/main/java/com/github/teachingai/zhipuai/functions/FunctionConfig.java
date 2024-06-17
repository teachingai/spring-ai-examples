package com.github.teachingai.zhipuai.functions;

import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {

    @Bean
    @Description("获取天气：根据给出的城市ID获取天气信息") // function description
    public Function<GetWeatherFunction.Request, GetWeatherFunction.Response> weatherFunction() {
        return new GetWeatherFunction();
    }

    @Bean
    public FunctionCallback ipRegionFunctionInfo() {
        return FunctionCallbackWrapper.builder(new PconlineRegionFunction())
                .withName("getLocationByIp") // (1) function name
                .withDescription("IP地址解析: 根据IP解析IP所在位置信息") // (2) function description
                .withSchemaType(FunctionCallbackWrapper.Builder.SchemaType.JSON_SCHEMA) // (3) schema type. Compulsory for Gemini function calling.
                .withInputType(PconlineRegionFunction.Request.class) // (4) input type
                .withResponseConverter((response) -> String.format("您的IP当前位置是：%s", response.addr())) // (6) response converter
                .build();
    }

}
