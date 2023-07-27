package com.jayuroun.plugin.dnt.request;

import lombok.*;


@Getter
@NoArgsConstructor
public class Auth {

    private Jayuroun jayuroun;
    private Naver naver;

    private Auth(Jayuroun jayuroun) {
        this.jayuroun = jayuroun;
    }

    private Auth(Naver naver) {
        this.naver = naver;
    }

    public static Auth newJayurounInstance(String clientId, String secretKey) {
        return new Auth( new Jayuroun(clientId, secretKey) );
    }

    public static Auth newNaverInstance(String clientId, String clientSecret) {
        return new Auth( new Naver(clientId, clientSecret));
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Jayuroun {

        public static final String HEADER_CLIENT_ID = "clientId";
        public static final String HEADER_CLIENT_SECRET = "secretKey";
        public String clientId;
        private String secretKey;

        public Jayuroun(String clientId, String secretKey) {
            this.clientId = clientId;
            this.secretKey = secretKey;
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Naver {
        public static final String HEADER_CLIENT_ID = "X-Naver-Client-Id";
        public static final String HEADER_CLIENT_SECRET = "X-Naver-Client-Secret";
        private String clientId;
        private String clientSecret;

        public Naver(String clientId, String clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }
    }
}
