package org.peng.Reponese;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

/**
 * @author sp
 * @date 2021-09-16 15:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GoAppPushParam {

    @JSONField(name = "action_id")
    private String actionId;

    @JSONField(name = "app_ids")
    private String[] appIds;


    private message message;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static class message {
        private String title;
        private String body;
        private data data;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @EqualsAndHashCode
        @ToString
        private static class data {
            private final String type = "1";
            private String bookId;
        }
    }
}
