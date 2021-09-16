package org.peng.Reponese;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author sp
 * @date 2021-09-14 17:44
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class GoPushResponse implements Serializable {

    private static final long serialVersionUID = -8016267167202011694L;

    private data[] data;
    private String message;
    private Integer status;
    private Integer timestamp;


    @Data
    @ToString
    private static class data{
        private String error;
        @JSONField(name = "platform_resp")
        private Object platformResp;

        @JSONField(name = "push_status")
        private String pushResult;

        private String token;

        @JSONField(name= "user_id")
        private String useId;

    }


}


