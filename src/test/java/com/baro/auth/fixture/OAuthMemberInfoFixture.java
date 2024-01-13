package com.baro.auth.fixture;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;

public class OAuthMemberInfoFixture {

    public static OAuthMemberInfo 유빈() {
        return new OAuthMemberInfo("1", "유빈", "email");
    }

    public static OAuthMemberInfo 태연() {
        return new OAuthMemberInfo("2", "태연", "email");
    }

    public static OAuthMemberInfo 동균() {
        return new OAuthMemberInfo("3", "동균", "email");
    }

    public static OAuthMemberInfo 원진() {
        return new OAuthMemberInfo("4", "원진", "email");
    }

    public static OAuthMemberInfo 은지() {
        return new OAuthMemberInfo("5", "은지", "email");
    }

    public static OAuthMemberInfo 준희() {
        return new OAuthMemberInfo("6", "준희", "email");
    }

    public static OAuthMemberInfo 아현() {
        return new OAuthMemberInfo("7", "아현", "email");
    }
}
