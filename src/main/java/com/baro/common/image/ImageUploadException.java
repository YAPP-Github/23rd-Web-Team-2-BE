package com.baro.common.image;


/**
 * p4. @see com.baro.common.exception.RequestException
 * 이 클래스를 상속하는게 좋지 않을까요?
 * 에러 관련 처리를 팀 컨밴션이 명확하지 않은 것 같아요.
 */
public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
